#define MAX_CONCURRENT_WORKERS 18
#define MAX_TOTAL_WORKERS 100
#define MAX_SPAWN_TIME 3
#define SPAWN_THRESHOLD 20



#include <stdio.h>
#include <stdbool.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/queue.h>
#include <sys/shm.h>
#include <signal.h>
#include <time.h>
#include <errno.h>
#include "resource.h"



//globals
time_t startTime;
int totalWorkers = 0;
int currentWorkers = 0;
int pendingWorkers = 0;
int requestsGranted = 0;
int executedWorkers = 0;
int pageFaults = 0;
int accessSpeed = 0;
int listenerMQId, sendMQId, ossMemId;
const int NANO_INCREMENT = 1000;
int TERM_FLAG = 0;
int linesRemaining = 100000; //total number of lines that can be written to the log
struct sysclock* osclock;

struct frameTable frameList;
struct proc_pages pageTable[MAX_CONCURRENT_WORKERS];


//blocked requests queue
struct blockedQueue {
    int size;
    struct resource requests[MAX_CONCURRENT_WORKERS];

};

struct blockedQueue blockedRequests;

char* logFile = "oss.log";
FILE *logfp;
bool verbose = false;


void addBlockedRequest(struct resource request)
{
    blockedRequests.requests[blockedRequests.size] = request;
    blockedRequests.size++;
}




/**
 * Cleanup of the processes and resources
 * used by this application
 */
void cleanupResources()
{
    int i = 0;

    printf("Terminating program\n");

    //kill known processes
    for(i = 0; i < MAX_CONCURRENT_WORKERS; i++)
    {
        if(pageTable[i].pid > 0)
        {
            kill(pageTable[i].pid, SIGTERM);
            waitForTerm(pageTable[i].pid);

        }
        else
        {
            continue;
        }
    }


    //close logs
    fclose(logfp);

    //close Message queues
    msgctl(listenerMQId, IPC_RMID, NULL);
    msgctl(sendMQId, IPC_RMID, NULL);
    //clear shared memory
    shmctl(ossMemId, IPC_RMID, NULL);
}

void termFlag()
{
    TERM_FLAG = 1;
}

void printHelp()
{
    FILE *fp;
    char* buffer;
    ssize_t read;
    size_t len = 0;

    fp = fopen("help", "r");

    while((read = getline(&buffer, &len, fp)) != -1)
    {
        printf("%s", buffer);
    }
    fclose(fp);
}


/** Handles incoming parameters **/
int handleParams(int argCount, char *argString[])
{
    int result = 0;
    int options;
    while((options = getopt(argCount, argString, ":hvf:")) != -1)
    {
        switch(options)
        {
            case 'h':
                printHelp();
                result = -1;
                break;
            case 'f':
                logFile = optarg;
                break;
            case ':':
                printf("%c missing option value\n", optopt);
                break;
            case 'v':
                printf("Verbose logging enabled\n");
                verbose = true;
                break;
            case '?':
            default:
                printf("Unknown option: %c\n", optopt);
                result = -1;
                break;

        }

        if(result == -1)
        {
            printf("Program did not run.  see help (oss -h) for execution details\n");
            printf("Ending program\n");
            break;
        }
    }

    return result;
}

void logToFile(char entry[200])
{
    int len = strlen(entry);
    char logItem[210] = "OSS:";

    if(len > 0)
    {
        if(linesRemaining > 0)
        {
            strcat(logItem, entry);
            strcat(logItem, "\n");
            fprintf(logfp, logItem);
            linesRemaining--;
        }

    }


}

/**
 * writes content to console and logfile
 * @param entry
 */
void writeToConsole(char entry[200])
{
    printf("%s\n", entry);
    logToFile(entry);
}

/**
 * Prints the frame table to
 * the console
 */
void printAllocationTable()
{
    int i;
    char line[100];
    //print header
    sprintf(line, "%25s %20s %10s", "Occupied", "Last Operation", "Head");
    writeToConsole(line);

    for(i = 0; i < FRAME_TABLE_SIZE; i++)
    {
        char* occupied = frameList.frames[i].occupied?"+":".";
        char* operation = frameList.frames[i].write == 0?"r":"w";
        char* head = frameList.frames[i].head?"*":"";
        char frame[20];
        sprintf(frame, "Frame %i:", i + 1);

        if(occupied == "."){
            operation = "";
        }
        sprintf(line, "%-15s %5s %15s %15s",frame, occupied, operation, head);
        writeToConsole(line);
    }


}


/** Increments the simulated clock of the system **/
void incrementClock(int mult)
{
    int nanos = osclock->nanoseconds;
    nanos += NANO_INCREMENT * mult;
    osclock->seconds = osclock->seconds + (nanos / NANOS_IN_SECOND);
    osclock->nanoseconds = nanos % NANOS_IN_SECOND;

}


/**
 * Update the resource allocation table
 * @param pid
 * @param resId
 * @param allocation
 */
void addPage(int pid)
{
    int i;
    struct proc_pages newPage;

    for(i = 0; i < MAX_CONCURRENT_WORKERS; i++)
    {
        if(pageTable[i].pid > 0)
        {
            continue;
        }
        else
        {
            newPage.pid = pid;
            pageTable[i] = newPage;
            break;
        }
    }

}

/**
 * Release a resource from a process
 * @param resId
 * @param pid
 * @param allocation the positive number of resources to release
 */
void removePage(int pid)
{
    char logEntry[200];
    int i;
    struct proc_pages emptyPage;

    for(i = 0; i < MAX_CONCURRENT_WORKERS; i++)
    {
        if(pageTable[i].pid != pid)
        {
            continue;
        }
        else
        {
            if(pageTable[i].pid == pid) //remove page entries
            {
                emptyPage.pid = 0;
                pageTable[i] = emptyPage;
                sprintf(logEntry, "Pages for PID %i removed", pid);
                logToFile(logEntry);
            }
            break;
        }
    }
}

/** Loops until a child process is terminated.
 * Increments OS clock
 * @return -1 for error
 * process Id of child on success
 */
int waitForTerm(int childPid)
{
    int result = 0;
    char logEntry[200];
    int pidStatus;
    do
    {
        result = waitpid(childPid, &pidStatus, WNOHANG);
        incrementClock(10);
    }while(result != childPid);
    removePage(childPid);
    currentWorkers--;
    executedWorkers++;
    sprintf(logEntry, "PID %d has terminated at S:%d N:%d", childPid, osclock->seconds, osclock->nanoseconds);
    writeToConsole(logEntry);
    return result;
}

/**
 * Forks and creates a child process
 * @return PID of child process
 */
int createChild()
{
    pid_t childPid; // process ID of a child executable

    childPid = fork();
    if (childPid == -1) // error
    {
        logToFile("An error occurred during fork");
        cleanupResources();
        perror("An error occurred during fork");
        exit(1);
    }
    else if (childPid == 0) //this is the child node.  run program
    {
        char* args[] = {"./user_proc", NULL};
        execvp(args[0], args);
    }
    else //parent. add child to allocation table
    {
        currentWorkers++;
        totalWorkers++;
        //TODO: add new page record
        addPage(childPid);
        incrementClock(5);
    }

    return childPid;
}









/**
 * Determines if child processes can be spawned
 * @return true if child processes can be spawned.
 * otherwise false
 */
bool canSpawnChildren()
{
    bool result = false;
    time_t now;
    time(&now);

    if(now - startTime < MAX_SPAWN_TIME && totalWorkers < MAX_TOTAL_WORKERS)
    {
        result = true;
    }

    return  result;
}

/**
 * Creates a resource message for
 * MQ transport
 * @param pid
 * @param resId
 * @param allocation
 * @return
 */
struct resourcemsg makeResourceMessage(int pid, int operation, int address)
{
    struct resourcemsg msg;
    msg.msgType = pid;

    sprintf(msg.message, "%d:%d", operation, address);

    return msg;
}

/**
 * parses a message received from a child process
 * @param childMsg
 * @return
 */
struct resource getChildMessage(struct resourcemsg childMsg, int pid)
{
    struct resource result;

    char* token = strtok(childMsg.message, ":");
    result.operation = atoi(token);
    token = strtok(NULL, ":");
    result.base = atoi(token);
    token = strtok(NULL, ":");
    result.offset = atoi(token);
    token = strtok(NULL, ":");
    result.address = atoi(token);

    result.pid = pid;

    return result;
}

/**
 * Handle the creation of child processes
 */
void manageProcesses()
{
    int spawnChance;
    if(canSpawnChildren())  //chance to spawn child process
    {

        spawnChance =  rand() % 100;

        //check for max running processes
        if(spawnChance >= SPAWN_THRESHOLD)
        {
            if(totalWorkers < MAX_TOTAL_WORKERS)
            {
                if(currentWorkers >= MAX_CONCURRENT_WORKERS) //create child later
                {
                    pendingWorkers++;
                    totalWorkers++;
                }
                else
                {
                    //child creation
                    createChild();
                }
            }
        }
    }
    else if(pendingWorkers > 0 && currentWorkers < MAX_CONCURRENT_WORKERS) //manage pending workers
    {
        createChild();
        pendingWorkers--;
    }
}

/**
 * Gets the number of the page to update
 * @param pageRequest
 * @return
 */
int getPage(struct resource pageRequest){
    int page = 0;
    page = (pageRequest.address / pageRequest.base) - (pageRequest.offset / pageRequest.base);
    return page;
}

void updateFrame(int frameIndex, struct resource request)
{

    frameList.frames[frameIndex].pid = request.pid;
    frameList.frames[frameIndex].occupied = true;
    frameList.frames[frameIndex].id = request.address;
    frameList.frames[frameIndex].write = request.operation;
    if(request.operation > 0)
    {
        incrementClock(3);
    }
    else
    {
        incrementClock(1);
    }

}

/**
 * Updates page with an associated frame index
 * @param frameIndex
 * @param offset
 * @param page
 * @param pid
 */
void updatePage(int frameIndex, int offset, int page, int pid)
{
    int i;
    char logEntry[200];
    for(i = 0; i < MAX_CONCURRENT_WORKERS; i++)
    {
        if(pageTable[i].pid != pid)
        {
            incrementClock(1);
            continue;
        }
        else
        {
            pageTable[i].pages[page].frames[offset] = frameIndex;
            sprintf(logEntry, "Page %d updated for PID %d with frame %d", page + 1, pid, frameIndex + 1);
            logToFile(logEntry);
            incrementClock(1);
            break;
        }
    }
}

/**
 * looks for the next available frame
 * @param request
 * @param pid
 * @return index of next available frame, or -1 for page fault
 */
int frameSearch(struct resource request)
{
    int i = frameList.headIndex;
    int result = -1;

    do
    {
        if(!frameList.frames[i].occupied)
        {
            result = i;
            break;
        }
        else if(frameList.frames[i].id == request.address)
        {
            result = i;
            break;
        }

        i++;
        if(i == FRAME_TABLE_SIZE)
        {
            i = 0;
        }
    }
    while( i != frameList.headIndex);

    return result;
}

/**
 * Performs frame update in the event of page fault scenarios
 * @param request
 * @param pid
 */
void handlePageFault(struct resource request)
{
    int head = frameList.headIndex;
    char logEntry[200];
    struct resourcemsg replyMessage;

    sprintf(logEntry, "Frame %d containing address %d has been removed", head + 1, frameList.frames[head].id);
    writeToConsole(logEntry);

    //update current heading frame
    frameList.frames[head].head = false;
    updateFrame(head, request);
    sprintf(logEntry, "Frame %d has been updated with address %d at S:%d N:%d", head + 1, frameList.frames[head].id, osclock->seconds, osclock->nanoseconds);
    writeToConsole(logEntry);

    //make next frame the new head
    head++;
    if(head == FRAME_TABLE_SIZE)
    {
        head = 0;
    }
    frameList.headIndex = head;
    frameList.frames[head].head = true;
    sprintf(logEntry, "Frame %d is now the head of the frame table", head + 1);
    writeToConsole(logEntry);

    //reply to PID
    replyMessage = makeResourceMessage(request.pid, request.operation, request.address);
    msgsnd(sendMQId, &replyMessage, sizeof(struct resourcemsg), 0);

}

void processRequest(struct resource request)
{
    int memFrame = frameSearch(request);
    struct resourcemsg replyMessage;
    char logEntry[200];

    if(memFrame == -1)
    {
        sprintf(logEntry, "Address %i is not in frame.  Page Fault has occurred. Request for PID %d blocked", request.address, request.pid);
        writeToConsole(logEntry);
        addBlockedRequest(request);
        pageFaults++;
    }
    else // update normal frame or read existing frame
    {
        updateFrame(memFrame, request);
        if(!frameList.frames[memFrame].occupied) //update empty frame
        {
            sprintf(logEntry, "Frame %d updated with address %d for PID %d at S:%d N:%d",
                    memFrame + 1, request.address, request.pid, osclock->seconds, osclock->nanoseconds);
            logToFile(logEntry);
        }
        else //reference existing frame
        {
            sprintf(logEntry, "Referencing address %d in existing frame %d for PID %d",
                    request.address, memFrame + 1, request.pid);
            logToFile(logEntry);

        }
        //reply to PID
        replyMessage = makeResourceMessage(request.pid, request.operation, request.address);
        msgsnd(sendMQId, &replyMessage, sizeof(struct resourcemsg), 0);
        requestsGranted++;
    }



}


/**
 * listens for incoming messages
 */
void listenForMessages()
{
    struct resourcemsg msg;
    char* operation;
    struct resource request;

    int i = 0;
    int maxCheck = currentWorkers;
    char logEntry[200];

    do
    {
        if(msgrcv(listenerMQId, (void *)&msg, sizeof(struct resourcemsg), 0, IPC_NOWAIT) != -1)
        {
            request = getChildMessage(msg, msg.msgType);

            if(request.operation == 0)
            {
                operation = "read";
            }
            else if(request.operation == 1)
            {
                operation = "write";
            }

            switch(request.operation)
            {
                case -1:
                    waitForTerm(msg.msgType);
                    break;
                case 0:
                case 1:
                    sprintf(logEntry, "PID %d requests %s operation on address %i at S:%d N:%d", msg.msgType, operation, request.address, osclock->seconds, osclock->nanoseconds);
                    logToFile(logEntry);
                    processRequest(request);
                    incrementClock(1);
                    break;
                default:
                    //unknown.  end program
                    sprintf(logEntry, "PID %i asked for an unknown operation.  Terminating OSS", msg.msgType);
                    logToFile(logEntry);
                    break;

            }
        }
        else if(errno == ENOMSG)
        {
            incrementClock(1);
        }
        else
        {
            perror("There was a problem retrieving messages from processes");
            cleanupResources();
            exit(-1);
        }
        i++;
        incrementClock(1);
    }
    while(i < maxCheck);

    //process blocked requests
    if(blockedRequests.size > 0)
    {
        for(i = 0; i < blockedRequests.size; i++)
        {
            handlePageFault(blockedRequests.requests[i]);
        }
        blockedRequests.size = 0;
    }

}

/**
 * executes the worker process using the
 * parameters supplied by the user
 */
void executeWorkers()
{
    bool running = true;
    //get start time
    time(&startTime);
    while(running)
    {
        if(TERM_FLAG)
        {
            cleanupResources();
            break;
        }
        else
        {
            manageProcesses();
            listenForMessages();
        }

        if((currentWorkers <= 0 && !canSpawnChildren()))
        {
            running = false;
        }

    }
}

/**
 * Initializes resources needed for the application
 */
void init()
{
    int listenerMQKey = ftok("oss.c", 1);
    int replyMQKey = ftok("oss.c", 3);
    key_t sharedMemKey = ftok("oss.c", 5);

    //init 'OS' clock and shared resources
    ossMemId = shmget(sharedMemKey, sizeof(struct sysclock), 0644|IPC_CREAT);
    osclock = (struct sysclock*) shmat(ossMemId, (void*)0, 0);
    osclock->seconds = 0;
    osclock->nanoseconds = 0;

    //blocked message structure
    blockedRequests.size = 0;

    //init randomgen
    srand((unsigned) getpid());

    //init log file
    logfp = fopen(logFile, "w+");

    //register signal interrupt
    signal(SIGINT, termFlag);


    //init MQs
    listenerMQId = msgget(listenerMQKey, 0666 | IPC_CREAT);
    sendMQId = msgget(replyMQKey, 0666 | IPC_CREAT);
    if(listenerMQId == -1 || sendMQId == -1)
    {
        perror("There was a problem setting up message queues");
        cleanupResources();
        exit(-1);
    }
}

void printFinalResults()
{

    char logEntry[200];
    int totalRequests = requestsGranted + pageFaults;
    float faultPctg = 100 - (((float)requestsGranted / pageFaults) * 100);

    sprintf(logEntry,"======FINAL FRAME TABLE======");
    writeToConsole(logEntry);
    printAllocationTable();

    int memAccessPerSecond = totalRequests / MAX_SPAWN_TIME;
    sprintf(logEntry, "======FINAL RESULTS======");
    writeToConsole(logEntry);
    sprintf(logEntry, "Total Processes: %d", executedWorkers);
    writeToConsole(logEntry);
    sprintf(logEntry, "Total System run time: %d seconds", MAX_SPAWN_TIME);
    writeToConsole(logEntry);
    sprintf(logEntry, "Total Requests: %d", totalRequests);
    writeToConsole(logEntry);
    sprintf(logEntry, "Requests granted: %d", requestsGranted);
    writeToConsole(logEntry);
    sprintf(logEntry, "Memory accesses per second: %d", memAccessPerSecond);
    writeToConsole(logEntry);
    sprintf(logEntry, "Page faults: %d", pageFaults);
    writeToConsole(logEntry);
    sprintf(logEntry, "Page faults per memory access: %.2f:10", faultPctg * 0.1f);
    writeToConsole(logEntry);
}

/**MAIN ENTRY POINT**/
int main(int argCount, char *argv[])
{
    if(handleParams(argCount, argv) != -1)
    {
        char logentry[200];
        init();
        sprintf(logentry, "Initialized");
        logToFile(logentry);
        //spin up workers
        executeWorkers();
        printFinalResults();
        //cleanup program resources
        sprintf(logentry, "End of Execution");
        logToFile(logentry);
        cleanupResources();
    }
    return 0;
}
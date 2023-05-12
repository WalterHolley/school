#define MAX_CONCURRENT_WORKERS 18
#define MAX_TOTAL_WORKERS 1
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
int deadlockTerminations = 0;
int deadlocksDetected = 0;
int normalTerminations = 0;
int deadlockChecks = 0;
int requestsGranted = 0;
int listenerMQId, sendMQId, ossMemId;
const int MAX_RUN_TIME = 60;
const int NANO_INCREMENT = 10000;
int TERM_FLAG = 0;
int linesRemaining = 100000; //total number of lines that can be written to the log
struct sysclock* osclock;

struct frameTable frameList;
struct proc_pages pageTable[MAX_CONCURRENT_WORKERS];


//TODO: Define frame queue
//TODO: Define frame wait queue


char* logFile = "oss.log";
FILE *logfp;
bool verbose = false;





/**
 * Cleanup of the processes and resources
 * used by this application
 */
void cleanupResources()
{

    printf("Terminating program\n");

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
 * Prints the process table to
 * the console
 */
void printAllocationTable()
{
    int i;
    char line[100];
    //print header


}


/** Increments the simulated clock of the system **/
void incrementClock()
{
    int nanos = osclock->nanoseconds;
    nanos += NANO_INCREMENT;
    osclock->seconds = osclock->seconds + (nanos / NANOS_IN_SECOND);
    osclock->nanoseconds = nanos % NANOS_IN_SECOND;

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
        incrementClock();
        incrementClock();
    }

    return childPid;
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
            if(pageTable[i].pid == pid) //consume resources. reduce priority
            {
                emptyPage.pid = 0;
                pageTable[i] = emptyPage;
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
        incrementClock();
    }while(result != childPid);
    currentWorkers--;
    sprintf(logEntry, "PID %d has terminated at S:%d N:%d", childPid, osclock->seconds, osclock->nanoseconds);
    writeToConsole(logEntry);
    return result;
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

    if(now - startTime < 5 && totalWorkers <= MAX_TOTAL_WORKERS)
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
struct resourcemsg makeResourceMessage(int pid, int resId, int allocation)
{
    struct resourcemsg msg;
    msg.msgType = pid;

    sprintf(msg.message, "%d:%d", resId, allocation);

    return msg;
}

struct resource getChildMessage(struct resourcemsg childMsg)
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

int getPage(struct resource pageRequest){
    int page = 0;
    page = (pageRequest.address / pageRequest.base) - (pageRequest.offset / pageRequest.base);
    return page;
}

int frameSearch(struct resource request)
{
    int i = frameList.headIndex;
    int result = -1;

    do
    {
        if(!frameList.frames[i].occupied)
        {
            //update frame
            //update page

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

void processRequest(struct resource request)
{
    int memFrame = frameSearch(request);
    char logEntry[200];

    if(memFrame == -1)
    {
        sprintf(logEntry, "Address %i is not in frame.  Page Fault has occurred");
        logToFile(logEntry);
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
    int maxCheck = currentWorkers;
    int i = 0;
    char logEntry[200];

    incrementClock();
    do
    {
        if(msgrcv(listenerMQId, (void *)&msg, sizeof(struct resourcemsg), 0, IPC_NOWAIT) != -1)
        {
            request = getChildMessage(msg);

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
                    sprintf(logEntry, "PID %d requests %s operation on address %i", msg.msgType, operation, request.address);
                    logToFile(logEntry);
                    processRequest(request);
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
            incrementClock();
        }
        else
        {
            perror("There was a problem retrieving messages from processes");
            cleanupResources();
            exit(-1);
        }
        i++;
        incrementClock();
    }
    while(i < maxCheck);
    incrementClock();


}

/**
 * executes the worker process using the
 * parameters supplied by the user
 */
void executeWorkers()
{
    bool running = true;
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

        if(currentWorkers <= 0 && !canSpawnChildren())
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
    char logEntry[100];
    int i;

    //init 'OS' clock and shared resources
    ossMemId = shmget(sharedMemKey, sizeof(struct sysclock), 0644|IPC_CREAT);
    osclock = (struct sysclock*) shmat(ossMemId, (void*)0, 0);
    osclock->seconds = 0;
    osclock->nanoseconds = 0;

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


    //get start time
    time(&startTime);

}

void printFinalResults()
{
    float terminationPercentage = (float)deadlockTerminations / (float)deadlocksDetected;
    char logEntry[200];

    sprintf(logEntry, "Requests granted: %d", requestsGranted);
    writeToConsole(logEntry);
    sprintf(logEntry, "Deadlock checks run: %d", deadlockChecks);
    writeToConsole(logEntry);
    sprintf(logEntry, "Deadlocks detected: %d", deadlocksDetected);
    writeToConsole(logEntry);
    sprintf(logEntry, "Deadlock terminations: %d", deadlockTerminations);
    writeToConsole(logEntry);
    sprintf(logEntry, "Deadlock termination percentage %.2f%s", terminationPercentage * 100, "%");
    writeToConsole(logEntry);
    sprintf(logEntry, "Processes terminated normally: %d", normalTerminations);
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
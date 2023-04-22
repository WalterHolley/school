#define MAX_CONCURRENT_WORKERS 18
#define MAX_TOTAL_WORKERS 40
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



//element for process queues
struct queue_entry {
    int processId;
    TAILQ_ENTRY(queue_entry) entries;
};

//element for scheduling queue
struct squeue_entry {
    struct sysclock timeBuffer;
    TAILQ_ENTRY(squeue_entry) entries;
};

//ready and blocked queues
TAILQ_HEAD(readyhead, queue_entry) readyQueue;
TAILQ_HEAD(blockedhead, queue_entry) blockedQueue;

//scheduling queue
TAILQ_HEAD(schedulehead, squeue_entry) schedQueue;

//globals
time_t startTime;
int totalWorkers = 0;
int currentWorkers = 0;
int scheduledWorkers = 0;
int listenerMQId, sendMQId, ossMemId;
const int MAX_RUN_TIME = 60;

const int NANO_INCREMENT = 10000;
int TERM_FLAG = 0;
struct sysclock* osclock;
struct resource availableResources;
struct resource allocationTable[18];
size_t procTableSize = sizeof(processTable) / sizeof(struct process);
char* logFile = "oss.log";
FILE *logfp;

/**
 * Cleanup of the processes and resources
 * used by this application
 */
void cleanupResources()
{
    //terminate remaining children
    printf("Terminating process\n");
    int i;

    for(i = 0; i < procTableSize; i++)
    {
        if(processTable[i].occupied)
        {
            kill(processTable[i].pid, SIGTERM);
        }
    }

    fclose(logfp);

    //close Message queues
    msgctl(listenerMQId, IPC_RMID, NULL);
    msgctl(sendMQId, IPC_RMID, NULL);
    //clear shared memory
    shmctl(ossMemId, IPC_RMID, NULL);

    //clear process queues
    while(readyQueue.tqh_first != NULL)
    {
        TAILQ_REMOVE(&readyQueue, readyQueue.tqh_first, entries);
    }

    while(blockedQueue.tqh_first != NULL)
    {
        TAILQ_REMOVE(&blockedQueue, blockedQueue.tqh_first, entries);
    }

    while(schedQueue.tqh_first != NULL)
    {
        TAILQ_REMOVE(&schedQueue, schedQueue.tqh_first, entries);
    }

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
    while((options = getopt(argCount, argString, ":hf:")) != -1)
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
            case 'v': //TODO: Implement verbose logging flag
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
    char logitem[210] = "OSS:";

    if(len > 0)
    {
        strcat(logitem, entry);
        strcat(logitem, "\n");
        fprintf(logfp, logitem);
    }


}

/**
 * Prints the process table to
 * the console
 */
void printProcessTable()
{
        int i;
        char line[100];
        //print OSS message
        sprintf(line, "OSS PID: %d SysClockS: %d SysclockNano: %d", getpid(), osclock->seconds, osclock->nanoseconds);
        logToFile(line);
        printf(line);
        sprintf(line, "Process Table:");
        logToFile(line);
        printf(line);

        //print header
        sprintf(line,"%-10s%-10s%-10s%-10s%s", "Entry", "Occupied", "PID", "StartS", "StartN");
        logToFile(line);
        printf(line);

        //print rows
        for(i = 0; i < procTableSize; i++)
        {
            sprintf(line, "%-10i%-10i%-10i%-10i%i", i, processTable[i].occupied, processTable[i].pid, processTable[i].startSeconds, processTable[i].startNano);
            logToFile(line);
            printf(line);
        }

}


/** Increments the simulated clock of the system **/
void incrementClock()
{
    int nanos = osclock->nanoseconds;
    nanos += NANO_INCREMENT;
    osclock->seconds = osclock->seconds + (nanos / NANOS_IN_SECOND);
    osclock->nanoseconds = nanos % NANOS_IN_SECOND;

}

/** Adds a process record to the process table
 * sends initial MQ message to child process
 * **/
int addProcess(pid_t childPid)
{
    struct process newProc;
    struct queue_entry *procEntry;
    int i, result = 0;

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
        char* args[] = {"./worker", NULL};
        execvp(args[0], args);
    }
    else //parent. add child to ready queue.
    {
        currentWorkers++;
        totalWorkers++;
        incrementClock();
        incrementClock();
    }

    return childPid;
}




/** Loops until a child process is terminated.
 * Increments OS clock
 * @return -1 for error
 * process Id of child on success
 */
int waitForTerm(int childPid)
{
    int result = 0;
    int pidStatus;
    do
    {
        result = waitpid(childPid, &pidStatus, WNOHANG);
        incrementClock();
    }while(result != childPid);
    currentWorkers--;
    return result;
}

/**
 * Update the resource allocation table
 * @param pid
 * @param resId
 * @param allocation
 */
void updateResourceAllocation(int pid, int resId, int allocation)
{
    int i;

    for(i = 0; i < len(allocationTable); i++)
    {
        if(allocationTable[i] != pid)
        {
            continue;
        }
        else
        {
            allocationTable[i].res[resId - 1] += allocation;
            availableResources.res[resId - 1] -= allocation;
            break;
        }
    }

}

/**
 * Claim a resource for use
 * @param resId
 * @param pid
 * @return
 */
bool claimResource(int resId, int pid)
{
    bool result = false;
    if(availableResources.res[resId - 1] == 0)
    {
        //log resource not available
    }
    else
    {

        updateResourceAllocation(pid, resId, 1);
        result = true;
    }

    return result;
}

/**
 * Release a resource from a process
 * @param resId
 * @param pid
 */
void releaseResource(int resId, int pid)
{
    updateResourceAllocation(pid, resId, -1);
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
    char msgText[15];

    sprintf(msgText, "%d:%d", resId, allocation);
    msg.message = msgText;

    return msg;
}

int[2] getResourceMsg(struct resourcemsg childMsg)
{
    int result[2];

    char* token = strtok(childMsg.message, ":");
    result[0] = atoi(token);
    token = strtok(NULL, ":");
    result[1] = atoi(token);

    return result;
}

/**
 * executes the worker process using the
 * parameters supplied by the user
 */
void executeWorkers()
{
    int spawnChance = 0;
    bool running = true;
    struct resourcemsg msg;
    struct resourcemsg replyMsg;
    int messageVal[2];

    while(running)
    {
        if(msgrcv(listenerMQId, (void *)&msg, sizeof(struct resourcemsg), 0, IPC_NOWAIT) != -1)
        {
            messageVal = getResourceMsg(msg);
            if(messageVal[0] == -1) // end process
            {
                waitForTerm(msg.msgType);
            }
            else if(messageVal[1] > 0) //claim resource
            {
                replyMsg.msgType = msg.msgType;
                if(!claimResource(messageVal[0], msg.msgType)) // reject claim
                {
                    sprintf(replyMsg.message, "%d:%d", messageVal[0], -1);
                }
                else // approve claim
                {
                    replyMsg.message = msg.message;
                }
                msgsnd(replyMQId, &replyMsg, sizeof(struct resourcemsg), 0);
            }
            else //release resource
            {
                releaseResource(messageVal[0], msg.msgType);
                msgsnd(replyMQId, &msg, sizeof(struct resourcemsg), 0);
            }

        }
        else if(errno == ENOMSG)
        {
            //log no messages
        }
        else
        {
            perror("There was a problem retrieving messages from processes");
            cleanupResources();
            exit(-1);
        }

        if(canSpawnChildren())  //chance to spawn child process
        {

            spawnChance =  rand() % 100;

            //check for max running processes
            if((spawnChance >= SPAWN_THRESHOLD) && (totalWorkers < MAX_TOTAL_WORKERS))
            {
                //child creation
                createChild();
            }
        }

        //manage active processes
        incrementClock();
        if(currentWorkers == 0)
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

    //init available resource
    availableResources.pid = getpid();
    for(int i = 0; i < 10; i ++)
    {
        availableResources.res[i] = MAX_RES_COUNT;
    }

    //get start time
    time(&startTime);

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

        //cleanup program resources
        sprintf(logentry, "End of Execution");
        logToFile(logentry);
        cleanupResources();
    }
    return 0;
}
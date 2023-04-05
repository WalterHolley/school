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
#include "osclock.h"


//process structure
struct process {
    int occupied;   //indicates if record space is occupied
    pid_t pid;
    int startSeconds;
    int startNano;
    int mqId;
};



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
time_t t;
int totalWorkers = 0;
int currentWorkers = 0;
int scheduledWorkers = 0;
int listenerMQId, sendMQId, ossMemId;
const int MAX_RUN_TIME = 60;

const int NANO_INCREMENT = 10000;
int TERM_FLAG = 0;
struct sysclock* osclock;
struct sysclock nextPrint;
struct process processTable[18];
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

/** Creates clockmsg object from a sysclock object**/
struct clockmsg buildClockMessage(struct sysclock clock, int childPid)
{
    struct clockmsg message;
    message.msgType = childPid;
    sprintf(message.message, "%d", clock.nanoseconds);


    return message;
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

    newProc.occupied = 1;
    newProc.pid = childPid;
    newProc.startNano = osclock->nanoseconds;
    newProc.startSeconds = osclock->seconds;

    for(i = 0; i < procTableSize; i++)
    {
        if(!processTable[i].occupied)
        {
            processTable[i] = newProc;
            procEntry = malloc(sizeof(struct queue_entry));
            procEntry->processId = childPid;
            result = 1;
            incrementClock();
            break;
        }
        incrementClock();
    }
    return result;
}

/**
 * Forks and creates a child process
 * @return PID of child process
 */
int createChild()
{
    pid_t childPid; // process ID of a child executable
    struct queue_entry* item;

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
        item = malloc(sizeof(struct queue_entry));
        item->processId = childPid;
        TAILQ_INSERT_TAIL(&readyQueue, item, entries);
        incrementClock();
        incrementClock();
    }

    return childPid;
}

/**
 * schedules an execution time
 * for a child process
 */
void addChildToScheduler()
{
    struct squeue_entry* item;
    int seconds = (rand() % (2 - 0 + 1));
    int nanos = (rand() % (NANO_INCREMENT - 0 + 1));
    item = malloc(sizeof (struct squeue_entry));
    char logEntry[200];

    //if schedQueue isn't NULL, use tail entry to calculate start time
    if(schedQueue.tqh_first != NULL)
    {
        struct squeue_entry* lastItem;
        lastItem = TAILQ_LAST(&schedQueue, schedulehead);
        nanos += lastItem->timeBuffer.nanoseconds;
        seconds += lastItem->timeBuffer.seconds;
    }
    else // use os clock
    {
        nanos += osclock->nanoseconds;
        seconds += osclock->seconds;
    }

    nanos = nanos % NANO_INCREMENT;
    seconds += (nanos / NANOS_IN_SECOND);

    item->timeBuffer.nanoseconds = nanos;
    item->timeBuffer.seconds = seconds;

    TAILQ_INSERT_TAIL(&schedQueue, item, entries);
    scheduledWorkers++;
    sprintf(logEntry, "New Process scheduled for S:%d N:%d", seconds, nanos);
    logToFile(logEntry);

}

/** Removes the given pid from the process table**/
int removeProcess(pid_t processId)
{
    int i, result = 0;

    for(i = 0; i < procTableSize; i++)
    {
        if(processTable[i].pid == processId)
        {
            processTable[i].occupied = 0;
            processTable[i].startSeconds = 0;
            processTable[i].startNano = 0;
            processTable[i].pid = 0;
            result = 1;
            break;
        }
    }

    return result;
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
 * Gets the replies back for each existing process.
 */
void handleReplies(int replies)
{
    int i = 0;
    struct clockmsg msg;
    struct queue_entry* newEntry;
    char logEntry[200];
    while(i < replies)
    {
       if(msgrcv(listenerMQId, (void *)&msg, sizeof(struct clockmsg), 0, IPC_NOWAIT) != -1)
       {
           sprintf(logEntry, "Received message from PID %d at %d:%d", msg.msgType, osclock->seconds, osclock->nanoseconds);
           logToFile(logEntry);
           int timeSpent = atoi(msg.message);
           if(timeSpent < NANO_INCREMENT && timeSpent > 0)  //process blocked.  add to blocked queue
           {
               newEntry = malloc(sizeof(struct queue_entry));
               newEntry->processId = msg.msgType;
               TAILQ_INSERT_TAIL(&blockedQueue, newEntry, entries);
               sprintf(logEntry, "PID %d waiting for I/O. Moved to blocked queue", msg.msgType, osclock->seconds, osclock->nanoseconds);
               logToFile(logEntry);
           }
           else if(timeSpent < 0) // process completed. terminate
           {
               waitForTerm(msg.msgType);
               removeProcess(msg.msgType);
               sprintf(logEntry, "PID %d has terminated", msg.msgType);
               logToFile(logEntry);
           }
           else //full time used. add to ready queue
           {
               newEntry = malloc(sizeof(struct queue_entry));
               newEntry->processId = msg.msgType;
               TAILQ_INSERT_TAIL(&readyQueue, newEntry, entries);
               sprintf(logEntry, "PID %d request more time.  Moved to ready queue", msg.msgType);
               logToFile(logEntry);
           }
           i++;
           incrementClock();

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


    }
}


/**
 * Manage the scheduling of processes
 * @returns number of items scheduled
 */
int dispatchProcesses()
{
    struct queue_entry* item;
    struct queue_entry* blockedItem;
    struct squeue_entry* scheduleItem;
    struct clockmsg msg;
    struct sysclock time_allotment;
    int childPid;
    int dispatched = 0;
    char logEntry[200];

    time_allotment.seconds = 0;
    time_allotment.nanoseconds = NANO_INCREMENT;

    //check schedule queue for waiting processes
    while(schedQueue.tqh_first != NULL)
    {
        scheduleItem = schedQueue.tqh_first;
        if(osclock->seconds == scheduleItem->timeBuffer.seconds)
        {
            if((osclock->nanoseconds >= scheduleItem->timeBuffer.nanoseconds) && currentWorkers < MAX_CONCURRENT_WORKERS)
            {
                //launch process
                childPid = createChild();
                addProcess(childPid);
                sprintf(logEntry, "PID %d scheduled for %d:%d launched at %d:%d", childPid,
                        scheduleItem->timeBuffer.seconds, scheduleItem->timeBuffer.nanoseconds, osclock->seconds, osclock->nanoseconds);
                logToFile(logEntry);
                TAILQ_REMOVE(&schedQueue, schedQueue.tqh_first, entries);
                scheduledWorkers--;
                continue;
            }
        }
        else if((osclock->seconds > scheduleItem->timeBuffer.seconds) && currentWorkers < MAX_CONCURRENT_WORKERS)
        {
            //launch process
            childPid = createChild();
            addProcess(childPid);
            sprintf(logEntry, "PID %d scheduled for %d:%d launched at %d:%d", childPid,
                    scheduleItem->timeBuffer.seconds, scheduleItem->timeBuffer.nanoseconds, osclock->seconds, osclock->nanoseconds);
            logToFile(logEntry);
            TAILQ_REMOVE(&schedQueue, schedQueue.tqh_first, entries);
            scheduledWorkers--;
            continue;
        }
        break;
    }

    //process ready queue
    while(readyQueue.tqh_first != NULL)
    {
        item = readyQueue.tqh_first;
        msg = buildClockMessage(time_allotment, item->processId);
        msgsnd(sendMQId, &msg, sizeof(msg), 0);
        sprintf(logEntry,"sending to processId %d with time allotment 0:%s", item->processId, msg.message);
        logToFile(logEntry);
        TAILQ_REMOVE(&readyQueue, readyQueue.tqh_first, entries);
        dispatched++;
        incrementClock();
    }


    //move blocked processes to ready queue
    while(blockedQueue.tqh_first != NULL)
    {
        blockedItem = blockedQueue.tqh_first;
        item = malloc(sizeof(struct queue_entry));
        item->processId = blockedItem->processId;
        TAILQ_INSERT_TAIL(&readyQueue, item, entries);
        TAILQ_REMOVE(&blockedQueue, blockedQueue.tqh_first, entries);
        sprintf(logEntry, "PID %d moved from blocked queue to ready queue", item->processId);
        logToFile(logEntry);
        incrementClock();
    }
    incrementClock();
    return dispatched;
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

    if(now - t < 3 && totalWorkers <= MAX_TOTAL_WORKERS)
    {
        result = true;
    }

    return  result;
}

/**
 * executes the worker process using the
 * parameters supplied by the user
 */
void executeWorkers()
{
    int spawnChance = 0;

    while(canSpawnChildren())
    {
        if(TERM_FLAG)
        {
            //kill child processes and clear resources
            cleanupResources();
            break;
        }
        else
        {
            //chance to spawn child process
            srand(time(0));
            spawnChance =  rand() % 100;

            //check for max running processes
            if((spawnChance >= SPAWN_THRESHOLD) && (totalWorkers < MAX_TOTAL_WORKERS))
            {
                //schedule child creation
                addChildToScheduler();
                totalWorkers++;
            }
        }

        //perform dispatch tasks
        int dispatched = dispatchProcesses();
        incrementClock();
        //handle replies from the child processes
        handleReplies(dispatched);

    }
    //spawning children done.  manage scheduling until end of all programs

    do
    {
        if(!TERM_FLAG)
        {
            int dispatched = dispatchProcesses();
            incrementClock();
            handleReplies(dispatched);
            incrementClock();
        }
        else
        {
            cleanupResources();
            exit(-1);
        }

    }while((currentWorkers > 0) || (scheduledWorkers > 0));

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
    srand((unsigned) time(NULL));

    //init log file
    logfp = fopen(logFile, "w+");

    //register signal interrupt
    signal(SIGINT, termFlag);

    //init queue structures
    TAILQ_INIT(&readyQueue);
    TAILQ_INIT(&blockedQueue);
    TAILQ_INIT(&schedQueue);

    //init MQs
    listenerMQId = msgget(listenerMQKey, 0666 | IPC_CREAT);
    sendMQId = msgget(replyMQKey, 0666 | IPC_CREAT);
    if(listenerMQId == -1 || sendMQId == -1)
    {
        perror("There was a problem setting up message queues");
        cleanupResources();
        exit(-1);
    }


    //init processTable clock
    nextPrint.nanoseconds = NANOS_HALF_SECOND;
    nextPrint.seconds = 0;

    //capture start time
    time(&t);

}

/**MAIN ENTRY POINT**/
int main(int argCount, char *argv[])
{
    if(handleParams(argCount, argv) != -1)
    {
        char logentry[200];
        sprintf(logentry, "Initialized");
        logToFile(logentry);
        init();
        //spin up workers
        executeWorkers();

        //cleanup program resources
        sprintf(logentry, "End of Execution");
        logToFile(logentry);
        cleanupResources();
    }
    return 0;
}
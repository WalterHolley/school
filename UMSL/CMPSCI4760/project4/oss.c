#define MAX_CONCURRENT_WORKERS 18
#define MAX_SPAWN_TIME 3

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
    TAILQ_ENTRY(struct queue_entry) entries;
};

//globals
time_t t;
int totalWorkers;
int timelimit;
int ossMemId, replyMQId, sendMQId;
const int MAX_RUN_TIME = 60;

int nanoIncrement = 100000;
int TERM_FLAG = 0;
struct ossProperties* properties;
struct sysclock nextPrint;
struct process processTable[18];
size_t procTableSize = sizeof(processTable) / sizeof(struct process);
char* logFile = "oss.log";
FILE *logfp;

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
struct clockmsg buildClockMessage(struct sysclock clock)
{
    struct clockmsg message;
    message.msgType = 1;
    sprintf(message.message, "%d,%d", clock.seconds, clock.nanoseconds);


    return message;
}

void logToFile(char* entry)
{
    int len = strlen(entry);
    if(len >= 2)
    {
        if(entry[len - 2] != "\\" || entry[len - 1] != "n")
        {
            strcat(entry, "\n");
        }
        fprintf(logfp, entry);
    }
    else if(len > 0)
    {
        strcat(entry, "\n");
        fprintf(logfp, entry);
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
        sprintf(line, "OSS PID: %d SysClockS: %d SysclockNano: %d", getpid(), osclock.seconds, osclock.nanoseconds);
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
    int nanos = osclock.nanoseconds;
    nanos += nanoIncrement;
    osclock.seconds = osclock.seconds + (nanos / NANOS_IN_SECOND);
    osclock.nanoseconds = nanos % NANOS_IN_SECOND;
    char* message = "Execution time has expired";


    //notify child processes
    notifyChildren();
    //Max run time reached. application must terminate
    if(osclock.seconds >= MAX_RUN_TIME)
    {

        logToFile(message);
        printf(message);
        termFlag();


    }

    //print process table and increment time for next printing
    if(osclock.nanoseconds >= nextPrint.nanoseconds && osclock.seconds >= nextPrint.seconds)
    {
        printProcessTable();
        nextPrint.nanoseconds += NANOS_HALF_SECOND;
        if(nextPrint.nanoseconds == NANOS_IN_SECOND)
        {
            nextPrint.seconds++;
            nextPrint.nanoseconds = 0;
        }
    }
}

/** Adds a process record to the process table
 * sends initial MQ message to child process
 * **/
int addProcess(pid_t childPid)
{
    struct process newProc;
    int i, result = 0;
    char seconds[3];
    char nanos[10];
    struct sysclock runningTime;
    struct clockmsg mqMsg;

    sprintf(seconds, "%i",rand() % timelimit);

    if(atoi(seconds) == timelimit)
    {
        nanos[0] = '0';
    }
    else
    {
        sprintf(nanos, "%i",(rand() % (NANOS_IN_SECOND / nanoIncrement)) * nanoIncrement);
    }



    runningTime.nanoseconds = atoi(nanos);
    runningTime.seconds = atoi(seconds);

    newProc.mqId = msgget(childPid, 0666 | IPC_CREAT);
    newProc.occupied = 1;
    newProc.pid = childPid;
    newProc.startNano = osclock.nanoseconds;
    newProc.startSeconds = osclock.seconds;
    mqMsg = buildClockMessage(runningTime);

    //send running time and current system time
    msgsnd(newProc.mqId, &mqMsg, sizeof(mqMsg), 0);
    mqMsg = buildClockMessage(osclock);
    msgsnd(newProc.mqId, &mqMsg, sizeof(mqMsg), 0);

    for(i = 0; i < procTableSize; i++)
    {
        if(!processTable[i].occupied)
        {
            processTable[i] = newProc;
            result = 1;
            break;
        }
    }
    return result;
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

/**
 * Cleanup of the processes and resources
 * used by this application
 */
void cleanup()
{
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

}

/** Loops until a child process is terminated.
 * Increments OS clock
 * @return -1 for error
 * process Id of child on success
 */
int waitForTerm()
{
    int result = 0;
    int pidStatus;
    do
    {
        result = waitpid(-1, &pidStatus, WNOHANG);
        incrementClock();
    }while(result == 0);

    return result;
}

/**
 * Determines if child processes can be spawned
 * @return true if child processes can be spawned.
 * otherwise false
 */
bool spawnChildren()
{
    bool result = false;
    time_t now;
    time(&now);

    if(now - t <= 3 || totalWorkers < 100)
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

    pid_t childPid; // process ID of a child executable


    while(spawnChildren())
    {
        if(TERM_FLAG)
        {
            //kill child processes
            cleanup();
            break;
        }
        else
        {
            //TODO: -> Make random decision to spawn new worker
            bool spawnChild = true;

            if(spawnChild)
            {
                //fork new process
                childPid = fork();
                if (childPid == -1) // error
                {
                    logToFile("An error occurred during fork");
                    perror("An error occurred during fork");
                    cleanup();
                    exit(1);
                }
                else if (childPid == 0) //this is the child node.  run program
                {
                    char* args[] = {"./worker", NULL};
                    execvp(args[0], args);
                }
                else //parent. add child to ready queue. increment clock
                {
                    //check for max running processes
                    //update ready queue. increment clock

                    //increment time for creating process
                }

            }

        }

        //manage ready queue. incrememt clock

        //manage blocked queue. incrememt clock

        //check for expired spawn time

    }

    //manage ready and blocked queues until end of execution time

}

void init()
{
    //register signal interrupt
    signal(SIGINT, termFlag);

    //init randomgen
    srand((unsigned) time(&t));

    //init log file
    logfp = fopen(logFile, "w+");

    //init 'OS' clock and shared resources
    ossMemId = shmget(SMEM_KEY, sizeof(struct ossProperties), 0666|IPC_CREAT);
    properties = (struct ossProperties*)shmat(ossMemId, (void*)0, 0);
    properties->osClock.seconds = 0;
    properties->osClock.nanoseconds = 0;
    properties->listenerQueue = ftok("oss.c", 1);
    properties->replyQueue = ftok("oss.c", 3);

    //init MQs
    replyMQId = msgget(properties->listenerQueue, 0666 | IPC_CREAT);
    sendMQId = msgget(properties->replyQueue, 0666 | IPC_CREAT);

    //init processTable clock
    nextPrint.nanoseconds = NANOS_HALF_SECOND;
    nextPrint.seconds = 0;

}

int main(int argCount, char *argv[])
{
    if(handleParams(argCount, argv) != -1)
    {
        init();

        //spin up workers
        executeWorkers();

        //end logging
        fclose(logfp);
    }
    return 0;
}
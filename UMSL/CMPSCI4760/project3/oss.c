#define MAX_CONCURRENT_WORKERS 18

#include <stdio.h>
#include <stdbool.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <signal.h>
#include "osclock.h"


//process structure
struct process {
    int occupied;   //indicates if record space is occupied
    pid_t pid;
    int startSeconds;
    int startNano;
    int mqId;
};


//globals
int totalWorkers;
int timelimit;
int maxSimultaneous;
const int MAX_RUN_TIME = 60;

int nanoIncrement = 100000;
int TERM_FLAG = 0;
struct sysclock osclock;
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

int validateParam(char* param)
{
    int i = atoi(param);
    if( i < 0)
    {
        i = -1;
    }

    return i;
}
/** Handles incoming parameters **/
int handleParams(int argCount, char *argString[])
{
    int result = 0;
    int options;
    while((options = getopt(argCount, argString, ":hn:s:t:f:")) != -1)
    {
        switch(options)
        {
            case 'h':
                printHelp();
                result = -1;
                break;
            case 'n':
                totalWorkers = validateParam(optarg);
                totalWorkers == -1 ? result = -1 :false;
                break;
            case 's':
                maxSimultaneous = atoi(optarg);
                maxSimultaneous == -1 ? result = -1 :false;
                maxSimultaneous == 0 ? maxSimultaneous = MAX_CONCURRENT_WORKERS :false;
                break;
            case 't':
                timelimit = atoi(optarg);
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

/** checks to see if at least one process is running **/
int isWorkerRunning()
{
    int i, result = 0;

    for(i; i < procTableSize; i++)
    {
        if(processTable[i].occupied)
        {
            result = 1;
            break;
        }
    }
    return result;
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

void printProcessTable()
{
        int i;
        char line[100];
        //print OSS message
        sprintf(line, "OSS PID: %d SysClockS: %d SysclockNano: %d\n", getpid(), osclock.seconds, osclock.nanoseconds);
        logToFile(line);
        printf(line);
        sprintf(line, "Process Table:\n");
        logToFile(line);
        printf(line);

        //print header
        sprintf(line,"%-10s%-10s%-10s%-10s%s\n", "Entry", "Occupied", "PID", "StartS", "StartN");
        logToFile(line);
        printf(line);

        //print rows
        for(i = 0; i < procTableSize; i++)
        {
            sprintf(line, "%-10i%-10i%-10i%-10i%i\n", i, processTable[i].occupied, processTable[i].pid, processTable[i].startSeconds, processTable[i].startNano);
            logToFile(line);
            printf(line);
        }

}

/**
 * Updates each active child process
 * with the current OS time
 */
void notifyChildren()
{
    int i;
    struct clockmsg clockMsg = buildClockMessage(osclock);
    for(i = 0; i < procTableSize; i++)
    {
        if(processTable[i].occupied)
        {
            msgsnd(processTable[i].mqId, &clockMsg, sizeof(clockMsg), 0);
        }
    }
}

/** Increments the simulated clock of the system **/
void incrementClock()
{
    int nanos = osclock.nanoseconds;
    nanos += nanoIncrement;
    osclock.seconds = osclock.seconds + (nanos / NANOS_IN_SECOND);
    osclock.nanoseconds = nanos % NANOS_IN_SECOND;

    //printf("Time: %d : %d\n", osclock.seconds, osclock.nanoseconds);

    //notify child processes
    notifyChildren();
    //Max run time reached. application must terminate
    if(osclock.seconds >= MAX_RUN_TIME)
    {
        termFlag();
        printf("Execution time has expired\n");
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

/** terminates all child processes**/
void killChildren()
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
 * executes the worker process using the
 * parameters supplied by the user
 */
void executeWorkers()
{

    pid_t childPid; // process ID of a child executable
    bool runLimit = maxSimultaneous > 0 ? true : false; //indicates a limit exists for simultaneous executions
    int pid;
    int workersStarted = 0;
    int workersRunning = 0;

    //init clock
    osclock.seconds = 0;
    osclock.nanoseconds = 0;

    //init processTable clock
    nextPrint.nanoseconds = NANOS_HALF_SECOND;
    nextPrint.seconds = 0;

    while(workersStarted != totalWorkers)
    {
        if(TERM_FLAG)
        {
            //kill child processes
            killChildren();
            break;
        }
        else
        {

            //fork new process
            childPid = fork();
            if (childPid == -1) // error
            {
                logToFile("An error occurred during fork");
                perror("An error occurred during fork");
                killChildren();
                exit(1);
            }
            else if (childPid == 0) //this is the child node.  run program
            {
                char* args[] = {"./worker", NULL};
                execvp(args[0], args);
            }
            else //parent. handle clock and execution
            {
                workersStarted++;
                workersRunning++;

                //update process table
                if(addProcess(childPid))
                {
                    //if max simultaneous reached, wait for a process to end
                    if ((runLimit && workersRunning >= maxSimultaneous) || workersRunning >= MAX_CONCURRENT_WORKERS)
                    {
                        do
                        {
                            pid = waitForTerm();
                            if(pid != -1)
                            {
                                workersRunning--;
                                removeProcess(pid);
                            }
                            else
                            {
                                //error.  kill children and end oss
                                killChildren();
                                exit(1);
                            }

                        }while(workersRunning >= maxSimultaneous);
                    }
                    incrementClock();
                }
                else
                {
                    //destroy children and end app
                    logToFile("An error occurred while updating the process table\n");
                    printf("An error occurred while updating the process table\n");
                    killChildren();
                    break;

                }
            }
        }

    }

        //wait for remaining workers to finish
        do
        {
            pid = waitForTerm();
            removeProcess(pid);
        }
        while (isWorkerRunning());
}

int main(int argCount, char *argv[])
{
    if(handleParams(argCount, argv) != -1)
    {
        //register signal interrupt
        signal(SIGINT, termFlag);

        //init randomgen
        time_t t;
        srand((unsigned) time(&t));

        //init log file
        logfp = fopen(logFile, "w+");
        //spin up workers
        executeWorkers();

        //end logging
        fclose(logfp);
    }
    return 0;
}
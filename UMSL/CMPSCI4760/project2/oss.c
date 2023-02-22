#define MAX_CONCURRENT_WORKERS 18

#include <stdio.h>
#include <stdbool.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/shm.h>
#include <sys/ipc.h>
#include <signal.h>
#include "osclock.h"


#define SMEM_KEY 0x3357

//process structure
struct process {
    int occupied;   //indicates if record space is occupied
    pid_t pid;
    int startSeconds;
    int startNano;
};


//globals
int totalWorkers;
int timelimit;
int maxSimultaneous;

int nanoIncrement = 1000;
int TERM_FLAG = 0;
struct sysclock* osclock;
struct process processTable[18];
size_t procTableSize = sizeof(processTable) / sizeof(struct process);

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
    if( i < 0 || i > 18)
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
    while((options = getopt(argCount, argString, ":hn:s:t:")) != -1)
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
                break;
            case 't':
                timelimit = atoi(optarg);
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

void printProcessTable()
{
        int i;
        //print OSS message
        printf("OSS PID: %i SysClockS: %i SysclockNano: %i\n", getppid(), osclock->seconds, osclock->nanoseconds);
        printf("Process Table:\n");

        //print header
        printf("%-10s%-10s%-10s%-10s%s\n", "Entry", "Occupied", "PID", "StartS", "StartN");

        //print rows
        for(i = 0; i < procTableSize; i++)
        {
            printf("%-10i%-10i%-10i%-10i%i\n", i, processTable[i].occupied, processTable[i].pid, processTable[i].startSeconds, processTable[i].startNano);
        }

}

/** Increments the simulated clock of the system **/
void incrementClock()
{
    int nanos = osclock->nanoseconds;
    nanos += nanoIncrement;
    osclock->seconds = osclock->seconds + (nanos / NANOS_IN_SECOND);
    osclock->nanoseconds = nanos % NANOS_IN_SECOND;

    if(osclock->nanoseconds % NANOS_HALF_SECOND == 0)
    {
        printProcessTable();
    }


}
/** Adds a process record to the process table**/
int addProcess(pid_t childPid)
{
    struct process newProc;
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
            printf("Process Removed\n");
            break;
        }
    }

    return result;
}

void termChild()
{

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
    char* args[] = {"./worker","3", "450000", NULL};
    pid_t childPid; // process ID of a child executable
    struct shmid_ds* smemStats;
    bool runLimit = maxSimultaneous > 0 ? true : false; //indicates a limit exists for simultaneous executions
    int status, sharedMemId, pid;
    int workersStarted = 0;
    int workersExecuted = 0;
    int workersRunning = 0;

    //setup shared memory
    sharedMemId = shmget(SMEM_KEY, sizeof(struct sysclock), 0644|IPC_CREAT);
    osclock = (struct sysclock*)shmat(sharedMemId, (void *)0, 0);
    osclock->seconds = 0;
    osclock->nanoseconds = 0;



    if(sharedMemId != -1)
    {
        while(workersExecuted != totalWorkers)
        {
            if(TERM_FLAG)
            {
                //kill child processes
            }
            childPid = fork();
            if (childPid == -1) // error
            {
                perror("An error occurred during fork");
                exit(1);
            }
            else if (childPid == 0) //this is the child node.  run program
            {
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
                        if (!WIFEXITED(status))
                        {
                            do
                            {
                               pid = waitpid(-1, &status, WNOHANG);
                                incrementClock();
                            }
                            while (!WIFEXITED(status));
                            workersRunning--;
                            workersExecuted++;
                            removeProcess(pid);
                        }
                    }
                    else if (workersStarted == totalWorkers) //all workers started. wait for execution to complete
                    {
                        pid_t pid;
                        do
                        {

                            pid = waitForTerm();
                            removeProcess(pid);
                            workersExecuted++;
                        }
                        while (workersExecuted != totalWorkers);
                    }
                    else
                    {
                        incrementClock();
                    }
                }
                else
                {
                    printf("An error occurred while updating the process table\n");
                    //destroy children and end app
                }
            }
        }


        //wait for shared memory to detach
        do
        {
            if(shmctl(sharedMemId, IPC_STAT, smemStats) < 1)
            {
                break;
            }
            incrementClock();
            //TODO:  attach not reporting correct value.  resolve
        }while(smemStats->shm_nattch > 1);


        if(shmctl(sharedMemId, IPC_RMID,NULL) == -1)
        {
            perror("Parent could not destroy shared memory.");
            exit(1);
        }
        else
        {
            printf("Shared memory destroyed\n");
        }
    }
    else
    {
        perror("A problem occurred while setting up shared memory");
    }
}

int main(int argCount, char *argv[])
{
    if(handleParams(argCount, argv) != -1)
    {
        //register signal interrupt
        signal(SIGINT, termFlag);
        //spin up workers
        executeWorkers();
    }
    return 0;
}
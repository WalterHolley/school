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
#include "osclock.h"


#define SMEM_KEY 0x3357

//globals
int totalWorkers;
int timelimit;
int maxSimultaneous;
struct sysclock* osclock;


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

/*
 * executes the worker process using the
 * parameters supplied by the user
 */
void executeWorkers()
{
    char* args[] = {"./worker","3", "450000", NULL};
    pid_t childPid; // process ID of a child executable
    bool runLimit = maxSimultaneous > 0 ? true : false; //indicates a limit exists for simultaneous executions
    int status, sharedMemId;
    int workersStarted = 0;
    int workersExecuted = 0;
    int workersRunning = 0;

    //setup shared memory
    sharedMemId = shmget(SMEM_KEY, sizeof(struct sysclock), 0644|IPC_CREAT);
    osclock = (struct sysclock*)shmat(sharedMemId, (void *)0, 0);
    osclock->seconds = 0;
    osclock->nanoseconds = 0;
    //shmdt(osclock);


    if(sharedMemId != -1)
    {
        while(workersExecuted != totalWorkers)
        {
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
            else //parent. manage child status
            {
                workersExecuted++;
                //osclock = (struct sysclock*)shmat(sharedMemId, (void*)0, 0);
                osclock->seconds = osclock->seconds + 1;
                osclock->nanoseconds = osclock->nanoseconds + 4500;
                /*if(workersExecuted == totalWorkers)
                {
                    shmdt(osclock);
                }*/

            }
        }

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
        //spin up workers
        executeWorkers();
    }
    return 0;
}
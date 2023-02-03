#define MAX_CONCURRENT_WORKERS 19

#include <stdio.h>
#include <stdbool.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>

//globals
int totalWorkers;
char* maxIterations;
int maxSimultaneous;

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
                printf("help option \n");
                result = -1;
                break;
            case 'n':
                totalWorkers = atoi(optarg);
                break;
            case 's':
                maxSimultaneous = atoi(optarg);
                break;
            case 't':
                maxIterations = optarg;
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
            printf("Ending program\n");
            break;
        }
    }

    return result;
}


void executeWorkers()
{
    char* args[] = {"./worker",maxIterations,NULL};
    pid_t childPid;
    bool runLimit = maxSimultaneous > 0 ? true : false;
    int status;
    int workersStarted = 0;
    int workersExecuted = 0;
    int workersRunning = 0;

    while(workersExecuted != totalWorkers) {
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
            workersStarted++;
            workersRunning++;

            if (runLimit && workersRunning >= maxSimultaneous)
            {
                if (!WIFEXITED(status))
                {
                    do
                    {
                        wait(&status);
                    }
                    while (!WIFEXITED(status));
                    workersRunning--;
                    workersExecuted++;
                }
            }

            if (workersStarted == totalWorkers)
            {
                do
                {
                    wait(&status);
                    if (WIFEXITED(status))
                    {
                        workersExecuted++;
                    }
                }
                while (workersExecuted != totalWorkers);
            }
        }
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
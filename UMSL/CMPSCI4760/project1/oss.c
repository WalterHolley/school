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

            //if max simultaneous reached, wait for a process to end
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

            //all workers started. wait for execution to complete
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
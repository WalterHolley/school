
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

void printWorkerInfo(int iteration, int pid, int ppid)
{
    printf("WORKER PID:%s PPID:%s Iteration:%n ");
}

int main(int argc, char* argv[])
{
    int iterations;

    if(argc > 1)
    {
        iterations = atoi(argv[1]);
        int pid = getpid();
        int ppid = getppid();
        int i;
        for(i = 0; i < iterations; i++)
        {
            printWorkerInfo(i + 1, pid, ppid);
            printf("before sleeping\n");
            sleep(1);
            printWorkerInfo(i + 1, pid, ppid);
            printf("after sleeping\n");

        }
    }
    else
    {
        printf("No argument for worker\n");
    }
    return 0;
}

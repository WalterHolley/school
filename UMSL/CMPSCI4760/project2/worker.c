
#include <stdio.h>

void printWorkerInfo(int iteration, int pid, int ppid)
{
    printf("WORKER PID:%i PPID:%i Iteration:%i ", pid, ppid, iteration);
}


int main(int argc, char* argv[])
{
    int seconds;
    int nanoseconds;

    if(argc > 2)
    {
        seconds = atoi(argv[1]);
        nanoseconds = atoi(argv[2]);
        int pid = getpid();
        int ppid = getppid();
        int i;

    }
    else
    {
        printf("No argument for worker\n");
    }
    return 0;
}



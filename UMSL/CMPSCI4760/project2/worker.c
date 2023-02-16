
#include <stdio.h>
#include <sys/shm.h>
#include <sys/ipc.h>
#include "osclock.h"

#define SMEM_KEY 0x3357

int smemId;
struct sysclock* osClock;


void printWorkerInfo(int pid, int ppid, int termSeconds, int termNano)
{
    printf("WORKER PID:%i PPID:%i SysClockS: %i SysClockNano: %i TermTimeS: %i TermTimeNano: %i\n", pid, ppid, osClock->seconds, osClock->nanoseconds, termSeconds, termNano);
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

        smemId = shmget(SMEM_KEY, sizeof(struct sysclock), 0644|IPC_CREAT);
        osClock = (struct sysclock*)shmat(smemId, NULL, 0);

        printWorkerInfo(pid, ppid, seconds, nanoseconds);
        printf("--Just Starting\n");
        shmdt(osClock);

    }
    else
    {
        printf("No argument for worker\n");
    }
    return 0;
}



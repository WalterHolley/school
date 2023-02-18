
#include <stdio.h>
#include <sys/shm.h>
#include <sys/ipc.h>
#include "osclock.h"

#define SMEM_KEY 0x3357

int smemId;
int pid;
int ppid;
int seconds;
int nanoseconds;
struct sysclock* osClock;
struct sysclock processStartTime;
struct sysclock processEndTime;

void printWorkerInfo(int pid, int ppid, int termSeconds, int termNano)
{
    printf("WORKER PID:%i PPID:%i SysClockS: %i SysClockNano: %i TermTimeS: %i TermTimeNano: %i\n", pid, ppid, osClock->seconds, osClock->nanoseconds, termSeconds, termNano);
}

//Determines how much time has elapsed
struct sysclock elapsedTime(struct sysclock startTime, struct sysclock* endTime)
{
    struct sysclock result;
    int endNanos = endTime->nanoseconds;
    int endSeconds = endTime->seconds;
    int deltaNanos = endNanos - startTime.nanoseconds;
    int deltaSeconds = endSeconds - startTime.seconds;

    if(deltaNanos < 0 && deltaSeconds > 0)
    {
        deltaSeconds = deltaSeconds - 1;
        deltaNanos = startTime.nanoseconds - endNanos;
    }

    result.nanoseconds = deltaNanos;
    result.seconds = deltaSeconds;

    return result;
}

/*Determines if the time criteria has been met, and
 * indicates if the process can be terminated*/
int doTerminate()
{
    int result = 0;
    if(processEndTime.seconds == osClock->seconds && processEndTime.nanoseconds <= osClock->nanoseconds)
    {
        result = 1;
    }
    else if(processEndTime.seconds < osClock->seconds)
    {
        result = 1;
    }

    if(result)
    {
        printWorkerInfo(pid, ppid, processEndTime.seconds, processEndTime.nanoseconds);
        printf("--Terminating\n");
    }

    return result;
}

int setup(int argc, char* argv[])
{
    int result = 0;
    if(argc > 2)
    {
        seconds = atoi(argv[1]);
        nanoseconds = atoi(argv[2]);
        pid = getpid();
        ppid = getppid();

        //get shared memory for os clock
        smemId = shmget(SMEM_KEY, sizeof(struct sysclock), 0644|IPC_CREAT);
        osClock = (struct sysclock*)shmat(smemId, (void *)0, 0);

        //record start time
        processStartTime.nanoseconds = osClock->nanoseconds;
        processStartTime.seconds = osClock->seconds;

        //set end time
        processEndTime = processStartTime;
        processEndTime.seconds += seconds + ((nanoseconds + processStartTime.nanoseconds) / NANOS_IN_SECOND);
        processEndTime.nanoseconds += ((nanoseconds + processStartTime.nanoseconds) % NANOS_IN_SECOND);
        result = 1;
    }
    else
    {
        printf("No argument for worker\n");
    }
    return result;
}

int main(int argc, char* argv[])
{


    if(setup(argc, argv))
    {
        int i = 0;
        struct sysclock timeElapsed;



        printWorkerInfo(pid, ppid, processEndTime.seconds, processEndTime.nanoseconds);
        printf("--Just Starting\n");

        //loop until process time has expired
        do
        {

            timeElapsed = elapsedTime(processStartTime, osClock);
            if(i < timeElapsed.seconds)
            {
                i = timeElapsed.seconds;
                printWorkerInfo(pid, ppid, processEndTime.seconds, processEndTime.nanoseconds);
                printf("--%i seconds have passed since starting\n", i);
            }
        }while(!doTerminate());
        shmdt(osClock);

    }
    else
    {
        return -1;
    }
    return 0;
}




#include <stdio.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include<string.h>
#include "osclock.h"

int pid;
int ppid;
int mqId;
int seconds;
int nanoseconds;
struct sysclock osClock;
struct sysclock processStartTime;
struct sysclock processEndTime;

void printWorkerInfo(int pid, int ppid, int termSeconds, int termNano)
{
    printf("WORKER PID:%i PPID:%i SysClockS: %i SysClockNano: %i TermTimeS: %i TermTimeNano: %i\n", pid, ppid, osClock.seconds, osClock.nanoseconds, termSeconds, termNano);
}

/**
 * Converts a clock message value into a sysclock object
 * @param msg
 * @return
 */
struct sysclock clockMsgToSysClock(struct clockmsg msg)
{
    struct sysclock result;
    char* token = strtok(msg.message, ",");

    result.seconds = atoi(token);
    token = strtok(NULL,",");
    result.nanoseconds = atoi(token);

    return result;
}

//Determines how much time has elapsed
struct sysclock elapsedTime(struct sysclock startTime, struct sysclock endTime)
{
    struct sysclock result;
    int endNanos = endTime.nanoseconds;
    int endSeconds = endTime.seconds;
    int deltaNanos = endNanos - startTime.nanoseconds;
    int deltaSeconds = endSeconds - startTime.seconds;

    if(deltaNanos < 0)
    {
        deltaSeconds = deltaSeconds - 1;
        deltaNanos =  NANOS_IN_SECOND + deltaNanos;
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
    if(processEndTime.seconds == osClock.seconds && processEndTime.nanoseconds <= osClock.nanoseconds)
    {
        result = 1;
    }
    else if(processEndTime.seconds < osClock.seconds)
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
    struct sysclock runTime;
    struct clockmsg msg;

    pid = getpid();
    ppid = getppid();

    //get run time for process
    mqId = msgget(pid, 0666 | IPC_CREAT);
    msgrcv(mqId, &msg, sizeof(msg), 1, 0);
    runTime = clockMsgToSysClock(msg);

    //get current os time
    msgrcv(mqId, &msg, sizeof(msg), 1, 0);
    osClock = clockMsgToSysClock(msg);

    //record start time
    processStartTime.nanoseconds = osClock.nanoseconds;
    processStartTime.seconds = osClock.seconds;

    //set end time
    processEndTime = processStartTime;
    processEndTime.seconds += runTime.seconds + ((runTime.nanoseconds + processStartTime.nanoseconds) / NANOS_IN_SECOND);
    processEndTime.nanoseconds += ((runTime.nanoseconds + processStartTime.nanoseconds) % NANOS_IN_SECOND);
    result = 1;

    return result;
}

int main(int argc, char* argv[])
{


    if(setup(argc, argv))
    {
        int i = 0;
        struct sysclock timeElapsed;
        struct clockmsg msg;



        printWorkerInfo(pid, ppid, processEndTime.seconds, processEndTime.nanoseconds);
        printf("--Just Starting\n");

        //loop until process time has expired
        do
        {
            msgrcv(mqId, &msg, sizeof(msg), 1, 0);
            osClock = clockMsgToSysClock(msg);
            timeElapsed = elapsedTime(processStartTime, osClock);
            if(i < timeElapsed.seconds)
            {
                i = timeElapsed.seconds;
                printWorkerInfo(pid, ppid, processEndTime.seconds, processEndTime.nanoseconds);
                printf("--%i seconds have passed since starting\n", i);
            }
        }while(!doTerminate());

        //destroy MQ
        msgctl(mqId, IPC_RMID, NULL);

    }
    else
    {
        return -1;
    }
    return 0;
}



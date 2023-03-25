
#include <stdio.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include<string.h>
#include "osclock.h"

int pid;
int ppid;
int listenerMQId, replyMQId;
int seconds;
int nanoseconds;
struct sysclock* osClock;
struct sysclock processStartTime;
struct sysclock processEndTime;

void printWorkerInfo()
{
    printf("WORKER PID:%i PPID:%i SysClockS: %i SysClockNano: %i \n", pid, ppid, osClock.seconds, osClock.nanoseconds);
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
int doTerminate(struct sysclock runTime)
{
    int result, randVal = 0;
    //generate random 0 - 99(inclusive)





    if(randVal >= 0 && randVal < 30)//0 - 29, end program
    {
        //change runtime value, send negative
        result = 1;
    }
    else if(randVal >= 30 && randVal <= 69) //30 - 69, continue running
    {
        //'work' until time elapses, then continue
        //send back original runTime value
    }
    else //70 - 99,  I/O blocked
    {
        //change runtime value
    }

    if(result)
    {
        printWorkerInfo();
        printf("--Terminating\n");
    }

    //send resulting runtime to reply queue

    return result;
}

int setup()
{
    int result = 0;
    struct sysclock runTime;
    struct clockmsg msg;

    pid = getpid();
    ppid = getppid();

    //get shared resources(osclock, reply queue ID, listener queue ID)

    //get listener queue

    //get response queue



    //get run time for process?

    result = 1;

    return result;
}

int main(int argc, char* argv[])
{


    if(setup())
    {

        struct clockmsg msg;
        struct clockmsg workTime;



        printWorkerInfo(pid, ppid, processEndTime.seconds, processEndTime.nanoseconds);
        printf("--Just Starting\n");

        //listen for message from parent
        do
        {

            msgrcv(mqId, &msg, sizeof(msg), 1, 0);
            workTime = clockMsgToSysClock(msg);


        }while(!doTerminate(workTime));

        //disconnect from shared resources

    }
    else
    {
        return -1;
    }
    return 0;
}



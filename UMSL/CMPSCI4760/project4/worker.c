
#include <stdio.h>
#include <stdbool.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#include <sys/shm.h>
#include <time.h>
#include "osclock.h"

time_t t;
int pid;
int ppid;
int listenerMQId, replyMQId, ossMemId;
int seconds;
int nanoseconds;


struct sysclock* osClock;
struct sysclock processStartTime;
struct sysclock processEndTime;


void printWorkerInfo()
{
    printf("WORKER PID:%d PPID:%d SysClockS: %d SysClockNano: %d \n", pid, ppid, osClock->seconds, osClock->nanoseconds);
}

/**
 * Simulates work for the given length of time
 */
void doWork(struct sysclock workTime)
{
    struct sysclock stopTime;
    stopTime.seconds = osClock->seconds;
    stopTime.nanoseconds = osClock->nanoseconds + workTime.nanoseconds;

    if(stopTime.nanoseconds > NANOS_IN_SECOND)
    {
        stopTime.seconds = stopTime.seconds + 1;
        stopTime.nanoseconds = NANOS_IN_SECOND % stopTime.nanoseconds;
    }

    while(osClock->seconds <= stopTime.seconds)
    {
        if(stopTime.nanoseconds <= osClock->nanoseconds)
        {
            if(stopTime.seconds >= osClock->seconds)
            {
                printf("PID: %i Work completed\n", pid);
                break;
            }
            else
            {
                printf("PID: %i Working. S: %i N: %i\n", pid, osClock->seconds, osClock->nanoseconds);
            }
        }
    }
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

/**
 * Convert system clock to
 * clock message for MQ
 * @param clock
 * @return clockmsg structure
 */
struct clockmsg SysClockToclockmsg(struct sysclock clock)
{
    struct clockmsg result;
    int i;
    char* messageText;
    sprintf(messageText, "%i", clock.nanoseconds);
    result.msgType = pid;

    for(i = 0; i < 14 && i < strlen(messageText); i++)
    {
        result.message[i] = messageText[i];
    }
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

/**
 * Determines if the time criteria has been met, and
 * indicates if the process can be terminated
 */
int doTerminate(struct sysclock runTime)
{
    int result, randVal, newTime = 0;
    struct clockmsg message;

    //generate random 0 - 99(inclusive)
    randVal = rand() % 100;


    if(randVal >= 0 && randVal < 30)//0 - 29, end program
    {
        printf("Worker %i: Ending Program\n", pid);
        //change runtime value, send negative
        randVal == 0? randVal = 1 :false;
        runTime.nanoseconds = (runTime.nanoseconds / randVal);

        //doWork(runTime);
        runTime.nanoseconds = runTime.nanoseconds * -1;
        result = 1;
    }
    else if(randVal >= 30 && randVal <= 69) //30 - 69, continue running
    {
        //'work' until time elapses, then continue
        printf("Worker %i: Continuing Program\n", pid);
        doWork(runTime);
    }
    else //70 - 99,  I/O blocked
    {
        //change runtime value
        printf("Worker %i: IO Blocked\n", pid);
        runTime.nanoseconds = (runTime.nanoseconds / randVal);
        doWork(runTime);
    }

    if(result)
    {
        printWorkerInfo();
        printf("--Terminating\n");
    }

    //send resulting runtime to reply queue
    message = SysClockToclockmsg(runTime);
    msgsnd(replyMQId, &message, sizeof(struct clockmsg), 0);
    return result;
}

int setup()
{
    int result = 0;
    int replyMQKey = ftok("oss.c", 1);
    int listenerMQKey = ftok("oss.c", 3);
    key_t sharedMemKey = ftok("oss.c", 5);

    pid = getpid();
    ppid = getppid();

    //init randomgen
    srand((unsigned) time(&t));

    //get shared resources(osclock, reply queue ID, listener queue ID)
    ossMemId = shmget(sharedMemKey, sizeof(struct sysclock), 0644|IPC_CREAT);
    osClock = (struct sysclock*) shmat(ossMemId, (void *)0, 0);


    if(ossMemId == -1)
    {
        perror("Worker could not retrieve shared memory");
    }
    else
    {


        if((struct sysclock*)osClock == NULL)
        {
            perror("Worker could not attach to shared memory");
        }
        else
        {
            listenerMQId = msgget(listenerMQKey, 0666 | IPC_CREAT);
            replyMQId = msgget(replyMQKey, 0666 | IPC_CREAT);

            if(listenerMQId < 1 || replyMQId < 1)
            {
                perror("Worker could not retrieve MQs");
            }
            result = 1;

        }
    }

    return result;
}

int main(int argc, char* argv[])
{


    if(setup())
    {
        struct clockmsg msg;
        struct sysclock workTime;
        printWorkerInfo();
        printf("--Just Starting\n");

        //listen for message from parent
        do
        {

            msgrcv(listenerMQId, &msg, sizeof(struct clockmsg), pid, 0);
            printf("Worker: %i message received\n", pid);
            workTime = clockMsgToSysClock(msg);
            printf("Work time: S:%d Nano:%d\n", workTime.seconds, workTime.nanoseconds);

        }while(!doTerminate(workTime));

        //disconnect from shared resources
        shmdt(osClock);

    }
    else
    {
        return -1;
    }
    return 0;
}



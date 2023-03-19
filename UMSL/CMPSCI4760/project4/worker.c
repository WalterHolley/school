
#include <stdio.h>
#include <stdbool.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#include "osclock.h"

time_t t;
int pid;
int ppid;
int listenerMQId, replyMQId, ossMemId;
int seconds;
int nanoseconds;

struct sysclock processStartTime;
struct sysclock processEndTime;
struct ossProperties* parentProperties;

void printWorkerInfo()
{
    printf("WORKER PID:%i PPID:%i SysClockS: %i SysClockNano: %i \n", pid, ppid, parentProperties->osClock.seconds, parentProperties->osClock.nanoseconds);
}

/**
 * Simulates work for the given length of time
 */
void doWork(struct sysclock workTime)
{
    struct sysclock stopTime;
    stopTime.seconds = parentProperties->osClock.seconds;
    stopTime.nanoseconds = parentProperties->osClock.nanoseconds + workTime.nanoseconds;

    if(stopTime.nanoseconds > NANOS_IN_SECOND)
    {
        stopTime.seconds = stopTime.seconds + 1;
        stopTime.nanoseconds = NANOS_IN_SECOND % stopTime.nanoseconds;
    }

    while(parentProperties->osClock.seconds <= stopTime.seconds)
    {
        if(stopTime.nanoseconds <= parentProperties->osClock.nanoseconds)
        {
            if(stopTime.seconds >= parentProperties->osClock.seconds)
            {
                break;
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
    int i = 0;
    char messageText[15];
    sprintf(messageText, "%i", clock.nanoseconds);
    result.msgType = pid;
    //TODO:-> loop to convert int to char*
    result.message = messageText;
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
        //change runtime value, send negative
        randVal == 0? randVal = 1 :false;
        runTime.nanoseconds = (runTime.nanoseconds / randVal);

        doWork(runTime);
        runTime.nanoseconds = runTime.nanoseconds * -1;
        result = 1;
    }
    else if(randVal >= 30 && randVal <= 69) //30 - 69, continue running
    {
        //'work' until time elapses, then continue
        doWork(runTime);
    }
    else //70 - 99,  I/O blocked
    {
        //change runtime value
        runTime.nanoseconds = (runTime.nanoseconds / randVal);
        doWork(runTime);
    }

    if(result)
    {
        printWorkerInfo();
        printf("--Terminating\n");
    }

    //send resulting runtime to reply queue
    runTime.seconds = pid;
    message = SysClockToclockmsg(runTime);
    msgsnd(replyMQId, &message, sizeof(message), 0);
    return result;
}

int setup()
{
    int result = 0;
    struct sysclock runTime;
    struct clockmsg msg;

    pid = getpid();
    ppid = getppid();

    //init randomgen
    srand((unsigned) time(&t));

    //get shared resources(osclock, reply queue ID, listener queue ID)
    ossMemId = shmget(SMEM_KEY, sizeof(struct ossProperties), 0644 | IPC_CREAT);

    if(ossMemId == -1)
    {
        perror("Worker could not retrieve shared memory");
    }
    else
    {
        parentProperties = shmat(ossMemId, NULL, 0);

        if(parentProperties == (void *) -1)
        {
            perror("Worker could not attach to shared memory");
        }
        else
        {
            //get MQs
            replyMQId = msgget(parentProperties->replyQueue, 0666 | IPC_CREAT);
            listenerMQId = msgget(parentProperties->listenerQueue, 0666 | IPC_CREAT);

            if(replyMQId == -1 || listenerMQId == -1)
            {
                perror("Worker could not access message queues");
            }
            else
            {
                result = 1;
            }

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

        printWorkerInfo(pid, ppid, processEndTime.seconds, processEndTime.nanoseconds);
        printf("--Just Starting\n");

        //listen for message from parent
        do
        {

            msgrcv(listenerMQId, &msg, sizeof(msg), pid, 0);
            workTime = clockMsgToSysClock(msg);

        }while(!doTerminate(workTime));

        //disconnect from shared resources
        shmdt(parentProperties);

    }
    else
    {
        return -1;
    }
    return 0;
}




#include <stdio.h>
#include <stdbool.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#include <sys/shm.h>
#include <time.h>
#include "osclock.h"

#define ACTION_BOUND 50

time_t t;
int pid;
int ppid;
int listenerMQId, replyMQId, ossMemId;
int seconds;
int nanoseconds;
const int MAX_WAIT_NANOS = 250000000;
bool terminate = false;
struct sysclock startTime;
struct sysclock nextCancelCheck;

struct sysclock* osClock;

void cleanup()
{
    //disconnect from shared resources
    shmdt(osClock);
}

/**
 * Determine how much time
 * has elapsed against the parent
 * process clock since the process started
 * @return
 */
struct sysclock elapsedTime()
{
    struct sysclock result;
    int endNanos = osClock->nanoseconds;
    int endSeconds = osClock->seconds;
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
 * Gets random number.  Returns
 * true if number below or at action bound
 * @return
 */
bool randRoll()
{
    bool result = false;

    if(rand() <= ACTION_BOUND)
    {
        result = true;
    }

    return result;
}

void printWorkerInfo()
{
    printf("User PID:%d PPID:%d SysClockS: %d SysClockNano: %d \n", pid, ppid, osClock->seconds, osClock->nanoseconds);
}


void updateTerminationFlag()
{
    if(randRoll())
    {
        terminate = true;
    }
    else //update next termination check
    {
        nextCancelCheck.seconds = osClock->seconds;
        nextCancelCheck.nanoseconds = osClock->nanoseconds;
        int nanos = (rand() % (MAX_WAIT_NANOS - 0 + 1));
        nextCancelCheck.nanoseconds += nanos;

        if(nextCancelCheck.nanoseconds > NANOS_IN_SECOND)
        {
            nextCancelCheck.seconds += 1;
            nextCancelCheck.nanoseconds = nextCancelCheck.nanoseconds - NANOS_IN_SECOND;
        }
    }
}

/**
 * Updates the termination flag after meeting certain criteria
 */
void checkForTermination()
{
    if(elapsedTime().seconds > 0)
    {
        if(osClock->seconds > nextCancelCheck.seconds)
        {
            updateTerminationFlag()
        }
        else if(osClock->seconds == nextCancelCheck.seconds && osClock->nanoseconds >= nextCancelCheck.nanoseconds)
        {
            updateTerminationFlag();
        }
    }
}

/**
 * Manage process resources
 */
void manageResources()
{
    if(randRoll())
    {
        if(randRoll())
        {
            //claim resource
        }
        else
        {
            //release resource
        }
    }
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
    srand((unsigned) pid);

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
            cleanup();
            perror("Worker could not attach to shared memory");
        }
        else
        {
            listenerMQId = msgget(listenerMQKey, 0666 | IPC_CREAT);
            replyMQId = msgget(replyMQKey, 0666 | IPC_CREAT);

            if(listenerMQId < 1 || replyMQId < 1)
            {

                cleanup();
                perror("Worker could not retrieve MQs");
            }
            result = 1;

        }
    }

    return result;
}

/**
 * Orchestrates the operations of the child process
 */
void doSomething()
{
    manageResources();
    checkForTermination();
}

int main(int argc, char* argv[])
{


    if(setup())
    {
        printWorkerInfo();
        printf("--Just Starting\n");

        do
        {
            doSomething();
            /*
            printf("Worker: %i (R)entering work loop\n", pid);
            if(msgrcv(listenerMQId, &msg, sizeof(struct clockmsg), pid, 0) != -1)
            {
                printf("Worker: %i message received\n", pid);
            }
            else
            {
                printWorkerInfo();
                cleanup();
                perror("Worker had a problem receiving a message");
                break;
            }
             */


        }while(!terminate);
        cleanup();
        printWorkerInfo();
        printf("--Terminating\n");

    }
    else
    {
        return -1;
    }
    return 0;
}



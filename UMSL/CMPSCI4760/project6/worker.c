
#include <stdio.h>
#include <stdbool.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>
#include <sys/shm.h>
#include <time.h>
#include "resource.h"

#define ACTION_BOUND 60

time_t t;
int pid;
int ppid;
int listenerMQId, replyMQId, ossMemId;
int seconds;
int nanoseconds;
int memChecks, memcheckLimit;
bool terminate = false;

struct sysclock* osClock;

struct replyMsg{
    int address;
    char* operation;
};

void cleanup()
{
    //disconnect from shared resources
    shmdt(osClock);
}

/**
 * Gets random number.  Returns
 * true if number below or at action bound
 * @return
 */
bool randRoll()
{
    bool result = false;
    int randVal = rand() % 100;

    if(randVal <= ACTION_BOUND)
    {
        result = true;
    }

    return result;
}

void printWorkerInfo()
{
    printf("User PID:%d PPID:%d SysClockS: %d SysClockNano: %d \n", pid, ppid, osClock->seconds, osClock->nanoseconds);
}

/**
 * Contrsucts a memory request to send to OSS
 * @param resId
 * @param allocation
 * @return
 */
struct resourcemsg getResourceMsg(int address, int base, int offset, int operation)
{
    struct resourcemsg msg;
    msg.msgType = pid;
    sprintf(msg.message, "%d:%d:%d:%d", operation, base, offset, address);

    return msg;
}

int randRange(int upper, int lower)
{
    int result = 0;

    result = (rand() % (upper - lower + 1)) + lower;

    return  result;
}



void updateTerminationFlag()
{
    if(randRoll())
    {
        terminate = true;
    }
    else //update next termination check
    {
        memChecks = 0;
    }
}

/**
 * Updates the termination flag after meeting certain criteria
 */
void checkForTermination()
{
    if(memChecks >= memcheckLimit)
    {
        updateTerminationFlag();
    }
}

struct replyMsg parseResourceMsg(struct resourcemsg msg)
{
   struct replyMsg result;

    char* token = strtok(msg.message, ":");
    result.operation = token;
    token = strtok(NULL, ":");
    result.address = atoi(token);

    return result;
}


/**
 * Handles reply from Message Queue
 */
void handleReply()
{
    struct resourcemsg msg;
    struct replyMsg parsedMessage;
    if(msgrcv(listenerMQId, (void *)&msg, sizeof(struct resourcemsg), pid, 0) != -1)
    {

        parsedMessage = parseResourceMsg(msg);
        printf("Worker %i: memory %s of address %i acknowledged\n", pid, parsedMessage.operation, parsedMessage.address);
        memChecks++;

    }
    else
    {
        printWorkerInfo();
        cleanup();
        perror("Worker had a problem receiving a message");
    }

}

/**
 * Build memory request and send to OSS
 */
void manageResources()
{
    bool read = false;
    int page, base, offset, address = 0;
    struct resourcemsg memRequest;

    //read or write operation
    read = randRoll();
    //page
    page = randRange(31, 0);
    //base
    base = randRange(1023, 0);
    //offset
    offset = randRange(1023, 0);
    //address
    address = (page * base) + offset;

    //build request
    memRequest = getResourceMsg(address, base, offset, read);

    //send request
    msgsnd(replyMQId, &memRequest, sizeof(struct resourcemsg), 0);

    //handle reply
    handleReply();
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

    //init memory check limit
    memcheckLimit = 1000 + randRange(100, -100);

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
void performOperation()
{
    manageResources();
    checkForTermination();
}

int main(int argc, char* argv[])
{
    struct resourcemsg msg;

    if(setup())
    {
        printWorkerInfo();
        printf("--Just Starting\n");

        do
        {
            performOperation();

        }while(!terminate);

        //communicate termination
        msg = getResourceMsg(-1, -1, -1, -1);
        msgsnd(replyMQId, &msg, sizeof(struct resourcemsg), 0);

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



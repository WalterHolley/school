//
// Created by Zero on 4/18/2023.
//

#ifndef PROJECT5_RESOURCE_H
#define PROJECT5_RESOURCE_H
#define MAX_RES_COUNT 20

#include "osclock.h"
struct resource{
    int pid;
    int priority;
    int res[10];

};

struct resourcemsg{
    long msgType;
    char message[15];
};





#endif //PROJECT5_RESOURCE_H

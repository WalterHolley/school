//
// Created by Zero on 4/18/2023.
//

#ifndef PROJECT5_RESOURCE_H
#define PROJECT5_RESOURCE_H
#define MAX_RES_COUNT 20
#define PROC_PAGE_TABLE_SIZE 32

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

struct frame{
    bool occupied;
    bool head;
    unsigned int written : 1;
    int id;

};

struct pageEntry {
    int id;
    char value;
};

struct proc_pages {
    int pid;
    int id;
    bool free;
    struct pageEntry pages[PROC_PAGE_TABLE_SIZE];

};






#endif //PROJECT5_RESOURCE_H

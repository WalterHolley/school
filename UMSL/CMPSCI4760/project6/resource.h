//
// Created by Zero on 4/18/2023.
//

#ifndef PROJECT5_RESOURCE_H
#define PROJECT5_RESOURCE_H
#define MAX_RES_COUNT 20
#define PROC_PAGE_TABLE_SIZE 32
#define PAGE_OFFSET 1024
#define FRAME_TABLE_SIZE 256


#include "osclock.h"
struct resource{
    int operation;
    int offset;
    int base;
    int address;
    int pid;

};

struct resourcemsg{
    long msgType;
    char message[50];
};

struct frame{
    bool occupied;
    bool head;
    int pid;
    int id;
    int write;

};

struct frameTable{
    int headIndex;
    struct frame frames[FRAME_TABLE_SIZE];
};

struct pageEntry {
    int frames[PAGE_OFFSET];
};

struct proc_pages {
    int pid;
    int allocation;
    struct pageEntry pages[PROC_PAGE_TABLE_SIZE];

};






#endif //PROJECT5_RESOURCE_H

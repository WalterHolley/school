//
// Created by Zero on 2/15/2023.
//

#ifndef OSCLOCK_H
#define OSCLOCK_H

#define SMEM_KEY 0x3357

struct sysclock {
    int nanoseconds;
    int seconds;
};

struct clockmsg {
    long msgType;
    char message[15];
};

struct ossProperties {
    struct sysclock osClock;
    int replyQueue;
    int listenerQueue;
};

const int NANOS_IN_SECOND = 1000000000;
const int NANOS_HALF_SECOND = 500000000;
#endif //OSCLOCK_H

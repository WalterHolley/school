//
// Created by Zero on 2/15/2023.
//

#ifndef OSCLOCK_H
#define OSCLOCK_H

struct sysclock {
    int nanoseconds;
    int seconds;
};

const int NANOS_IN_SECOND = 1000000000;
const int NANOS_HALF_SECOND = 500000000;
#endif //OSCLOCK_H

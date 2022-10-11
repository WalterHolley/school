//
// Created by Zero on 10/10/2022.
//

#ifndef TESTSCANNER_H
#define TESTSCANNER_H

#include <vector>
#include "scanner.h"
using namespace std;

class TestScanner
{
    public:
        TestScanner(vector<Token> tokens);
        void presentTokens();
};

#endif //TESTSCANNER_H

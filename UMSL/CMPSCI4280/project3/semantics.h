/**
 * semantics.h
 * @author Walter Holley III
 * 11/9/2022
 * Structure for semantics class implementation
 */

#ifndef SEMANTICS_H
#define SEMANTICS_H

#include <string>
#include <vector>
#include <stack>
#include "token.h"

using namespace std;

struct StackVariable
{
    string ID;
    int scope;
    int line;
};

class Semantics
{
    public:
        void push(StackVariable var);
        void pop();
        StackVariable top();
        int find(string varName);
        bool DEBUG = false;
    private:
        stack<StackVariable> symbolStack;
        void printDebug(StackVariable var, string action);
};

#endif //SEMANTICS_H

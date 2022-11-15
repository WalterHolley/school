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
#include "token.h"

using namespace std;

class Semantics
{
    public:
        void insert(string value);
        bool verify(string value);
        vector<string> getSymbolTable();
    private:
        vector<string> symbolTable;
};

#endif //SEMANTICS_H

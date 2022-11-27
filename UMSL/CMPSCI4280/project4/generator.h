/**
 * generator.h
 * @author Walter Holley III
 * 11/9/2022
 * Structure for code generator class implementation
 */

#ifndef GENERATOR_H
#define GENERATOR_H

#include "parser.h"
#include "semantics.h"

using namespace std;

class Generator
{
    public:
        void genASMFile(ParserNode* root, string fileName);
    private:
        Semantics semantics;
};

#endif //GENERATOR_H

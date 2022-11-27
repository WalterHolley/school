/**
 * generator.h
 * @author Walter Holley III
 * 11/9/2022
 * Structure for code generator class implementation
 */

#ifndef GENERATOR_H
#define GENERATOR_H

#include <fstream>
#include "parser.h"
#include "semantics.h"

using namespace std;

class Generator
{
    public:
        void genASMFile(ParserNode* root, string fileName);
    private:
        Semantics semantics;
        void processNode(ParserNode* node, FILE* outputFile);
        void handleVarNode(ParserNode* root, FILE* outputFile);
};

#endif //GENERATOR_H

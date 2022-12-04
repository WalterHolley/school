/**
 * generator.h
 * @author Walter Holley III
 * 11/9/2022
 * Structure for code generator class implementation
 */

#ifndef GENERATOR_H
#define GENERATOR_H

#include <fstream>
#include <string>
#include "parser.h"


using namespace std;


class Generator
{
    public:
        void genASMFile(ParserNode* root, string fileName);
    private:
        void processNode(ParserNode* node, FILE* outputFile);
        void handleVarNode(ParserNode* root, FILE* outputFile);
        void handleInputNode(ParserNode* node, FILE* outputFile);
        void handleBlock(ParserNode* node, FILE* outputFile);
        void handleOut(ParserNode* node, FILE* outputFile);
        void handleProgram(ParserNode* node, FILE* outputFile);
        Token handleExpr(ParserNode* node, FILE* outputFile);
        Token handleN(ParserNode* node, FILE* outputFile);
        Token handleA(ParserNode* node, FILE* outputFile);
        Token handleB(ParserNode* node, FILE* outputFile);
        Token handleM(ParserNode* node, FILE* outputFile);
        Token handleR(ParserNode* node, FILE* outputFile);

};

#endif //GENERATOR_H

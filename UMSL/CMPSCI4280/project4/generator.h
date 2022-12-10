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
#include <vector>
#include "parser.h"

using namespace std;

struct TokenOperation
{
    Token valueToken;
    TokenState operation;
};

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
        void handleAssign(ParserNode* node, FILE* outputFile);
        void handleConditional(ParserNode* node, FILE* outputFile);
        void handleLoop(ParserNode* node, FILE* outputFile);
        void handleMStats(ParserNode* node, FILE* outputFile);
        void handleLabel(ParserNode* node, FILE* outputFile);
        void handleGoto(ParserNode* node, FILE* outputFile);
        void handleStats(ParserNode* node, FILE* outputFile);
        vector<TokenOperation*> handleExpr(ParserNode* node, FILE* outputFile);
        vector<TokenOperation*> handleN(ParserNode* node, FILE* outputFile);
        vector<TokenOperation*> handleA(ParserNode* node, FILE* outputFile);
        vector<TokenOperation*> handleB(ParserNode* node, FILE* outputFile);
        vector<TokenOperation*> handleM(ParserNode* node, FILE* outputFile);
        TokenOperation* handleR(ParserNode* node, FILE* outputFile);

};

#endif //GENERATOR_H

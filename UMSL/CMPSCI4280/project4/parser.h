/**
 * Parser.h
 * @author Walter Holley III
 * 10/21/2022
 * Structure for the parser class.  outlines functions for
 * each BNF non-terminal expected for the program
*/

#ifndef PARSER_H
#define PARSER_H

#include <string>
#include <vector>
#include "token.h"

using namespace std;



enum TerminalEnum
{
    TERMPROGRAM = 0,
    BLOCK = 1,
    VARS = 2,
    EXPR = 3,
    TERMN = 4,
    TERMA = 5,
    TERMB = 6,
    TERMM = 7,
    TERMR = 8,
    STATS = 9,
    MSTAT = 10,
    STAT = 11,
    IN = 12,
    OUT = 13,
    TERMIF = 14,
    LOOP = 15,
    TERMASSIGN = 16,
    TERMRO = 17,
    TERMLABEL = 18,
    TERMGOTO = 19
};

struct ParserNode {
    TerminalEnum nonTerminal;
    Token value = Token();
    struct ParserNode* parentNode = NULL;
    vector<ParserNode*> children;

};

class Parser
{
    public:
        ParserNode* parseTokens(vector<Token> tokens);

    private:
        ParserNode* program();
        ParserNode* block();
        ParserNode* vars();
        ParserNode* expr();
        ParserNode* N();
        ParserNode* A();
        ParserNode* B();
        ParserNode* M();
        ParserNode* R();
        ParserNode* stats();
        ParserNode* mStat();
        ParserNode* stat();
        ParserNode* in();
        ParserNode* out();
        ParserNode* If();
        ParserNode* loop();
        ParserNode* assign();
        ParserNode* RO();
        ParserNode* label();
        ParserNode* Goto();
        vector<Token> tokens;
        Token getNextToken();
        Token lookAhead();
        ReservedWords getReservedWord(string rWordValue);
        ParserNode* createTokenNode(Token nodeToken, ParserNode* parentNode);
        string createErrorMessage(Token token);
};
#endif //PARSER_H

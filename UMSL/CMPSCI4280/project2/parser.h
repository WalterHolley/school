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

struct ParserNode {
    string nonTerminal;
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
        void expr();
        void N();
        void A();
        void M();
        void R();
        ParserNode* stats();
        ParserNode* mStat();
        ParserNode* stat();
        ParserNode* in();
        void out();
        void If();
        void loop();
        void assign();
        void RO();
        void label();
        void Goto();
        vector<Token> tokens;
        Token getNextToken();
        Token lookAhead();
        ReservedWords getReservedWord(string rWordValue);
        ParserNode* createTokenNode(Token nodeToken, ParserNode* parentNode);
};
#endif //PARSER_H

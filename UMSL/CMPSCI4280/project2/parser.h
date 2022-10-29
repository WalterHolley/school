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

struct parserNode {
    string value;
};

class Parser
{
    public:
        void parseTokens(vector<Token> tokens);
        void program();
        void block();
        void vars();
        void expr();
        void N();
        void A();
        void M();
        void R();
        void stats();
        void mStat();
        void stat();
        void in();
        void out();
        void If();
        void loop();
        void assign();
        void RO();
        void label();
        void Goto();
    private:
        vector<Token> tokens;
        Token getNextToken();
        Token lookAhead();
};
#endif //PARSER_H

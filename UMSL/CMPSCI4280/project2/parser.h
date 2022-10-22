/**
 * Parser.h
 * @author Walter Holley III
 * 10/21/2022
 * Structure for the parser class.  outlines functions for
 * each BNF non-terminal expected for the program
*/

#ifndef PARSER_H
#define PARSER_H
class Parser
{
    public:
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
};
#endif //PARSER_H

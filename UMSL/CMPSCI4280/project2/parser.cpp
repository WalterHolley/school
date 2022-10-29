/**
 * parser.cpp
 * @author Walter Holley III
 * 10/21/2022
 * Implementation of parser for the language
 */

#include "parser.h"

void Parser::parseTokens(int tokens)
{

}

/**
 * Program non-terminal.  called first.
 * handles the beginning of the program
 */
void Parser::program()
{
    //call <vars>
    vars();
    //get next token
    //check if next token is program
    //call block if true
    block();
}
void Parser::block()
{
    //check if next token is begin
    //check if next is vars
    //check if next token is stats
    //check if next token is end
}

void Parser::expr()
{
    //call <N>
    N();
    //if next token is "-", call <expr>, otherwise done
}

void Parser::N()
{
    //call <A>
    A();
    //check for "+" or "*", run <N> if true

}

void Parser::A()
{
    //check next token for "/", call <A> if true
    //call <M>
    M();
}

void Parser::M()
{
    //check for ":" token, call <M> if true
    //otherwise call <R>
}

void Parser::R()
{
    //check for "(", call <expr> if true, then check for ")"
    //if false, check for number or identifier
}

void Parser::stats()
{
    //call <stat>
    stat();
    //call <mStat>
    mStat();
}

void Parser::mStat()
{
    //if certain reserved words are the next token, call <stat>, then <mStat>
}

void Parser::stat()
{
    //switch case for <in>, <out>,<block>,<if>, <loop>, <assign>, <goto>, and <label>.
    //check for ";" at end of each one
}

void Parser::in()
{
    // check for input reserved word followed by an IDToken
}

void Parser::out()
{
    //check for output reserved work
    //if true, call <expr>
}

void Parser::If()
{
    //check for if reserved word
    //check for "["
    //call <expr>
    //check for "]"
    //check for  "then" reserved word
    //call <stat>
    //check for "pick" reserved word, call <stat> if true

}

void Parser::loop()
{
    //check for "while"
    //check for "["(required), call <expr><RO><expr> if true
    //check for "]"(required), call <stat> if true
}

void Parser::assign()
{
    //check for "assign"
    //check for IDToken
    //check for "=", call <expr> if true
}

void Parser::RO()
{
    //check for following operators, fail if none exist:
    // >, <, ==, [=](three tokens), !=
}

void Parser::label()
{
    //check for label rword
    //check for ID token if true
}

void Parser::Goto()
{
    //check for warp rword
    //check for IDtoken if true
}
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
    vars();
    //get next token
    //check if next token is program
    //call block if true
    block();
}
void block()
{
    //check if next token is begin
    //check if next is vars
}
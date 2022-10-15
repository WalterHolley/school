/**
 * testScanner.cpp
 * @author Walter Holley III
 * Prints the tokens from the scanner
 * application.
 */

#include "testScanner.h"

#include <cstdio>


void printToken(Token token)
{
    printf("line: %i\t column: %i\t type: %s\t value: %s\n",token.line,token.col, token.name.c_str(), token.value.c_str());
}



void TestScanner::presentTokens(vector<Token> fileTokens)
{

    for(vector<Token>::iterator iter = fileTokens.begin(); iter != fileTokens.end(); iter++)
    {
        printToken(*iter);
    }
}




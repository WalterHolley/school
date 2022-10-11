/**
 * testScanner.cpp
 * @author Walter Holley III
 * Prints the tokens from the scanner
 * application.
 */

#include "testScanner.h"

#include <cstdio>

vector<Token> fileTokens;

string getTokenString(TokenState tokenId)
{
    string result = "ERROR";
    if(tokenId != ERROR)
    {
        result = TOKEN_NAME[tokenId - 1];
    }

    return result;
}

void printToken(Token token)
{
    string tokenType = getTokenString(token.ID);
    printf("line: %i\t column: %i\t type: %s\t value: %s\n",token.line,token.col, tokenType.c_str(), token.value.c_str());
}

TestScanner::TestScanner(vector <Token> tokens) {
    fileTokens = tokens;
}

void TestScanner::presentTokens()
{

    for(vector<Token>::iterator iter = fileTokens.begin(); iter != fileTokens.end(); iter++)
    {
        printToken(*iter);
    }
}




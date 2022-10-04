/**
* Scanner.cpp
 * @author Walter Holley III
 * 10/3/2022
 * Verifies the tokens received by the application, and
 * identifies what kind of token was received
*/
#include "scanner.h"
#include <stdexcept>
#include <vector>
using namespace std;

TokenType Scanner::findToken(char tokenChar)
{
    TokenType result;
    string element = tokenChar;
    try
    {
        result = TOKENS.at(element)
    }
    catch (const exception& e)
    {
        result = UNKNOWN;
    }

    return result;

}

vector<Token> Scanner::verifyToken(string token)
{
    vector<Token>
}

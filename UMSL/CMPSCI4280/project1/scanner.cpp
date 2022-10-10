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
#include <iostream>
using namespace std;

/**
 * Determine if the given character is a valid token
 * @param tokenChar the character to evaliuate
 * @return index of tokens locatioo in the Token Table.
 * returns -1 if not found
 */
int Scanner::findToken(char tokenChar)
{
    int result = -1;
    string element = tokenChar;
    try
    {
        for(int i = 0; i < MAX_TOKENS; i++)
        {
            if(TOKENS[0][i].c_str() == tokenChar)
            {
                result = i;
            }
        }
    }
    catch (const exception& e)
    {
        result = -2;
    }

    return result;

}

vector<Token> Scanner::verifyToken(string token)
{
    vector<Token> tokens;
    TokenState state = START;
    int lastColumn = token.length();
    String tokenValue;

    int column = 0;
    while(state != FINAL || state != ERROR)
    {
        char next = token.at(column);
        int tokenId = findToken(next);
        if(tokenId == -1)
        {
            state = ERROR;
        }
        if( state == START)
        {
            column++;
            //continue if whitespace
            if(next == ' ')
            {

                continue;
            }
            else             //update state
            {
                state = TOKENS[state][tokenId];
                token.push_back(next);
            }

        }
        if(state == ERROR)// record failure value and stop loop
        {
            Token errorToken;
            errorToken.ID = ERROR;
            errorToken.col = column + 1;
            errorToken.value = next;
            tokens.push_back(errorToken);
            break;
        }
    }
}

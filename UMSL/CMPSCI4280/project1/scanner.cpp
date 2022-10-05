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

vector<Token> Scanner::verifyToken(string tokenString, int lineNumber)
{
    vector<Token> tokens;
    int column = 0;
    string value;
    TokenState state = START;
    TokenType initialToken;


    while(state != FINAL && state != ERROR)
    {
        char symbol = tokenString.at(column);
        if(state == START)
        {
            if(symbol == ' ')
            {
                column++;
                continue;
            }
            else
            {
                initialToken = findToken(symbol);
                if(initialToken != UNKNOWN)
                {
                    state = NEXT;
                    append(value, symbol);
                    column++;

                }
            }
        }
        else if (state == NEXT)
        {
            if(symbol == ' ')
            {
                column++;
                continue;
            }
        }


        //check for whitespace

        //check for start

        //check for next

        //check for unknown
    }

    return tokens;
}

/**
 * @brief Reads a file and outputs the tokens from the file
 * @param fb file buffer to read
 */
void Scanner::scanFile(std::filebuf fb)
{
    istream stream(&fb);
    string line;
    int lineCount = 0;
    vector<Token> tokens;
    try
    {
        getline(stream, line);
        while(stream)
        {
            if(!line.empty())
            {
                lineCount++;
                tokens = verifyToken(line, lineCount);

                //print tokens

                //stop processing if unknown token found

            }
        }
    }
}

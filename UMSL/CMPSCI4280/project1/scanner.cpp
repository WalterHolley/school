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
#include <fstream>
#include <sstream>
using namespace std;

/**
 * Determine if the given character is a valid token
 * @param tokenChar the character to evaliuate
 * @return index of tokens location in the Token Table.
 * returns -999(tokenstate: ERROR) if not found
 */
int Scanner::findToken(char tokenChar)
{
    int result = ERROR;
    string element(1, tokenChar);

    for(int i = 0; i < MAX_TOKENS; i++)
    {
       if(TOKENS[0][i] == element)
       {
           result = i;
       }
    }

    return result;

}

/**
 * Breaks down a given file into its token artifacts
 * @param fileName the file to scan
 * @return Vector collection of the tokens produced from the file
 */
vector<Token>Scanner::scanFile(std::string fileName)
{
    filebuf  fb;

    vector<Token> tokens;
    vector<Token> allTokens;

    if(fb.open(fileName.c_str(), ios::in))
    {
        istream stream(&fb);
        string line;
        int lineCount = 1;
        try
        {
            getline(stream, line);
            while(stream)
            {
                if(!line.empty())
                {
                    //process file line
                    tokens = verifyTokens(line, lineCount);


                    allTokens.insert(allTokens.end(), tokens.begin(), tokens.end());
                    lineCount++;

                    //check for error
                    if(tokens.back().ID == ERROR)
                    {
                        cout << "An error occurred while scanning the source file.  All tokens may not have been found" << endl;
                        break;
                    }
                    getline(stream, line);
                }
            }
            fb.close();
        }
        catch (const exception& e)
        {
            cout << "An error occurred while scanning the source file" <<endl;
            cerr << e.what() <<'\n';
            fb.close();
        }
    }
    else
    {
        cout << fileName <<": File not found"<<endl;
    }

    return allTokens;
}

vector<Token> Scanner::verifyTokens(string token, int lineNumber)
{
    vector<Token> tokens;
    TokenState state = START;
    TokenState prevState;
    int lastColumn = token.length();
    string tokenValue;
    Token nextToken;

    int column = 0;
    while(state != FINAL && state != ERROR)
    {
        char next;
        int tokenId;
        if(column >= lastColumn)
        {
            state = FINAL;
        }
        else
        {
            next = token.at(column);
            tokenId = findToken(next);

        }

        column++;

        if(tokenId == ERROR)
        {
            state = ERROR;
        }
        if( state == START)
        {

            //continue if whitespace
            if(next == ' ')
            {
                continue;
            }
            else             //update state
            {
                tokenValue = "";
                stringstream  ss;
                int stateVal;
                ss << TOKENS[state][tokenId];
                ss >> stateVal;
                state = (TokenState)stateVal;
                tokenValue.push_back(next);
                nextToken.ID = state;
                nextToken.col = column;
                nextToken.line = lineNumber;
                prevState = state;
            }

        }
        else
        {
            if(next == ' ' && state != FINAL)
            {
                // complete token
                state = START;

                nextToken.ID = prevState;
                nextToken.value = tokenValue;
                tokens.push_back(nextToken);

            }
            else if(state == FINAL)
            {
                // complete token
                nextToken.ID = prevState;
                nextToken.value = tokenValue;
                tokens.push_back(nextToken);
            }
            else if(state != prevState)
            {
                //make token of previous state
                nextToken.value = tokenValue;
                tokens.push_back(nextToken);

                //setup new token
                nextToken.col = column;
                tokenValue = "";
                nextToken.ID = state;
                tokenValue.push_back(next);
            }
            else
            {
                tokenValue.push_back(next);
            }
        }

        if(state == ERROR)// record failure value and stop loop
        {
            Token errorToken;
            errorToken.ID = ERROR;
            errorToken.col = column;
            errorToken.value = tokenValue;
            tokens.push_back(errorToken);
            break;
        }
    }

    return tokens;
}

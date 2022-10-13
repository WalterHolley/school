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
 * Checks token to determine if it's a reserved word
 * @param token
 * @return
 */
bool  isReservedWord(string token)
{
    bool result = false;

    for(int i = 0; i < RESERVED_WORDS->length(); i ++)
    {
        if(RESERVED_WORDS[i] == token)
        {
            result = true;
            break;
        }
    }
    return result;
}

/**
 * Removes carriage returns from string
 * @param value
 * @return
 */
string cleanup(string value)
{

   if((!value.empty() && value[value.size() - 1] == '\r') || (!value.empty() && value[value.size() - 1] == '\n'))
   {
       value.erase(value.size() - 1);
   }
    return value;
}

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
                    //remove end line characters
                    line = cleanup(line);
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

/**
 * Analyzes a string for its tokens
 * @param token the string to be analyzed
 * @param lineNumber the number of the line to be associated with the tokens
 * @return collection of tokens found in a string
 */
vector<Token> Scanner::verifyTokens(string token, int lineNumber)
{
    vector<Token> tokens;
    TokenState state = START;
    int stateVal;
    int lastColumn = token.length();
    bool comments = false;
    Token nextToken;
    stringstream ss;

    int column = 0;
    while(state != FINAL && state != ERROR)
    {
        char next;
        int tokenId;

        if(column >= lastColumn)//if last position scanned, end loop
        {
            state = FINAL;
        }
        else //get next token
        {
            next = token.at(column);
            tokenId = findToken(next);
            column++;

            if(next == ' ')//check for empty space
            {
                state = START;
                if(nextToken.ID) //commit previous token if any
                {
                    nextToken.line = lineNumber;
                    tokens.push_back(nextToken);
                }
                nextToken.value = ""; //reset token
                nextToken.ID = ERROR;

                continue;
            }
            else if(state == START) //setup new token
            {

                if(tokenId != ERROR) //token recognized
                {
                    ss << TOKENS[state][tokenId];
                    ss >> stateVal;
                    state = (TokenState)stateVal;
                    nextToken.ID = state;
                    nextToken.col = column;
                    nextToken.line = lineNumber;
                    nextToken.value.push_back(next);

                    if(state == DELIMTOKEN) //delimiter found.  save and reset token
                    {

                    }
                    else if(state == OPTOKEN) // check for compound tokens(look-ahead)
                    {

                    }
                }
                else //unrecognized token.  report issue
                {
                    state = ERROR;
                    nextToken.ID = ERROR;
                    nextToken.col = column;
                    nextToken.line = lineNumber;
                    nextToken.value.push_back(next);
                    tokens.push_back(nextToken);
                    continue;
                }
            }
            else
            {
                //get state for new token
                if(tokenId != ERROR)
                {
                    ss << TOKENS[state][tokenId];
                    ss >> stateVal;
                    state = (TokenState)stateVal;
                    if(state == nextToken.ID)
                    {
                        nextToken.value.push_back(next);
                    }
                    else //token type changed.evaluate
                    {
                        if(state != ERROR)
                        {

                        }
                    }

                }
                else //unrecognized token.  report issue
                {
                    state = ERROR;
                    nextToken.ID = ERROR;
                    nextToken.col = column;
                    nextToken.line = lineNumber;
                    nextToken.value.push_back(next);
                    tokens.push_back(nextToken);
                    continue;
                }

            }


        }

    }

    return tokens;
}

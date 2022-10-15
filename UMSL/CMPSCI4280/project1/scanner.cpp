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
 * Determines the next state of the loop
 * @param currentState the curent state of the loop
 * @param tokenId
 * @return new state of the loop
 */
TokenState getNewState(TokenState currentState, int tokenId)
{
    TokenState result;
    stringstream ss;

    int stateVal;
    string tokenVal = TOKENS[currentState][tokenId];
    ss.str("");

    for(unsigned  i = 0; i < tokenVal.size(); i++)
    {
        ss << tokenVal[i];
    }

    ss >> stateVal;
    result = (TokenState)stateVal;

    return result;
}

/**
 * gets the printable name of the token
 * @param tokenId the token state enumerator value
 * @return string representing the name of the token
 */
string getTokenName(int tokenId)
{
    stringstream  ss;
    string id;
    ss << tokenId;
    ss >> id;

    string result = "ERROR";

    for(int i = 0; i < MAX_TOKEN_TYPES; i++)
    {
        if(TOKEN_NAME[i][0] == id)
        {
            result = TOKEN_NAME[i][1];
        }
    }

    return result;
}

/**
 * Returns the operation token state of a token
 * @param tokenId the numeric id of a token
 * @return state indicating what kind of operator token belongs to the given id
 */
TokenState getOpTokenState(int tokenId)
{
    TokenState result = ERROR;

    return  result;
}

/**
 * Checks token to determine if it's a reserved word
 * @param token
 * @return true if reserved word found.  otherwise false
 */
bool isReservedWord(string token)
{
    bool result = false;

    for(int i = 0; i < MAX_RWORDS; i ++)
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

                    //check for empty lines
                    if(tokens.size() == 1 && tokens.back().value == "")
                    {
                        lineCount++;
                    }
                    else
                    {
                        allTokens.insert(allTokens.end(), tokens.begin(), tokens.end());
                        lineCount++;
                    }



                    //check for error
                    if(tokens.back().ID == ERROR)
                    {
                        cout << "An error occurred while scanning the source file.  All tokens may not have been found" << endl;
                        break;
                    }
                    getline(stream, line);
                }
            }
            //add EOF Token
            Token endToken;
            endToken.ID = EOFTOKEN;
            endToken.value = "EOF";
            endToken.line = lineCount - 1;
            endToken.col = tokens.back().col + 1;
            allTokens.push_back(endToken);
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

    int lastColumn = token.length();
    bool comments = false;
    Token nextToken;


    int column = 0;
    while(state != FINAL && state != ERROR)
    {
        char next;
        int tokenId;

        if(column >= lastColumn)//if last position scanned, end loop
        {
            state = FINAL;
            //resolve final token if any
            if(nextToken.ID)
            {
                if(nextToken.ID == IDTOKEN)
                {
                    if(isReservedWord(nextToken.value))
                    {
                        nextToken.ID = RWORD;
                    }
                }
                nextToken.name = getTokenName((int)nextToken.ID);
                tokens.push_back(nextToken);
            }
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
                    //check for reserved word
                    if(isReservedWord(nextToken.value))
                    {
                        nextToken.ID = RWORD;
                    }
                    nextToken.name = getTokenName((int)nextToken.ID);
                    tokens.push_back(nextToken);
                }
                //reset token
                nextToken = Token();

                continue;
            }
            else if(state == START) //setup new token
            {

                if(tokenId != ERROR) //token recognized
                {
                    //update state
                    state = getNewState(state, tokenId);

                    nextToken.ID = state;
                    nextToken.col = column;
                    nextToken.line = lineNumber;
                    nextToken.value.push_back(next);

                    if(state == DELIMTOKEN) //delimiter found.  save and reset token
                    {
                        nextToken.ID = (TokenState)tokenId;
                        nextToken.name = getTokenName((int)nextToken.ID);
                        tokens.push_back(nextToken);
                        nextToken = Token();
                        continue;
                    }
                    else if(state == OPTOKEN) // check for compound tokens(look-ahead)
                    {
                        if(column < lastColumn)
                        {
                            //next = token.at(column);
                            column++;

                        }
                    }
                }
                else //unrecognized token.  report issue
                {
                    state = ERROR;
                    nextToken.ID = ERROR;
                    nextToken.col = column;
                    nextToken.line = lineNumber;
                    nextToken.name = getTokenName((int)nextToken.ID);
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

                    state = getNewState(state, tokenId);
                    if(state == nextToken.ID)
                    {
                        nextToken.value.push_back(next);
                    }
                    else //token type changed.evaluate
                    {
                        if(state != ERROR)
                        {
                            //commit previous token
                        }
                    }

                    if(state == DELIMTOKEN) //delimiter found.  save and reset token
                    {
                        nextToken.ID = (TokenState)tokenId;
                        nextToken.name = getTokenName((int)nextToken.ID);
                        tokens.push_back(nextToken);
                        nextToken = Token();
                        continue;
                    }
                    else if(state == OPTOKEN) // check for compound tokens(look-ahead)
                    {
                        if(column < lastColumn)
                        {
                            //next = token.at(column);
                            column++;

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
                    nextToken.name = getTokenName((int)nextToken.ID);
                    tokens.push_back(nextToken);
                    continue;
                }

            }


        }

    }

    return tokens;

}

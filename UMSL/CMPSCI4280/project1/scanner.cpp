/**
* Scanner.cpp
 * @author Walter Holley III
 * 10/3/2022
 * Verifies the tokens received by the application, and
 * identifies what kind of token was received
*/
#include "scanner.h"
#include <stdexcept>
#include<algorithm>
#include <vector>
#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
using namespace std;

//scanner error message
const string SCANNER_ERROR_MESSAGE = "An error occurred while scanning the source file.  All tokens may not have been found";

//keep track of line number and tokens in file
int lineCount;
int column;
bool comments = false;

vector<Token> allTokens;

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




int stringToInt(string number)
{
    stringstream  ss;
    int result;
    try
    {
        ss << number;
        ss >> result;
    }
    catch (const exception& e)
    {
        cout << SCANNER_ERROR_MESSAGE <<endl;
        cerr << e.what() <<'\n';
    }

    return  result;
}
/*
Token finalToken(Token token, char nextChar)
{
    string finalTokenValue;
    string finalTokenId;
    string tokenArray[][];
    TokenState initialState = token.ID;
    int maxTokens;

    finalTokenValue.push_back(token.value);

    //combine character
    if(nextChar != '')
    {
        finalTokenValue.push_back(nextChar);

    }

    if(token.ID == DELIMTOKEN)
    {
        maxTokens = DELIM_TOKEN_COUNT;
        tokenArray = DELIMTOKENS;

    }
    else if(token.ID == OPTOKEN)
    {
        maxTokens = OP_TOKEN_COUNT;
        tokenArray = OPTOKENS;
    }

    if(token.ID == OPTOKEN || token.ID == DELIMTOKEN)
    {
        //search for token
        for(int i = 0; i < maxTokens; i++)
        {
            if(finalTokenValue == tokenArray[i][0])
            {
                token.value = finalTokenValue;
                //final token name
                for(int c = 0; c < MAX_TOKEN_TYPES; c++)
                {
                    if( tokenArray[i][1] == TOKEN_NAME[c][0])
                    {
                        token.name = TOKEN_NAME[c][1];
                        token.ID = (TokenState) stringToInt(TOKEN_NAME[c][0])
                        break;
                    }
                }
            }
        }
    }

    return token;
}
*/

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
 * Removes carriage returns from string
 * @param value
 * @return
 */
string Scanner::handleNewLines(std::string value) {

    size_t  found = value.find("\r\n");
    string token;
    while(found != string::npos)
    {

        value.replace(found, 2, "");

        //check for potential token
        token = value.substr(0,found);
        if(!token.empty())
        {
          vector<Token> lineTokens = verifyTokens(token, lineCount);
          column = 1;
          allTokens.insert(allTokens.end(), lineTokens.begin(), lineTokens.end());
          value.erase(0,found);
        }
        lineCount++;
        found = value.find("\r\n");
    }
    //value.erase(value.size() - 1);
    return value;
}

/**
 * Breaks down a given file into its token artifacts
 * @param fileName the file to scan
 * @return Vector collection of the tokens produced from the file
 */
vector<Token>Scanner::scanFile(std::string fileName)
{
    filebuf  fb;
    Token nextToken;
    vector<Token> tokens;



    if(fb.open(fileName.c_str(), ios::in))
    {
        istream stream(&fb);
        char delimiter = ' ';
        string line;
        lineCount = 1;
        try
        {

            while(getline(stream, line, delimiter))
            {
                //process token values that are split between lines
                line = handleNewLines(line);

                //check for errors
                if(!allTokens.empty() && allTokens.back().ID == ERROR)
                {
                    cout << SCANNER_ERROR_MESSAGE << endl;
                    break;
                }
                else if(!line.empty())
                {
                    tokens = verifyTokens(line, lineCount);
                    allTokens.insert(allTokens.end(), tokens.begin(), tokens.end());


                    //check for error
                    if(tokens.back().ID == ERROR)
                    {
                        cout << SCANNER_ERROR_MESSAGE << endl;
                        break;
                    }
                }
            }
            //add EOF Token
            Token endToken;
            endToken.ID = EOFTOKEN;
            endToken.name = "EOFToken";
            endToken.value = "EOF";
            endToken.line = lineCount - 1;
            endToken.col = tokens.back().col + 1;
            allTokens.push_back(endToken);
            fb.close();
        }
        catch (const exception& e)
        {
            cout << SCANNER_ERROR_MESSAGE <<endl;
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
 /*
vector<Token> Scanner::verifyTokens(string token, int lineNumber)
{
    vector<Token> tokens;
    TokenState state = START;

    int lastColumn = token.length();
    int column = 0;

    Token nextToken;


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

            //TODO: check if comments enabled

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
                            tokens.push_back(nextToken);
                            nextToken = Token();

                            //start next token
                            nextToken.ID = state;
                            nextToken.value.push_back(next);
                            nextToken.col = column;
                            nextToken.line = lineCount;
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
*/
vector<Token> Scanner::verifyTokens(string token, int lineNumber)
{
    int tokenSize = 0;
    int tokenId;
    TokenState state = START;
    char nextChar;
    Token nextToken;
    vector<Token> tokens;

    while(state != FINAL & state != ERROR)
    {
        if(tokenSize >= token.length())
        {
            //end the loop
            state  = FINAL;
            if(nextToken.ID != 0)
            {
                tokens.push_back(nextToken);
            }
        }
        else
        {
            nextChar = token.at(tokenSize);

            //TODO: check if comments enabled
            if(nextChar == '#')
            {
                comments = !comments;
            }

            if(comments)
            {
                tokenSize++;
                continue;
            }

            //get state of current character
            tokenId = findToken(nextChar);

            //evaluate token states
            if(state == START)
            {
                state = getNewState(state, tokenId);
                nextToken = Token();
                nextToken.ID = state;
                nextToken.line = lineCount;
                nextToken.col = column;
                nextToken.value.push_back(nextChar);
                tokenSize++;

                //check for delimiter or operator
                if(state == DELIMTOKEN)
                {

                }
                else if (state == OPTOKEN)
                {

                }

            }
            else if(state == getNewState(state, tokenId))
            {
                if(state != ERROR)
                {
                    nextToken.value.push_back(nextChar);
                    tokenSize++;

                    //check next
                    if(tokenSize < token.length())
                    {
                        char lookAhead = token.at(tokenSize);
                        int id = findToken(lookAhead);
                        if(id == ERROR)
                        {
                            nextToken.ID = ERROR;
                            nextToken.name = getTokenName(ERROR);
                            nextToken.value.push_back(lookAhead);
                        }
                        else
                        {
                            TokenState nextState = getNewState(state, id);
                            if(state == nextState)
                            {
                                nextToken.value.push_back(lookAhead);
                                tokenSize++;
                                continue;
                            }
                            else
                            {
                                //check
                            }
                        }
                    }
                    else
                    {
                        state = FINAL;
                    }
                    continue;
                }
                else
                {
                    nextToken.ID = ERROR;
                    nextToken.name = getTokenName((int)ERROR);
                    tokenSize++
                    tokens.push_back(nextToken);
                    break;
                }
            }
            else //state has changed
            {
                //finalize previous token if any

                //check for delim or operator token

                //look ahead for delim/operator
            }
        }
    }
    return tokens;
}

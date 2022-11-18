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

/**
 * Returns the operation token state of a token
 * @param tokenId the numeric id of a token
 * @return state indicating what kind of operator token belongs to the given id
 */
TokenState getOpTokenState(string token, int tokenPosition)
{
    TokenState result = ERROR;
    char startingToken = token.at(tokenPosition);
    char nextToken;
    string finalToken;

    finalToken.push_back(startingToken);


    switch(startingToken)
    {
        case '=':
        case ':':
        case '!':
        case '|':
        case '&':
            if(tokenPosition + 1 < token.length())
            {
                nextToken = token.at(tokenPosition + 1);
                finalToken.push_back(nextToken);
            }
        default:
            for(int i = 0; i < OP_TOKEN_COUNT; i++)
            {
                if(finalToken == OPTOKENS[i][0])
                {
                    int tokenId = stringToInt(OPTOKENS[i][1]);
                    result = (TokenState)tokenId;
                    break;
                }
            }

    }

    //check single character operators
    if(result == ERROR && finalToken.length() == 2)
    {
        finalToken.erase(finalToken.end());

        for(int i = 0; i < OP_TOKEN_COUNT; i++)
        {
            if(finalToken == OPTOKENS[i][0])
            {
                int tokenId = stringToInt(OPTOKENS[i][1]);
                result = (TokenState)tokenId;
                break;
            }
        }
    }

    return  result;
}

/**
 * Determines the exact state(type) of a
 * delimiter token
 * @param token the token value to analyze
 * @return The exact delimiter type, or ERROR if not recognized
 */
TokenState getDelimTokenState(char token)
{
    string finalToken;
    TokenState result = ERROR;
    finalToken.push_back(token);


    for(int i = 0; i < DELIM_TOKEN_COUNT; i++)
    {
        if(finalToken == DELIMTOKENS[i][0])
        {
            int tokenId = stringToInt(DELIMTOKENS[i][1]);
            result = (TokenState)tokenId;
            break;
        }
    }

    return result;
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
        column = 1;
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
                    if(tokens.size() > 0)
                    {
                        //check for error
                        if(tokens.back().ID == ERROR)
                        {
                            cout << SCANNER_ERROR_MESSAGE << endl;
                            break;
                        }
                        else
                        {
                            allTokens.insert(allTokens.end(), tokens.begin(), tokens.end());
                        }
                    }




                }
            }
            //add EOF Token
            Token endToken;
            endToken.ID = EOFTOKEN;
            endToken.name = "EOFToken";
            endToken.value = "EOF";
            endToken.line = lineCount - 1;
            endToken.col = 1; //tokens.back().col + 1;
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
vector<Token> Scanner::verifyTokens(string token, int lineNumber)
{
    int tokenSize = 0;
    int tokenId;
    TokenState state = START;
    char nextChar;
    Token nextToken  = Token();
    vector<Token> tokens;

    while(state != FINAL & state != ERROR)
    {
        if(tokenSize >= token.length())
        {
            //end the loop
            state  = FINAL;
            if(getTokenName(nextToken.ID) != "ERROR")
            {
                if(nextToken.ID == IDTOKEN && isReservedWord(nextToken.value))
                {
                    nextToken.ID = RWORD;
                }
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

            if((comments == false && nextChar == '#') ||(comments))
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
                nextToken.name = getTokenName(state);
                nextToken.line = lineCount;
                nextToken.col = column;
                nextToken.value.push_back(nextChar);
                tokenSize++;

                //check for delimiter or operator
                if(state == DELIMTOKEN)
                {
                    switch(nextChar)
                    {
                        case ':':
                            //check for optoken
                            TokenState opTokenState;
                            opTokenState = getOpTokenState(token, tokenSize - 1);
                            if(opTokenState != ERROR)
                            {
                                nextToken.ID = opTokenState;
                                nextToken.name = getTokenName(opTokenState);
                                nextToken.value.push_back(token.at(tokenSize));
                                tokenSize++;
                                column += tokenSize;
                                tokens.push_back(nextToken);
                            }
                            else{
                                nextToken.ID = COLON;
                                column += tokenSize;
                                tokens.push_back(nextToken);
                            }
                            break;
                        default: //set delimiter as normal
                            nextToken.ID = getDelimTokenState(nextChar);
                            nextToken.name = getTokenName(nextToken.ID);
                            column += tokenSize;
                            tokens.push_back(nextToken);


                    }
                    nextToken = Token();
                    state = START;
                }
                else if (state == OPTOKEN)
                {
                    nextToken.ID = getOpTokenState(token, tokenSize - 1);
                    nextToken.name = getTokenName(nextToken.ID);

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
                            tokens.push_back(nextToken);
                            state = ERROR;
                            continue;
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
                                //commit previous token
                                if(nextToken.ID != 0)
                                {
                                    if(nextToken.ID == IDTOKEN && isReservedWord(nextToken.value))
                                    {
                                        nextToken.ID = RWORD;
                                    }
                                    tokens.push_back(nextToken);
                                    column += tokenSize;
                                }
                                //start new token
                                state = START;
                            }
                        }
                    }
                    else
                    {
                        state = FINAL;
                        //commit final token if any
                        if(nextToken.ID != 0)
                        {
                            if(nextToken.ID == IDTOKEN && isReservedWord(nextToken.value))
                            {
                                nextToken.ID = RWORD;
                            }
                            tokens.push_back(nextToken);
                            column += tokenSize;
                        }
                    }
                    continue;
                }
                else
                {
                    nextToken.ID = ERROR;
                    nextToken.name = getTokenName((int)ERROR);
                    tokenSize++;
                    tokens.push_back(nextToken);
                    column += tokenSize;
                    break;
                }
            }
            else //state has changed
            {
                //finalize previous token if any
                if(nextToken.ID != 0)
                {
                    if(nextToken.ID == IDTOKEN && isReservedWord(nextToken.value))
                    {
                        nextToken.ID = RWORD;
                    }
                }


                tokens.push_back(nextToken);
                column += tokenSize;
                //start new token
                state = START;
            }
        }
    }
    return tokens;
}

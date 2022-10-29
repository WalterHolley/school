/**
 * parser.cpp
 * @author Walter Holley III
 * 10/21/2022
 * Implementation of parser for the language
 */

#include <iostream>
#include "parser.h"

void Parser::parseTokens(vector<Token> fileTokens)
{
    tokens = fileTokens;
}

/**
 * Program non-terminal.  called first.
 * handles the beginning of the program
 * BNF: <vars> program <block>
 */
void Parser::program()
{
    //call <vars>
    vars();
    //get next token
    Token nextToken = getNextToken();
    //check if next token is program
    if(nextToken.value == "program")
    {
        //call block if true
        block();
    }

}
void Parser::block()
{
    Token processingToken;
    //check if next token is begin
    if(lookAhead().value == "begin")
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;
        //call vars
        vars();
        //call stats
        stats()
        //check if next token is end
        if(lookAhead().value == "end")
        {
            processingToken = getNextToken();
            cout << processingToken.value << endl;
        }
    }


}

/**
 * Processes the <vars> non-terminal
 * BNF:  empty | whole Identifier :=  Integer  ;  <vars>
 */
void Parser::vars()
{
    Token processingToken;
    if(lookAhead().ID == RWORD && lookAhead().value == "whole")
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;

        //check for identifier
        if(lookAhead().ID == IDTOKEN)
        {
            processingToken = getNextToken();
            cout << processingToken.value << endl;

            //check for comp
            if(lookAhead().ID == COMP)
            {
                processingToken = getNextToken();
                cout << processingToken.value << endl;

                //check for NUMTOKEN
                if(lookAhead().ID == NUMTOKEN)
                {
                    processingToken = getNextToken();
                    cout << processingToken.value << endl;

                    //check for semicolon
                    if(lookAhead().ID == SEMICOLON)
                    {
                        processingToken = getNextToken();
                        cout << processingToken.value << endl;
                    }
                }
            }
        }
    }
}

void Parser::expr()
{
    //call <N>
    N();
    //if next token is "-", call <expr>, otherwise done
}

void Parser::N()
{
    //call <A>
    A();
    //check for "+" or "*", run <N> if true

}

void Parser::A()
{
    //check next token for "/", call <A> if true
    //call <M>
    M();
}

void Parser::M()
{
    //check for ":" token, call <M> if true
    //otherwise call <R>
}

void Parser::R()
{
    //check for "(", call <expr> if true, then check for ")"
    //if false, check for number or identifier
}

/**
 * Handles the processing of statements
 * BNF: <stat>  <mStat>
 */
void Parser::stats()
{
    //call <stat>
    stat();
    //call <mStat>
    mStat();
}

void Parser::mStat()
{
    //if certain reserved words are the next token, call <stat>, then <mStat>
}

void Parser::stat()
{
    if(lookAhead().ID == RWORD)
    {
        string reservedWord = lookAhead().value;

        //switch case for <in>, <out>,<block>,<if>, <loop>, <assign>, <goto>, and <label>.
        switch(reservedWord)
        {
            case "input":
                in();
                break;
            case "output":
                out();
                break;
            case "begin":
                block(); //does not require check for semicolon at the end
                break;
            case "if":
                If();
                break;
            case "while":
                loop();
                break;
            case "assign":
                assign();
                break;
            case "goto":
                Goto();
                break;
            default:
                //TODO: throw an error
        }

        //check for ";" at the end
        if(lookAhead().ID == SEMICOLON && reservedWord != "block")
        {
            Token processingToken;
            processingToken = getNextToken();
            cout << processingToken.value << endl;
        }

    }


}

void Parser::in()
{
    Token processingToken;
    // check for input reserved word followed by an IDToken
    if(lookAhead().value == "input")
    {

        processingToken = getNextToken();
        cout << processingToken.value << endl;

        if(lookAhead().ID == IDTOKEN)
        {
            processingToken = getNextToken();
            cout << processingToken.value << endl;
        }
        //TODO: throw error
    }
    //TODO: Throw error
}

void Parser::out()
{
    //check for output reserved work
    //if true, call <expr>
}

void Parser::If()
{
    //check for if reserved word
    //check for "["
    //call <expr>
    //check for "]"
    //check for  "then" reserved word
    //call <stat>
    //check for "pick" reserved word, call <stat> if true

}

void Parser::loop()
{
    //check for "while"
    //check for "["(required), call <expr><RO><expr> if true
    //check for "]"(required), call <stat> if true
}

void Parser::assign()
{
    //check for "assign"
    //check for IDToken
    //check for "=", call <expr> if true
}

void Parser::RO()
{
    //check for following operators, fail if none exist:
    // >, <, ==, [=](three tokens), !=
}

void Parser::label()
{
    //check for label rword
    //check for ID token if true
}

void Parser::Goto()
{
    //check for warp rword
    //check for IDtoken if true
}

/**
 * Retrieves the next token to be reviewed,
 * and removes it from the token collection.
 * @return Token to be viewed, or ERROR token if
 * no more tokens remain
 */
Token Parser::getNextToken()
{
    Token result;
    if(tokens.size() > 0)
    {
        result = tokens.front();
        tokens.erase(tokens.begin());
    }
    else
    {
        result.ID = ERROR;
        result.name = "ERROR";
    }

    return result;
}

/**
 * Reveals the next token in the order,
 * but does not remove it from the collection.
 * @return the next available token, or an ERROR
 * token if no tokens remain
 */
Token Parser::lookAhead()
{
    Token result;
    if(tokens.size() > 0)
    {
        result = tokens.front();
    }
    else
    {
        result.ID = ERROR;
        result.name = "ERROR";
    }

    return result;
}
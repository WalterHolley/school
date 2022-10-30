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
    program();
}

/**
 * Program non-terminal.  called first.
 * handles the beginning of the program
 * BNF: <vars> program <block>
 */
void Parser::program()
{
    Token processingToken;
    //call <vars>
    vars();
    //get next token

    //check if next token is program
    if(lookAhead().value == "program")
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;
        //call block if true
        block();
    }
    //TODO: throw error

}

/**
 * Processes the <block> non-terminal
 * BNF:  begin <vars> <stats> end
 */
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
        stats();
        //check if next token is end
        if(lookAhead().value == "end")
        {
            processingToken = getNextToken();
            cout << processingToken.value << endl;
        }
        else
        {
            //TODO: throw error
        }
    }
    else
    {
        //TODO: throw error
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

/**
 * Processes the <expr> non-terminal
 * BNF:  <N> - <expr>  | <N>
 */
void Parser::expr()
{
    //call <N>
    N();
    //if next token is "-", call <expr>, otherwise done
    if(lookAhead().ID == SUB)
    {
        Token processingToken;
        processingToken = getNextToken();
        cout << processingToken.value << endl;
        expr();
    }
}

/**
 * Processes the <N> non-terminal
 * BNF:  <A> + <N> | <A> * <N> | <A>
 */
void Parser::N()
{
    Token processingToken;
    //call <A>
    A();
    //check for "+" or "*", run <N> if true
    switch(lookAhead().ID)
    {
        case ADD:
        case MUL:
            processingToken = getNextToken();
            cout << processingToken.value << endl;
            N();
        default:
            break;
    }

}

/**
 * Processes the <A> non-terminal
 * BNF:  <A> / <M> | <M>
 */
void Parser::A()
{
    Token processingToken;
    //call <M>
    M();
    //check next token for "/", call <A> if true
    if(lookAhead().ID == DIV)
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;
        A();

    }

}

/**
 * Processes the <M> non-terminal
 * : <M> |  <R>
 */
void Parser::M()
{
    Token processingToken;
    //check for ":" token, call <M> if true
    if(lookAhead().ID == COLON)
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;
        M();
    }
    else //otherwise call <R>
    {
        R();
    }

}

/**
 * Processes the <R> non-terminal
 * BNF:  ( <expr> ) | Identifier | Integer
 */
void Parser::R()
{
    Token processingToken;
    //check for "(", call <expr> if true, then check for ")"
    if(lookAhead().ID == LPAREN)
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;
        expr();

        if(lookAhead().ID == RPAREN)
        {
            processingToken = getNextToken();
            cout << processingToken.value << endl;
        }
        else //Error
        {
            //TODO: Throw error
        }

    }    //if false, check for number or identifier
    else if(lookAhead().ID == IDTOKEN || lookAhead().ID == NUMTOKEN)
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;
    }
    else //Error
    {
        //TODO: Throw Error
    }
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

/**
 * Processes the <mStat> non-terminal
 * BNF:   empty |  <stat>  <mStat>
 */
void Parser::mStat()
{
    //if reserved words are the next token, call <stat>, then <mStat>
    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) != END)
    {
        stat();
        mStat();
    }
    else if(getReservedWord(lookAhead().value) != END)
    {
        //TODO: Throw Error
    }


}

/**
 * Processes non-terminal for <stat>
 * BNF:  <in> ;  | <out> ;  | <block> | <if> ;  | <loop> ;  | <assign> ; | <goto> ; |
 * <label> ;
 */
void Parser::stat()
{
    if(lookAhead().ID == RWORD)
    {
        ReservedWords reservedWord = getReservedWord(lookAhead().value);

        //switch case for <in>, <out>,<block>,<if>, <loop>, <assign>, <goto>, and <label>.
        switch(reservedWord)
        {
            case INPUT:
                in();
                break;
            case OUTPUT:
                out();
                break;
            case BEGIN:
                block(); //does not require check for semicolon at the end
                break;
            case IF:
                If();
                break;
            case WHILE:
                loop();
                break;
            case ASSIGN:
                assign();
                break;
            case WARP:
                Goto();
                break;
            default:
                break;
                //TODO: throw an error
        }

        if(reservedWord != BEGIN)
        {
            //check for ";" at the end
            if(lookAhead().ID == SEMICOLON && reservedWord != BEGIN)
            {
                Token processingToken;
                processingToken = getNextToken();
                cout << processingToken.value << endl;
            }
            else
            {
                //TODO: Throw exception
            }
        }
    }
    else
    {
        //TODO: Throw exception
    }



}

/**
 * Processes the <in> non-terminal
 * BNF:  input  Identifier
 */
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

/**
 * Processes the <out> non-terminal
 * BNF:  output <expr>
 */
void Parser::out()
{
    Token processingToken;

    //check for output reserved word
    if(lookAhead().ID == RWORD && lookAhead().value == RESERVED_WORDS[OUTPUT])
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;
        //call <expr>
        expr();
    }
    else //Error
    {
        //TODO: throw error
    }


}

/**
 * Processes the <if> non-terminal
 * BNF:  if [ <expr> <RO> <expr> ] then <stat> |
 * if [ <expr> <RO> <expr> ] then <stat> pick <stat>
 */
void Parser::If()
{
    Token processingToken;
    //check for if reserved word
    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == IF)
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;

        //check for "["
        if(lookAhead().ID == LBRACKET)
        {
            processingToken = getNextToken();
            cout << processingToken.value << endl;

            //call <expr>
            expr();
            //check for "]"
            if(lookAhead().ID == RBRACKET)
            {
                processingToken = getNextToken();
                cout << processingToken.value << endl;

                //check for  "then" reserved word
                if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == THEN)
                {
                    processingToken = getNextToken();
                    cout << processingToken.value << endl;
                    //call <stat>
                    stat();
                    //check for "pick" reserved word(optional), call <stat> if true
                    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == PICK)
                    {
                        processingToken = getNextToken();
                        cout << processingToken.value << endl;
                        stat();
                    }
                }
                else
                {
                    //TODO: throw error
                }


            }
            else
            {
                //TODO: throw error
            }
        }
        else
        {
            //TODO: throw error
        }
    }
    else
    {
        //TODO: throw error
    }



}

/**
 * Processes the <loop> non-terminal
 * BNF:  while  [ <expr> <RO> <expr> ]  <stat>
 */
void Parser::loop()
{
    Token processingToken;

    //check for "while"
    if(lookAhead().ID == RWORD && lookAhead().value == "while")
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;

        //check for "["(required), call <expr><RO><expr> if true
        if(lookAhead().ID == LBRACKET)
        {
            processingToken = getNextToken();
            cout << processingToken.value << endl;
            expr();
            RO();
            expr();

            //check for "]"(required), call <stat> if true
            if(lookAhead().ID == RBRACKET)
            {
                processingToken = getNextToken();
                cout << processingToken.value << endl;
                stat();
            }
            else
            {
                //TODO: throw error
            }
        }
        else
        {
            //TODO: throw error
        }
    }
    else
    {
        //TODO: Throw Error
    }


}

/**
 * Processes the <assign> non-terminal
 * BNF:  assign Identifier  = <expr>
 */
void Parser::assign()
{
    Token processingToken;

    //check for "assign"
    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == ASSIGN)
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;

        //check for IDToken
        if(lookAhead().ID == IDTOKEN)
        {
            processingToken = getNextToken();
            cout << processingToken.value << endl;

            //check for "=", call <expr> if true
            if(lookAhead().ID == ASSN)
            {
                processingToken = getNextToken();
                cout << processingToken.value << endl;
                expr();
            }
            else
            {
                //TODO: throw error
            }
        }
        else
        {
            //TODO: throw error
        }
    }
    else
    {
        //TODO: throw error
    }


}

/**
 * Processes the <RO> non-terminal
 * BNF:    >  | < |  ==  |   [ = ]  (three tokens)  | !=
 */
void Parser::RO()
{
    Token processingToken;
    //check for following operators, fail if none exist:
    // >, <, ==, [=](three tokens), !=
    switch(lookAhead().ID)
    {
        case GT:
        case LT:
        case EQ:
        case NEQ:
            processingToken = getNextToken();
            cout << processingToken.value << endl;
            break;
        case LBRACKET:
            processingToken = getNextToken();
            cout << processingToken.value << endl;
            if(lookAhead().ID == ASSN)
            {
                processingToken = getNextToken();
                cout << processingToken.value << endl;

                if(lookAhead().ID == RBRACKET)
                {
                    processingToken = getNextToken();
                    cout << processingToken.value << endl;
                }
                else
                {
                    //TODO: Throw error
                }
            }
            else
            {
                //TODO: Throw Error
            }
            break;
        default:
            //TODO: throw error
    }
}

/**
 * Processes the <label> non-terminal
 * BNF:  label Identifier
 */
void Parser::label()
{
    Token processingToken;
    //check for label rword
    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == LABEL)
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;
        //check for ID token if true
        if(lookAhead().ID == IDTOKEN)
        {
            processingToken = getNextToken();
            cout << processingToken.value << endl;
        }
        else
        {
            //TODO: throw error
        }
    }
    else
    {
        //TODO: throw error
    }

}

void Parser::Goto()
{
    Token processingToken;
    //check for warp rword
    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == WARP)
    {
        processingToken = getNextToken();
        cout << processingToken.value << endl;
        //check for ID token if true
        if(lookAhead().ID == IDTOKEN)
        {
            processingToken = getNextToken();
            cout << processingToken.value << endl;
        }
        else
        {
            //TODO: throw error
        }
    }
    else
    {
        //TODO: throw error
    }
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

/**
 * Find the enumerator value for a reserved word
 * @param rWordValue The string to search for reserved words
 * @return enum of the reserved word
 */
ReservedWords Parser::getReservedWord(string rWordValue)
{
    ReservedWords rWordEnum;
    for(int i = 0; i < MAX_RWORDS; i++)
    {
        if(rWordValue == RESERVED_WORDS[i])
        {
            rWordEnum = (ReservedWords)i;
            break;
        }
    }
    return rWordEnum;
}
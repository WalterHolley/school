/**
 * parser.cpp
 * @author Walter Holley III
 * 10/21/2022
 * Implementation of parser for the language
 */

#include <stdexcept>
#include <sstream>
#include "parser.h"

/**
 * Converts an integer to a string
 * @param number the integer to convert
 * @return the resulting string from the conversion
 */
string intToString(int number)
{
    stringstream  ss;
    string result;
    ss << number;
    ss >> result;

    return  result;
}

/**
 * Creates an error message string from an offending
 * token
 * @param token The token at or near the source of the error
 * @return formatted string detailing the error
 */
string Parser::createErrorMessage(Token token)
{
    string errorMessage = "Invalid syntax '" + token.value + "' near line: " + intToString(token.line) + " column: " + intToString(token.col);

    return errorMessage;
}

ParserNode* Parser::parseTokens(vector<Token> fileTokens)
{
    tokens = fileTokens;
    return program();
}

/**
 * Program non-terminal.  called first.
 * handles the beginning of the program
 * BNF: <vars> program <block>
 */
ParserNode* Parser::program()
{
    ParserNode* processingNode;
    ParserNode* programNode;
    //call <vars>
    processingNode = vars();

    programNode = new ParserNode;
    programNode->nonTerminal = TERMPROGRAM;
    if(processingNode != NULL)
    {
        programNode->children.push_back(processingNode);
    }

    //check if next token is program
    if(lookAhead().value == "program")
    {
        processingNode = createTokenNode(getNextToken(), programNode);
        programNode->children.push_back(processingNode);
        processingNode = block();
        programNode->children.push_back(processingNode);

        if(lookAhead().ID != EOFTOKEN)
        {
            throw std::invalid_argument(createErrorMessage(lookAhead()));
        }
        else //clear final token
        {
            processingNode = createTokenNode(getNextToken(), programNode);
            programNode->children.push_back(processingNode);
        }

    }
    else
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return programNode;
}

/**
 * Processes the <block> non-terminal
 * BNF:  begin <vars> <stats> end
 */
ParserNode* Parser::block()
{
    ParserNode* blockNode = NULL;
    ParserNode* processingNode;


    //check if next token is begin
    if(lookAhead().value == "begin")
    {
        blockNode = new ParserNode;
        blockNode->nonTerminal = BLOCK;
        processingNode = createTokenNode(getNextToken(), blockNode);
        blockNode->children.push_back(processingNode);

        //call vars. It's optional. so check for NULL
        processingNode = vars();
        if(processingNode != NULL)
        {
            blockNode->children.push_back(processingNode);
        }
        //call stats
        processingNode = stats();
        blockNode->children.push_back(processingNode);
        //check if next token is end
        if(lookAhead().value == "end")
        {
            processingNode = createTokenNode(getNextToken(), blockNode);
            blockNode->children.push_back(processingNode);
        }
        else
        {
            throw std::invalid_argument(createErrorMessage(lookAhead()));
        }
    }
    else
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return  blockNode;

}

/**
 * Processes the <vars> non-terminal
 * BNF:  empty | whole Identifier :=  Integer  ;  <vars>
 * @returns NULL if no data
 */
ParserNode* Parser::vars()
{
    ParserNode* varsNode = NULL;
    ParserNode* processingNode;
    if(lookAhead().ID == RWORD && lookAhead().value == "whole")
    {
        varsNode = new ParserNode;
        varsNode->nonTerminal = VARS;
        processingNode = createTokenNode(getNextToken(), varsNode);

        varsNode->children.push_back(processingNode);

        //check for identifier
        if(lookAhead().ID == IDTOKEN)
        {
            processingNode = createTokenNode(getNextToken(), varsNode);

            varsNode->children.push_back(processingNode);

            //check for comp
            if(lookAhead().ID == COMP)
            {
                processingNode = createTokenNode(getNextToken(), varsNode);

                varsNode->children.push_back(processingNode);

                //check for NUMTOKEN
                if(lookAhead().ID == NUMTOKEN)
                {
                    processingNode = createTokenNode(getNextToken(), varsNode);
;
                    varsNode->children.push_back(processingNode);

                    //check for semicolon
                    if(lookAhead().ID == SEMICOLON)
                    {
                        processingNode = createTokenNode(getNextToken(), varsNode);

                        varsNode->children.push_back(processingNode);

                        //check for next <vars> node
                        varsNode->children.push_back(vars());
                    }
                    else
                    {
                        throw std::invalid_argument(createErrorMessage(lookAhead()));
                    }
                }
                else
                {
                    throw std::invalid_argument(createErrorMessage(lookAhead()));
                }
            }
            else
            {
                throw std::invalid_argument(createErrorMessage(lookAhead()));
            }
        }
        else
        {
            throw std::invalid_argument(createErrorMessage(lookAhead()));
        }
    }

    return varsNode;
}

/**
 * Processes the <expr> non-terminal
 * BNF:  <N> - <expr>  | <N>
 */
ParserNode* Parser::expr()
{
    ParserNode* exprNode;
    ParserNode* processingNode;

    exprNode = new ParserNode;
    exprNode->nonTerminal = EXPR;
    //call <N>
    processingNode = N();
    exprNode->children.push_back(processingNode);


    //if next token is "-", call <expr>, otherwise done
    if(lookAhead().ID == SUB)
    {
        processingNode = createTokenNode(getNextToken(), exprNode);
        exprNode->children.push_back(processingNode);
        processingNode = expr();
        exprNode->children.push_back(processingNode);
    }

    return exprNode;
}

/**
 * Processes the <N> non-terminal
 * BNF:  <A> + <N> | <A> * <N> | <A>
 */
ParserNode* Parser::N()
{
    ParserNode* nNode;
    ParserNode* processingNode;

    nNode = new ParserNode;
    nNode->nonTerminal = TERMN;
    //call <A>
    processingNode = A();
    nNode->children.push_back(processingNode);
    //check for "+" or "*", run <N> if true
    switch(lookAhead().ID)
    {
        case ADD:
        case MUL:
            processingNode = createTokenNode(getNextToken(), nNode);
            nNode->children.push_back(processingNode);
            processingNode = N();
            nNode->children.push_back(processingNode);
        default:
            break;
    }

    return nNode;
}

/**
 * Processes the <A> non-terminal
 * Original BNF:  <A> / <M> | <M>
 * Fixed BNF: <M><B> (left recursion removed)
 */
ParserNode* Parser::A()
{
    ParserNode* aNode;
    ParserNode* processingNode;

    aNode = new ParserNode;
    aNode->nonTerminal = TERMA;
    //call <M>
    processingNode = M();
    aNode->children.push_back(processingNode);

    processingNode = B();
    if(processingNode != NULL)
    {
        aNode->children.push_back(processingNode);
    }


    return  aNode;
}

/**
 * Processes the <B> non-terminal
 * This is an optional non-terminal
 * BNF: /<M><B>|empty
 * @returns NULL if there's no data to parse
 */
ParserNode* Parser::B()
{
    ParserNode* bNode = NULL;
    ParserNode* processingNode;

    //check next token for "/", call <M><B> if true
    if(lookAhead().ID == DIV)
    {
        bNode = new ParserNode;
        bNode->nonTerminal = TERMB;
        processingNode = createTokenNode(getNextToken(), bNode);
        bNode->children.push_back(processingNode);
        bNode->children.push_back(M());

        //<B> is optional. check for NULL
        processingNode = B();
        if(processingNode != NULL)
        {
            bNode->children.push_back(processingNode);
        }

    }

    return bNode;
}

/**
 * Processes the <M> non-terminal
 * : <M> |  <R>
 */
ParserNode* Parser::M()
{
    ParserNode* mNode;
    ParserNode* processingNode;

    mNode = new ParserNode;
    mNode->nonTerminal = TERMM;
    //check for ":" token, call <M> if true
    if(lookAhead().ID == COLON)
    {
        processingNode = createTokenNode(getNextToken(), mNode);
        mNode->children.push_back(processingNode);
        processingNode = M();
        mNode->children.push_back(processingNode);
    }
    else //otherwise call <R>
    {
        mNode->children.push_back(R());
    }

    return mNode;

}

/**
 * Processes the <R> non-terminal
 * BNF:  ( <expr> ) | Identifier | Integer
 */
ParserNode* Parser::R()
{
    ParserNode* rNode;
    ParserNode* processingNode;
    TerminalEnum nonTerminal = TERMR;

    //check for "(", call <expr> if true, then check for ")"
    if(lookAhead().ID == LPAREN)
    {
        rNode = new ParserNode;
        rNode->nonTerminal = nonTerminal;
        processingNode = createTokenNode(getNextToken(), rNode);
        rNode->children.push_back(processingNode);

        processingNode = expr();
        rNode->children.push_back(processingNode);

        if(lookAhead().ID == RPAREN)
        {
            processingNode = createTokenNode(getNextToken(), rNode);
            rNode->children.push_back(processingNode);
        }
        else //Error
        {
            throw std::invalid_argument(createErrorMessage(lookAhead()));
        }

    }    //if false, check for number or identifier
    else if(lookAhead().ID == IDTOKEN || lookAhead().ID == NUMTOKEN)
    {
        rNode = new ParserNode;
        rNode->nonTerminal = nonTerminal;
        processingNode = createTokenNode(getNextToken(), rNode);
        rNode->children.push_back(processingNode);
    }
    else //Error
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return rNode;
}

/**
 * Handles the processing of statements
 * BNF: <stat>  <mStat>
 */
ParserNode* Parser::stats()
{
    ParserNode* statsNode;
    ParserNode* processingNode;

    statsNode = new ParserNode;
    statsNode->nonTerminal = STATS;
    //call <stat>
    processingNode = stat();
    statsNode->children.push_back(processingNode);
    //call <mStat>
    processingNode = mStat();

    //mstat node is optional.  Check for null
    if(processingNode != NULL)
    {
        statsNode->children.push_back(processingNode);
    }

    return statsNode;
}

/**
 * Processes the <mStat> non-terminal
 * BNF:   empty |  <stat>  <mStat>
 */
ParserNode* Parser::mStat()
{
    ParserNode* mStatsNode = NULL;
    ParserNode* processingNode;

    //if reserved words are the next token, call <stat>, then <mStat>
    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) != END)
    {
        mStatsNode = new ParserNode;
        mStatsNode->nonTerminal = MSTAT;
        processingNode = stat();
        mStatsNode->children.push_back(processingNode);

        processingNode = mStat();
        //mstat is optional.  check for null
        if(processingNode != NULL)
        {
            mStatsNode->children.push_back(processingNode);
        }
    }
    else if(getReservedWord(lookAhead().value) != END)
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return mStatsNode;
}

/**
 * Processes non-terminal for <stat>
 * BNF:  <in> ;  | <out> ;  | <block> | <if> ;  | <loop> ;  | <assign> ; | <goto> ; |
 * <label> ;
 */
ParserNode* Parser::stat()
{
    ParserNode* statNode;
    ParserNode* processingNode;
    if(lookAhead().ID == RWORD)
    {
        statNode = new ParserNode;
        statNode->nonTerminal = STAT;
        ReservedWords reservedWord = getReservedWord(lookAhead().value);

        //switch case for <in>, <out>,<block>,<if>, <loop>, <assign>, <goto>, and <label>.
        switch(reservedWord)
        {
            case INPUT:
                processingNode = in();
                break;
            case OUTPUT:
                processingNode = out();
                break;
            case BEGIN:
                processingNode = block(); //does not require check for semicolon at the end
                break;
            case IF:
                processingNode = If();
                break;
            case WHILE:
                processingNode = loop();
                break;
            case ASSIGN:
                processingNode = assign();
                break;
            case WARP:
                processingNode = Goto();
                break;
            default:
                throw std::invalid_argument(createErrorMessage(lookAhead()));
        }

        statNode->children.push_back(processingNode);

        if(reservedWord != BEGIN)
        {
            //check for ";" at the end
            if(lookAhead().ID == SEMICOLON && reservedWord != BEGIN)
            {
                processingNode = createTokenNode(getNextToken(), statNode);
                statNode->children.push_back(processingNode);
            }
            else
            {
                throw std::invalid_argument(createErrorMessage(lookAhead()));
            }
        }
    }
    else
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return statNode;

}

/**
 * Processes the <in> non-terminal
 * BNF:  input  Identifier
 */
ParserNode* Parser::in()
{
    ParserNode* inputNode;
    ParserNode* processingNode;

    // check for input reserved word followed by an IDToken
    if(lookAhead().value == "input")
    {
        inputNode = new ParserNode;
        inputNode->nonTerminal = IN;

        processingNode = createTokenNode(getNextToken(), inputNode);
        inputNode->children.push_back(processingNode);


        if(lookAhead().ID == IDTOKEN)
        {
            processingNode = createTokenNode(getNextToken(), inputNode);
            inputNode->children.push_back(processingNode);
        }
        else
        {
            throw std::invalid_argument(createErrorMessage(lookAhead()));
        }

    }
    else
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return inputNode;

}

/**
 * Processes the <out> non-terminal
 * BNF:  output <expr>
 */
ParserNode* Parser::out()
{
    ParserNode* outputNode;
    ParserNode* processingNode;

    //check for output reserved word
    if(lookAhead().ID == RWORD && lookAhead().value == RESERVED_WORDS[OUTPUT])
    {
        outputNode = new ParserNode;
        outputNode->nonTerminal = OUT;
        processingNode = createTokenNode(getNextToken(), outputNode);
        outputNode->children.push_back(processingNode);

        //call <expr>
        processingNode = expr();
        outputNode->children.push_back(processingNode);


    }
    else //Error
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return outputNode;
}

/**
 * Processes the <if> non-terminal
 * BNF:  if [ <expr> <RO> <expr> ] then <stat> |
 * if [ <expr> <RO> <expr> ] then <stat> pick <stat>
 */
ParserNode* Parser::If()
{
    ParserNode* ifNode;
    ParserNode* processingNode;

    //check for if reserved word
    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == IF)
    {
        ifNode = new ParserNode;
        ifNode->nonTerminal = TERMIF;

        processingNode = createTokenNode(getNextToken(), ifNode);
        ifNode->children.push_back(processingNode);

        //check for "["
        if(lookAhead().ID == LBRACKET)
        {
            processingNode = createTokenNode(getNextToken(), ifNode);
            ifNode->children.push_back(processingNode);

            //call <expr>
            ifNode->children.push_back(expr());

            //call<RO>
            ifNode->children.push_back(RO());

            //call<expr>
            ifNode->children.push_back(expr());

            //check for "]"
            if(lookAhead().ID == RBRACKET)
            {
                processingNode = createTokenNode(getNextToken(), ifNode);
                ifNode->children.push_back(processingNode);

                //check for  "then" reserved word
                if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == THEN)
                {
                    processingNode = createTokenNode(getNextToken(), ifNode);
                    ifNode->children.push_back(processingNode);
                    //call <stat>
                    ifNode->children.push_back(stat());
                    //check for "pick" reserved word(optional), call <stat> if true
                    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == PICK)
                    {
                        processingNode = createTokenNode(getNextToken(), ifNode);
                        ifNode->children.push_back(processingNode);
                        ifNode->children.push_back(stat());
                    }
                }
                else
                {
                    throw std::invalid_argument(createErrorMessage(lookAhead()));
                }


            }
            else
            {
                throw std::invalid_argument(createErrorMessage(lookAhead()));
            }
        }
        else
        {
            throw std::invalid_argument(createErrorMessage(lookAhead()));
        }
    }
    else
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return ifNode;

}

/**
 * Processes the <loop> non-terminal
 * BNF:  while  [ <expr> <RO> <expr> ]  <stat>
 */
ParserNode* Parser::loop()
{
    Token processingToken;
    ParserNode* loopNode;
    ParserNode* processingNode;

    //check for "while"
    if(lookAhead().ID == RWORD && lookAhead().value == "while")
    {
        loopNode = new ParserNode;
        loopNode->nonTerminal = LOOP;
        processingNode = createTokenNode(getNextToken(), loopNode);
        loopNode->children.push_back(processingNode);

        //check for "["(required), call <expr><RO><expr> if true
        if(lookAhead().ID == LBRACKET)
        {
            processingNode = createTokenNode(getNextToken(), loopNode);
            loopNode->children.push_back(processingNode);

            loopNode->children.push_back(expr());
            loopNode->children.push_back(RO());
            loopNode->children.push_back(expr());

            //check for "]"(required), call <stat> if true
            if(lookAhead().ID == RBRACKET)
            {
                processingNode = createTokenNode(getNextToken(), loopNode);
                loopNode->children.push_back(processingNode);

                loopNode->children.push_back(stat());
            }
            else
            {
                throw std::invalid_argument(createErrorMessage(lookAhead()));
            }
        }
        else
        {
            throw std::invalid_argument(createErrorMessage(lookAhead()));
        }
    }
    else
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return loopNode;
}

/**
 * Processes the <assign> non-terminal
 * BNF:  assign Identifier  = <expr>
 */
ParserNode* Parser::assign()
{
    ParserNode* assignNode;
    ParserNode* processingNode;

    //check for "assign"
    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == ASSIGN)
    {
        assignNode = new ParserNode;
        assignNode->nonTerminal = TERMASSIGN;

        processingNode = createTokenNode(getNextToken(), assignNode);
        assignNode->children.push_back(processingNode);

        //check for IDToken
        if(lookAhead().ID == IDTOKEN)
        {
            processingNode = createTokenNode(getNextToken(), assignNode);
            assignNode->children.push_back(processingNode);

            //check for "=", call <expr> if true
            if(lookAhead().ID == ASSN)
            {
                processingNode = createTokenNode(getNextToken(), assignNode);
                assignNode->children.push_back(processingNode);
                assignNode->children.push_back(expr());
            }
            else
            {
                throw std::invalid_argument(createErrorMessage(lookAhead()));
            }
        }
        else
        {
            throw std::invalid_argument(createErrorMessage(lookAhead()));
        }
    }
    else
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return assignNode;

}

/**
 * Processes the <RO> non-terminal
 * BNF:    >  | < |  ==  |   [ = ]  (three tokens)  | !=
 */
ParserNode* Parser::RO()
{
    ParserNode* roNode;
    ParserNode* processingNode;
    TerminalEnum nonTerminalName = TERMRO;
    //check for following operators, fail if none exist:
    // >, <, ==, [=](three tokens), !=
    switch(lookAhead().ID)
    {
        case GT:
        case LT:
        case EQ:
        case NEQ:
            roNode = new ParserNode;
            roNode->nonTerminal = nonTerminalName;
            processingNode = createTokenNode(getNextToken(), roNode);
            roNode->children.push_back(processingNode);
            break;
        case LBRACKET:
            roNode = new ParserNode;
            roNode->nonTerminal = nonTerminalName;
            processingNode = createTokenNode(getNextToken(), roNode);
            roNode->children.push_back(processingNode);
            if(lookAhead().ID == ASSN)
            {
                processingNode = createTokenNode(getNextToken(), roNode);
                roNode->children.push_back(processingNode);

                if(lookAhead().ID == RBRACKET)
                {
                    processingNode = createTokenNode(getNextToken(), roNode);
                    roNode->children.push_back(processingNode);
                }
                else
                {
                    throw std::invalid_argument(createErrorMessage(lookAhead()));
                }
            }
            else
            {
                throw std::invalid_argument(createErrorMessage(lookAhead()));
            }
            break;
        default:
            throw std::invalid_argument(createErrorMessage(lookAhead()));
    }
    return roNode;
}

/**
 * Processes the <label> non-terminal
 * BNF:  label Identifier
 */
ParserNode* Parser::label()
{
    ParserNode* labelNode;
    ParserNode* processingNode;

    //check for label rword
    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == LABEL)
    {
        labelNode = new ParserNode;
        labelNode->nonTerminal = TERMLABEL;

        processingNode = createTokenNode(getNextToken(), labelNode);
        labelNode->children.push_back(processingNode);
        //check for ID token if true
        if(lookAhead().ID == IDTOKEN)
        {
            processingNode = createTokenNode(getNextToken(), labelNode);
            labelNode->children.push_back(processingNode);
        }
        else
        {
            throw std::invalid_argument(createErrorMessage(lookAhead()));
        }
    }
    else
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return labelNode;

}

/**
 * Processes the <goto> non-terminal
 * BNF:  warp Identifier
 * @return
 */
ParserNode* Parser::Goto()
{
    ParserNode* gotoNode;
    ParserNode* processingNode;
    //check for warp rword
    if(lookAhead().ID == RWORD && getReservedWord(lookAhead().value) == WARP)
    {
        gotoNode = new ParserNode;
        gotoNode->nonTerminal = TERMGOTO;
        processingNode = createTokenNode(getNextToken(), gotoNode);
        gotoNode->children.push_back(processingNode);
        //check for ID token if true
        if(lookAhead().ID == IDTOKEN)
        {
            processingNode = createTokenNode(getNextToken(), gotoNode);
            gotoNode->children.push_back(processingNode);
        }
        else
        {
            throw std::invalid_argument(createErrorMessage(lookAhead()));
        }
    }
    else
    {
        throw std::invalid_argument(createErrorMessage(lookAhead()));
    }

    return gotoNode;
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

/**
 * Creates a parser node with a scanner token
 * @param nodeToken The token for the parser node
 * @return parser node
 */
ParserNode* Parser::createTokenNode(Token nodeToken, ParserNode* parent)
{
    ParserNode* result;
    result = new ParserNode;
    result->value = nodeToken;
    result->nonTerminal = parent->nonTerminal;
    result->parentNode = parent;

    return result;
}
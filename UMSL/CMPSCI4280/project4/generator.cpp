/**
 * generator.cpp
 * Implementation of generator header
 * Converts toy source code to ACC Assembler code
 */

#include <stdexcept>
#include <iostream>
#include <sstream>
#include "generator.h"
#include "semantics.h"


int varScope = 0;
Semantics semantics;
int tempCount = 0;
bool ioUsed = false;
bool tempUsed = false;

const string IO_VAR = "W";

/**
 * Converts an integer to a string
 * @param number the integer to convert
 * @return the resulting string from the conversion
 */
string convertIntToString(int number)
{
    stringstream  ss;
    string result;
    ss << number;
    ss >> result;

    return  result;
}

/**
 * Retrieves the stack value for an IDTOKEN, or the
 * numeric value for a NUMTOKEN
 * @param token
 * @return
 */
string getTokenValue(Token token)
{
    string result;
    if(token.ID == IDTOKEN)
    {
        int stackPos = semantics.find(token.value);
        if(stackPos == -1)
        {
            //throw error
        }
        else
        {
            result = convertIntToString(stackPos);
        }
    }
    else if(token.ID == NUMTOKEN)
    {
        result = token.value;
    }
    else
    {
        //throw error
    }

    return result;
}

/**
 * Writes a math operation in the ASM language
 * @param outputFile file to write to
 * @param leftValue Left side value of the operation
 * @param rightValue right side value of the operation
 * @param operation Math function to perform(supports ADD, SUB, MUL and DIV)
 */
void writeMathOperation(FILE* outputFile, Token leftValue, Token rightValue, TokenState operation)
{
    string op;
    //get RHS Value
    switch(rightValue.ID)
    {

        case IDTOKEN:
            fprintf(outputFile, "STACKR %s\n", getTokenValue(rightValue).c_str());
            break;
        case NUMTOKEN:
            fprintf(outputFile, "LOAD %s\n", getTokenValue(rightValue).c_str());
            break;
        default:
            break;
            //throw error
    }

    //store RHS value
    fprintf(outputFile, "STORE T0\n"); //always uses temp zero for math operations

    //load LHS value
    switch(leftValue.ID)
    {
        case IDTOKEN:
            fprintf(outputFile, "STACKR %s\n", getTokenValue(leftValue).c_str());
            break;
        case NUMTOKEN:
            fprintf(outputFile, "LOAD %s\n", getTokenValue(leftValue).c_str());
            break;
        default:
            break;
            //throw error

    }

    //get operation
    switch(operation)
    {
        case ADD:
            op = "ADD";
            break;
        case SUB:
            op = "SUB";
            break;
        case MUL:
            op = "MUL";
            break;
        case DIV:
            op = "DIV";
            break;
        default:
            //throw error
            break;
    }

    //do operation
    fprintf(outputFile, "%s T0\n", op.c_str());
}

void writeEndOfDocument(FILE* outputFile)
{
    fprintf(outputFile, "STOP\n");

    //User I/O
    if(ioUsed)
    {
        fprintf(outputFile, "W 0\n");
    }
}

Token Generator::handleR(ParserNode* node, FILE* outputFile)
{
    Token result;
    if(node->children.empty())
    {
            //throw error
    }
    else
    {
        for(int i= 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            if(child->nonTerminal == EXPR)
            {
                result = handleExpr(child, outputFile);
                continue;
            }
            else
            {
                switch(child->value.ID)
                {
                    case LPAREN:
                        break;
                    case RPAREN:
                        break;
                    case IDTOKEN:
                    case NUMTOKEN:
                        result = child->value;
                        break;
                    default:
                        //throw error
                        break;
                }
            }
        }
    }

    if(result.ID != IDTOKEN || result.ID != NUMTOKEN)
    {
        //throw error;
    }

    return result;
}



Token Generator::handleM(ParserNode* node, FILE* outputFile)
{
    Token result;

    if(node->children.empty())
    {
        //throw error
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            switch(child->value.ID)
            {
                case COLON:
                    //syntax for negation
                    i++;
                    result = handleM(node->children.at(i), outputFile);
                    break;
                default:
                    result = handleR(child, outputFile);
            }
        }
    }
    return result;
}

Token Generator::handleA(ParserNode* node, FILE* outputFile)
{
    Token result;
    if(node->children.empty())
    {
        //throw error
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            switch(child->nonTerminal)
            {
                case TERMM://handle M
                    result = handleM(child, outputFile);
                    break;
                case TERMB://handle B
                    //handle B
                    break;
            }
        }
    }



    return result;
}

/**
 * Iterates through branches of <N> non-terminal
 * @param node
 * @param outputFile
 * @return variable or value of result
 */
Token Generator::handleN(ParserNode* node, FILE* outputFile)
{
    Token result;
    if(node->children.size() == 0)
    {
        //throw error
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            switch(child->nonTerminal)
            {
                case TERMA:
                    result = handleA(child, outputFile);
                    break;
                default:
                    if(child->value.ID == ADD || child->value.ID == MUL)
                    {
                        i++;
                        if(result.ID != IDTOKEN || result.ID != NUMTOKEN)
                        {
                            Token nValue = handleN(node->children.at(i), outputFile);
                            writeMathOperation(outputFile, result, nValue, child->value.ID);
                        }
                        else
                        {
                            //throw error
                        }
                    }
                    else
                    {
                        //throw error
                    }
            }
        }
    }


    return result;
}

Token Generator::handleB(ParserNode* node, FILE* outputFile)
{
    Token result;
    if(!node->children.empty())
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            if(child->value.ID == DIV) //perform division
            {
                fprintf(outputFile, "DIV ");
                i++;
                try
                {
                    child = node->children.at(i);
                    if(child->nonTerminal == TERMM)
                    {
                        result = handleM(child, outputFile);
                        fprintf(outputFile, "%s\n", getTokenValue(result).c_str());
                        i++;
                        child = node->children.at(i);
                        handleB(child, outputFile);
                    }
                }
                catch(const exception& e )
                {
                    //Throw error
                }
            }
        }
    }

    return result;
}

/**
 * Generates the source file for ACC Assembler
 * @param root parse tree node root
 * @param fileName name of the file to create
 */
void Generator::genASMFile(ParserNode* root, std::string fileName)
{
    try
    {
        fileName = fileName + ".asm";
        FILE* outputFile = fopen(fileName.c_str(), "w");
        processNode(root, outputFile);
        fclose(outputFile);
    }
    catch(const std::exception& ex)
    {
        cout << "A problem occurred while writing output file" << endl;
        std::cerr << ex.what() << '\n';
    }


}

/**
 * Writes syntax derived from <var> non-terminal
 * @param node
 * @param outputFile
 */
void Generator::handleVarNode(ParserNode* node, FILE* outputFile)
{
    if(node->children.size() > 0)
    {
        StackVariable var;
        string tokenPosition;
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* childNode = node->children.at(i);
            if(childNode->value.ID == IDTOKEN)
            {
                string IDTokenName = childNode->value.value;
                if(semantics.find(IDTokenName) == -1)
                {

                    var.scope = varScope;
                    var.ID = IDTokenName;
                    var.line = childNode->value.line;
                    semantics.push(var);
                    tokenPosition = getTokenValue(childNode->value);
                }
                else
                {
                    throw std::invalid_argument("Cannot redefine variable '" + IDTokenName + "' at line " + convertIntToString(childNode->value.line));
                }
            }
            else if(childNode->value.ID == NUMTOKEN) // write variable
            {
                fprintf(outputFile, "LOAD %s\n", childNode->value.value.c_str() );
                fprintf(outputFile, "PUSH\n");
                fprintf(outputFile, "STACKW %s\n", tokenPosition.c_str());
                break;
            }

        }
    }
}

/**
 * Writes syntax derived from the <in> non-terminal
 * @param node
 * @param outputFile
 */
void Generator::handleInputNode(ParserNode *node, FILE *outputFile)
{
    if(node->children.size() > 0)
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            if(child->value.ID == IDTOKEN)
            {
                if(semantics.find(child->value.value) != -1)
                {
                    fprintf(outputFile, "READ %s\n", IO_VAR.c_str());
                    fprintf(outputFile, "LOAD %s\n", IO_VAR.c_str());
                    fprintf(outputFile, "STACKW %s\n", getTokenValue(child->value).c_str());
                    ioUsed = true;
                }
                else
                {
                    throw std::invalid_argument("Variable '" + child->value.value + "' not defined at line " + convertIntToString(child->value.line));
                }
            }
        }
    }
}

/**
 * Handles the syntax associated with the <block> non-terminal
 * @param node
 * @param outputFile
 */
void Generator::handleBlock(ParserNode* node, FILE* outputFile)
{
    for(int i = 0; i < node->children.size(); i++)
    {
        ParserNode* child = node->children.at(i);
        if(child->value.value == "begin") //increase variable scope
        {
            varScope++;
        }
        else if(child->value.value == "end") //decrease variable scope. remove vars from stack
        {
            varScope--;
        }
        else //process additional nodes
        {
            processNode(child, outputFile);
        }
    }
}

/**
 * processes expressions <expr> non-terminal
 * @param node
 * @param outputFile
 * @return stack value of the stored expression result
 */
Token Generator::handleExpr(ParserNode* node, FILE* outputFile)
{
    Token result;
    if(node->children.empty())
    {
        //throw error
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);

            switch(child->nonTerminal)
            {
                case TERMN:
                    result = handleN(child, outputFile);
                    break;
                default:
                    if(child->value.ID == SUB)
                    {
                        i++;
                        if(result.ID != NUMTOKEN || result.ID != IDTOKEN)
                        {
                            Token exprValue = handleExpr(node->children.at(i), outputFile);
                            writeMathOperation(outputFile, result, exprValue, SUB);
                        }
                        else
                        {
                            //throw error
                        }

                    }
                    break;

            }
        }
    }


    return result;
}

void Generator::handleOut(ParserNode *node, FILE *outputFile)
{

    if(node->children.empty())
    {
        //throw error
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode*  child = node->children.at(i);
            if(child->nonTerminal == EXPR)
            {
                Token result = handleExpr(child, outputFile);
                fprintf(outputFile, "STACKR %s\n", getTokenValue(result).c_str());
                fprintf(outputFile, "STORE W\n");
                fprintf(outputFile, "WRITE W\n");
                ioUsed = true;
            }
        }
    }

}

void Generator::handleProgram(ParserNode *node, FILE *outputFile)
{
    if(node->children.empty())
    {
        //throw error
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            switch(child->nonTerminal)
            {
                case VARS:
                case BLOCK:
                    processNode(child, outputFile);
                    break;
                case TERMPROGRAM:
                    break;
                default:
                    //throw error
                    break;
            }
        }
        //finalize document
        writeEndOfDocument(outputFile);
    }
}

/**
 * General node processing.  Identifies nodes in the
 * parse tree and routes handling of the given node
 * @param node
 * @param outputFile
 */
void Generator::processNode(ParserNode* node, FILE* outputFile)
{
    //check for code generating nodes
    switch(node->nonTerminal)
    {
        case VARS:
            handleVarNode(node, outputFile);
            break;
        case BLOCK:
            handleBlock(node, outputFile);
            break;
        case IN: //handle Input
            handleInputNode(node, outputFile);
            break;
        case OUT:
            handleOut(node, outputFile);
            break;
        case TERMPROGRAM:
            handleProgram(node, outputFile);
            break;
        default://recurse through child nodes if any
            if(node->children.size() > 0)
            {
                for(int i = 0; i < node->children.size(); i++)
                {
                    processNode(node->children.at(i), outputFile);
                }
            }
    }



    //assign node

    //loop node

    //math operation node

    //conditional node

    //default(read children)
}



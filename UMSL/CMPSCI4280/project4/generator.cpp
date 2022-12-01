/**
 * generator.cpp
 * Implementation of generator header
 * Converts toy source code to ACC Assembler code
 */

#include <stdexcept>
#include <iostream>
#include <sstream>
#include "generator.h"


int varScope = 0;



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

string handleR(ParserNode* node, FILE* outputFile)
{

}

string handleM(ParserNode* node, FILE* outputFile)
{
    //check for colon

    //check for R
}

string handleA(ParserNode* node, FILE* outputFile)
{
    //check for div operator
    //handle M
}

/**
 * Iterates through branches of <N> non-terminal
 * @param node
 * @param outputFile
 * @return variable or value of result
 */
string handleN(ParserNode* node, FILE* outputFile)
{
    string result;
    for(int i = 0; i < node->children.size(); i++)
    {
        ParserNode* child = node->children.at(i);
        if(child->nonTerminal == EXPR) //check for + or * operators
        {
            //implied N.  get value
            i++;
            ParserNode* nextChild = node->children.at(i);
            result = handleN(nextChild, outputFile);
            switch(child->value.ID)
            {
                case ADD:
                    fprintf(outputFile, "ADD %s\n", result);
                    break;
                case MUL:
                    fprintf(outputFile, "MUL %s\n", result);

            }
        }
        else if(child->nonTerminal == TERMA) //handle A
        {
            result = handleA(child, outputFile);
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
                }
                else
                {
                    throw std::invalid_argument("Cannot redefine variable '" + IDTokenName + "' at line " + convertIntToString(childNode->value.line));
                }
            }
            else if(childNode->value.ID == NUMTOKEN) // write variable
            {
                fprintf(outputFile, "LOAD %s\n", childNode->value.value.c_str() );
                fprintf(outputFile, "PUSH %s\n", var.ID.c_str());
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
                    fprintf(outputFile, "READ %s\n", child->value.value.c_str());
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

void Generator::handleExpr(ParserNode* node, FILE* outputFile)
{
    switch(node->nonTerminal)
    {
        case TERMN:
            //handle N

    }
}

void Generator::handleOut(ParserNode *node, FILE *outputFile)
{
    string result = handleN(node, outputFile);
    fprintf(outputFile, "WRITE %s\n", result);
}

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
            handleExpr(node, outputFile);
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



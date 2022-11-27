/**
 * generator.cpp
 * Implementation of generator header
 * Converts toy source code to ACC Assembler code
 */

#include "generator.h"


void handleVarNode(ParserNode* node, FILE* outputFile)
{
    if(node->children.size() > 0)
    {
        StackVariable var;
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* childNode = node->children.at(i);
            if(childNode->value.ID == IDTOKEN)
            {
                IDTokenName = childNode->value.value;
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
                    break;
                }
            }
            else if(childNode->value.ID == NUMTOKEN) // write variable
            {
                fprintf(outputFile, "LOAD %s \n", childNode->value.value );
                fprintf(outputFile, "STORE %s", var.ID);
                break;
            }

        }
    }
}

void processNode(ParserNode* node, FILE* outputFile)
{
    //check for code generating nodes
    switch(node->nonTerminal)
    {
        case "vars":
            handleVarNode(node, outputFile);
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

/**
 * Generates the source file for ACC Assembler
 * @param root parse tree node root
 * @param fileName name of the file to create
 */
void Generator::genASMFile(ParserNode *root, std::string fileName)
{
    try
    {
        FILE* outputFile = fopen(fileName.c_str(), "w");
        fclose(outputFile);
    }
    catch(const std::exception& ex)
    {
        cout << "A problem occurred while writing output file" << endl;
        std::cerr << ex.what() << '\n';
    }


}




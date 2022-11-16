/*
CMPSCI 4280
Project 2: File Syntax Parser
Walter Holley III
10/22/2022

MAIN.CPP
The primary entry point of the program.  Handles file sources, and
sends the information to the scanner for tokenization, and then sends the tokens to
the parsing apparatus responsible for tranlating the program.
*/

#include <iostream>
#include <sstream>
#include <fstream>
#include <vector>
#include <string>
#include <stdexcept>
#include "scanner.h"
#include "parser.h"
#include "semantics.h"

using namespace std;

const string TEMP_FILE = "temp.in";

Scanner scanner;
Parser parser;
Semantics semantics;
int varScope = 0;


/**
 * @brief frees resources associated with
 * this program.
 */
void cleanup()
{
    //Delete Temp File
    remove(TEMP_FILE.c_str());
}

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

void printVariable(int scope, Token idToken, bool wasRemoved)
{

}


/**
 * @brief Review a node and its child contents in pre-order
 * @param node the node to print
 * @param depth depth of the node
 */
void processParseTree(ParserNode* node, int depth)
{
    vector<ParserNode*>::iterator iter = node->children.begin();
    string IDTokenName;
    StackVariable var;

    if(node->nonTerminal == "vars") //vars node
    {
        if(node->children.size() > 0)
        {
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
                    }
                }
            }
        }
    }
    else if(node->value.ID != IDTOKEN) //recurse through all children
    {
        if(node->value.value == "begin")
        {
            cout << "start of scope found" << endl;
            varScope++;
        }
        if(node->children.size() > 0)
        {
            for(iter; iter < node->children.end(); iter++)
            {
                processParseTree(*iter, depth + 1);
            }
        }

        if(node->value.value == "end") //clear current scope and decrement
        {
            cout << "End of scope reached" << endl;
            if(varScope >= 0)
            {
                while(semantics.top().scope == varScope)
                {
                    semantics.pop();
                }
                varScope--;
            }
            else
            {
                //clear remaining variables
                cout << "End of program" << endl;
            }

        }
    }
    else if(node->value.ID == IDTOKEN)
    {
        //check for existing token
        IDTokenName = node->value.value;
        if(semantics.find(IDTokenName) == -1) //Error if item not found in list
        {
            throw std::invalid_argument("Unknown variable '" + IDTokenName + "' at line " + convertIntToString(node->value.line));
        }
    }
}

/**
 * @brief processes a file parameter passed to the program
 * @param fileName
 */
void processFile(string fileName)
{
    try
    {
        vector<Token> tokens = scanner.scanFile(fileName);
        ParserNode* root = parser.parseTokens(tokens);
        processParseTree(root,1);

        delete root;
    }
    catch (const exception& e )
    {
        cout << 'A problem occurred during file analysis' <<endl;
        cerr << e.what() <<endl;
    }

}

/**
 * @brief processes the temporary file created from streamed content
 */
void processTempFile()
{
    ofstream tempFile(TEMP_FILE.c_str());
    //check for input from stdin

    while(!cin.eof())
    {
        string line;
        getline(cin, line);
        tempFile << line <<endl;
    }

    tempFile.close();

    processFile(TEMP_FILE);

    //cleanup temp file
    remove(TEMP_FILE.c_str());

}

//MAIN ENTRY POINT OF PROGRAM
int main(int argc, char *argv[])
{
    semantics.DEBUG = true;
    //read file
    if(argc > 1)
    {
        processFile(argv[1]);
    }
    else
    {
        //read stream
        processTempFile();
    }

    return 0;
}
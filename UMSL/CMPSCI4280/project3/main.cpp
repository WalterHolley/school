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
#include <fstream>
#include <vector>
#include <string>
#include "scanner.h"
#include "parser.h"

using namespace std;

const string TEMP_FILE = "temp.in";

Scanner scanner;
Parser parser;


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
 * @brief Prints the value of a given node
 * @param node
 * @param depth
 */
void printNode(ParserNode* node, int depth)
{
    //Print the node's token if set, otherwise just print the non-terminal
    if(node->value.ID != 0)
    {
        printf("%*c%s:%-9s", depth*2, ' ', node->nonTerminal.c_str(), node->value.value.c_str());
    }
    else
    {
        printf("%*c%s:", depth*2, ' ', node->nonTerminal.c_str());
    }
    printf("\n");
}

/**
 * @brief a node and its child contents in pre-order
 * @param node the node to print
 * @param depth depth of the node to print
 */
void printParseTree(ParserNode* node, int depth)
{

    //print parent node
    printNode(node, depth);

    //print children
    if(node->children.size() > 0)
    {
        vector<ParserNode*>::iterator iter = node->children.begin();
        for(iter; iter < node->children.end(); iter++)
        {
            printParseTree(*iter, depth + 1);
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
        printParseTree(root,1);

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
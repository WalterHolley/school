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
#include <vector>
#include <string>
#include <stdexcept>
#include "scanner.h"
#include "parser.h"
#include "generator.h"

using namespace std;

const string TEMP_FILE = "temp.in";

Scanner scanner;
Parser parser;
Generator generator;


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
 * @brief processes a file parameter passed to the program
 * @param fileName
 */
void processFile(string fileName)
{
    try
    {
        vector<Token> tokens = scanner.scanFile(fileName);
        ParserNode* root = parser.parseTokens(tokens);
        generator.genASMFile(root, fileName);
        cout << fileName + ".asm created"<< endl;

        delete root;
    }
    catch (const exception& e )
    {
        cout << "A problem occurred during file analysis" <<endl;
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
    //semantics.DEBUG = true;
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
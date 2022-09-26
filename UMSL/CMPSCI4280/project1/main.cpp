/*
CMPSCI 4280
Project 1: File Syntax Scanner
Walter Holley III
9/26/2022

MAIN.CPP
The primary entry point of the program.  Handles file sources, and
sends the information to the scanner.
*/
#include <iostream>
#include <fstream>
#include <string>
using namespace std;

const string TEMP_FILE = "temp.in";

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
        //processTempFile();
    }

    return 0;
}
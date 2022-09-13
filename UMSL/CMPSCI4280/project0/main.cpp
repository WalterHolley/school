/*
CMPSCI 4280 
Project 0: Tree Population and Traversal
Walter Holley III
9/15/2022

MAIN.CPP
The primary entry point of the program.  Reads input from the user, and 
parses that input into a tree structure.  Also acts as the user experience
for the command line application
*/
#include <iostream>
#include <fstream>
//#include <unistd.h>
#include<string>
#include "tree.h"
using namespace std;

#define ESC 27

//Tree to be used for application
Tree tree;

void readFromConsole(istream& input)
{
    while(input)
    {
        cout << char(input.get());
    }
}

/**
 * @brief Reads a file provided by the application into the the tree
 * 
 * @return true  = file reading was successful
 * @return false  = file reading failed
 */
bool processFile(string fileName)
{
    ifstream inputFile (fileName);
    bool fileProcessed = false;

    if(inputFile.is_open())
    {
        string fileLine;
        while(getline(inputFile, fileLine))
        {
            //parse words

            //skip space, tab, newline, etc.

            //add word to tree
        }
    }

    return fileProcessed;

}

 bool processUserInput(string input)
{
    bool inputProcessed = false;

    return inputProcessed;

}

/**
 * @brief Reads input from user until the ESC key is pressed
 * passes collected input to the tree
 */
void askForInput()
{
    string completeUserInput = "";
    char inputChar;

    cout << "Enter the input you wish to process. You can end the process anytime by sending the EOF command";
    cout << "\n";
    
    while((inputChar = getchar()) != EOF)
    {
        completeUserInput += inputChar;
    }

    //add given input to the tree

}

void buildTree(string content)
    {
        string delimeter = " ";
        string token = "";
        size_t position = 0;

        //init tree
        if(&tree == NULL)
        {
            tree = Tree();
        }
        
         //split up input 
        while((position = content.find(delimeter)) != string::npos)
        {
            token = content.substr(0,position);
            content.erase(0, position + delimeter.length());
            tree.add(token);
        }   
    }



//MAIN ENTRY POINT OF PROGRAM
int main(int argc, char *argv[])
{
    //read file
    if(argc > 1)
    {
        cout << argv[1] << endl;
        filebuf fb;
        if(fb.open(argv[1], ios::in))
        {
            istream stream(&fb);
            string line = "";
            while (stream)
            {
                getline(stream, line);
                buildTree(line);
            }
            
        }
    }  
    else
    {
        //check for input from stdin
        if(cin.eof())
        {
            //read from keyboard
            askForInput();
        }
        else
        {
            while(!cin.eof())
            {
                string line;
                getline(cin, line);
                cout << line << endl;
            }
        }
    }

    //continue if tree has been built

    //UX for printing order
    return 0;
}
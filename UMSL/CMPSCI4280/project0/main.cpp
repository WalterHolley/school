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
#include<iostream>
#include<sstream>
#include<string>
#include <coni
using namespace std;

#define ESC 27

/**
 * @brief Reads a file provided by the application into the the tree
 * 
 * @return true  = file reading was successful
 * @return false  = file reading failed
 */
bool processFile(string fileName)
{
    bool fileProcessed = false;

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
    bool getInput = true;
    string input = "";
    string completeUserInput = "";
    char inputChar;

    cout << "Enter the input you wish to process. You can end the process anytime by pushing the ESC key";
    cout << "\n";
    
    while(getInput)
    {
        _getch

        //check for escape key press
        
        if(input.find(ESC) != -1)
        {
            input += inputChar;
        }
        else
        {
            getInput = false;
        }

    }



    //add given input to the tree
    cout << "Input from user: \n";
    cout << input;

}

//MAIN ENTRY POINT OF PROGRAM
int main(int argc, char *argv[])
{
    //if 0 arg counts, do user input UX
    askForInput();

    //check for file

    //load or handle file issues

    //UX for printing order
    return 0;
}
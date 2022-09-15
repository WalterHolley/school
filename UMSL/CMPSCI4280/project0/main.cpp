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
#include<string>
#include "tree.h"
using namespace std;

//Tree to be used for application
Tree tree;



//MAIN ENTRY POINT OF PROGRAM
int main(int argc, char *argv[])
{
    //init tree
    tree = Tree();

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
                tree.buildTree(line);
            }
            
        }
        else
        {
            cout << argv[1] << ": File not found"<< endl;
        }
    }  
    else
    {
        //check for input from stdin
        cout << "Stream / type input you wish to provide.  If typing, press enter after each line, and send the EOF command when done."<< endl;
        while(!cin.eof())
        {
            string line;
            getline(cin, line);
            tree.buildTree(line);
        }
        tree.printPreOrder();
    }

    //continue if tree has been built

    return 0;
}
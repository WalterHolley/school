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

const string OUTPUT_FILE_PREORDER = "output.preorder";
const string OUTPUT_FILE_POSTORDER = "output.postorder";
const string OUTPUT_FILE_INORDER = "output.inorder";
const string TEMP_FILE = "temp.in";
const string PRE_ORDER_EXTENSION = ".preorder";
const string POST_ORDER_EXTENSION = ".postorder";
const string IN_ORDER_EXTENSION = ".inorder";

void cleanup()
{
    //Delete Temp File
    remove(TEMP_FILE.c_str());  
}


void processFile(string fileName)
{
    if(tree.buildTree(fileName.c_str()))
    {
        
        string inOrderFile;
        string postOrderFile;
        string preOrderFile;
           
        //inOrder file name
        inOrderFile.append(fileName);
        inOrderFile.append(IN_ORDER_EXTENSION);

        //postOrder fileName
        postOrderFile.append(fileName);
        postOrderFile.append(POST_ORDER_EXTENSION);

        //preOrder filename
        preOrderFile.append(fileName);
        preOrderFile.append(PRE_ORDER_EXTENSION);

        //clear existing output files if any
        remove(inOrderFile.c_str());
        remove(preOrderFile.c_str());
        remove(postOrderFile.c_str());

        tree.printInOrder(fileName + IN_ORDER_EXTENSION);
        tree.printPreOrder(fileName + PRE_ORDER_EXTENSION);
        tree.printPostOrder(fileName + POST_ORDER_EXTENSION);
    }
    else
    {
        cout << "There was a problem building the tree."<<endl;
    }
    
}

void processTempFile()
{
    ofstream tempFile(TEMP_FILE.c_str());
    //check for input from stdin
    cout << "Stream / type input you wish to provide.  If typing, press enter after each line, and send the EOF command when done."<< endl;
        
    while(!cin.eof())
    {
        string line;
        getline(cin, line);
        tempFile << line <<endl;
    }

    tempFile.close();
    if(tree.buildTree(TEMP_FILE))
    {
        //clear existing output files if any
        remove(OUTPUT_FILE_INORDER.c_str());
        remove(OUTPUT_FILE_POSTORDER.c_str());
        remove(OUTPUT_FILE_PREORDER.c_str());

        //process new files
        tree.printInOrder(OUTPUT_FILE_INORDER);
        tree.printPreOrder(OUTPUT_FILE_PREORDER);
        tree.printPostOrder(OUTPUT_FILE_POSTORDER);
    }
    else
    {
        cout << "There was a problem building the tree."<<endl;
    }

    //cleanup temp file
    remove(TEMP_FILE.c_str());
    
}

//MAIN ENTRY POINT OF PROGRAM
int main(int argc, char *argv[])
{
    //init tree
    tree = Tree();

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
#include "tree.h"
#include<string>
using namespace std;



//*****HELPER FUNCTIONS*******//

void printNode(node* Node, int depth, FILE* outputFile)
{
    
    fprintf(outputFile, "%*c%.1s:%-9s", depth*2, ' ', Node->value.c_str(), Node->value.c_str());
    fprintf(outputFile, "\n");
}

/**
 * @brief deletes a node and its children
 * from memory
 * @param Node the node to clean up 
 */
void cleanup(node* Node)
{
    if(Node->left)
    {
        cleanup(Node->left);
    }

    if(Node->right)
    {
        cleanup(Node->right);
    }

    if(Node->center)
    {
        cleanup(Node->center);
    }

    delete Node;
}

/**
 * @brief Inserts a given value into the next
 * available location in the tree.
 * @param value the value to insert
 * @param Node the node to start the search from
 */
void addValueToNode(string value, node* Node)
{
    node* nextNode;
    //add string value to empty node value
    if(Node->value.empty())
    {
        Node->value = value;
    }
    else
    {
        char nodeChar = Node->value.at(0);
        char valueChar = value.at(0);

        //compare first character
        //left for lesser character
        if(valueChar < nodeChar)
        {
            if(Node->left == NULL)
            {
                Node->left = new node;
            }
      
            nextNode = Node->left;               
        }//right for greater character
        else if(valueChar > nodeChar)
        {
            if(Node->right == NULL)
            {
                    Node->right = new node;
            }
          
            nextNode = Node->right;
        }//center for equal character
        else
        {
            if(Node->center == NULL)
            {
                Node->center = new node;
            }
            
            nextNode = Node->center;
        }
        addValueToNode(value, nextNode);
    }  
}


/**
 * @brief prints a tree in 
 * pre-order fashion
 * @param Node the node to process
 * @param depth the current depth of the tree
 */
void doPreOrder(node* Node, int depth, FILE* outputFile)
{
    if(!Node->value.empty())
    {
        printNode(Node, depth, outputFile);
    }

    if(Node->left)
    {
        doPreOrder(Node->left, depth + 1, outputFile);
    }

    if(Node->center)
    {
        doPreOrder(Node->center, depth + 1, outputFile);
    }

    if(Node->right)
    {
        doPreOrder(Node->right, depth + 1, outputFile);
    }
}

/**
 * @brief prints the tree in in-order
 * fashion
 * @param Node the node to process 
 * @param depth the depth of the node
 */
void doInOrder(node* Node, int depth, FILE* outputFile)
{
    
    //process left
    if(Node->left)
    {
        doInOrder(Node->left, depth + 1, outputFile);
    }

    //process root
    if(!Node->value.empty())
    {
        printNode(Node, depth, outputFile);
    }

    //process center
    if(Node->center)
    {
        doInOrder(Node->center, depth + 1, outputFile);
    }

    //process right
    if(Node->right)
    {
        doInOrder(Node->right, depth + 1, outputFile);
    }
}

/**
 * @brief Iterates through the tree in 
 * a post-order fashion
 * @param Node the node to be printed
 * @param depth depth of the node in the tree
 */
void doPostOrder(node* Node, int depth, FILE* outputFile)
{
    //process left
    if(Node->left)
    {
        doPostOrder(Node->left, depth + 1, outputFile);
    }

    //process center
    if(Node->center)
    {
        doPostOrder(Node->center, depth + 1, outputFile);
    }

    //process right
    if(Node->right)
    {
        doPostOrder(Node->right, depth + 1, outputFile);
    }

    //process root
    if(!Node->value.empty())
    {
        printNode(Node, depth, outputFile);
    }
}

/**
 * @brief parses strings into individual tokens
 * which are then added to the tree
 * @param content the string content to parse
 * @param Node the location to begin searching for inserting content
 */
void parseContent(string content, node* Node)
{
    string delimeter = " ";
    string token = "";
    size_t position = 0;
        
    //split up input 
    while((position = content.find(delimeter)) != string::npos)
    {
        token = content.substr(0,position);
        content.erase(0, position + delimeter.length());
        addValueToNode(token, Node);
    }
    //add final string if any
    if(!content.empty())
    {
       addValueToNode(content, Node);
    } 
}

//*****CLASS FUNCTIONS*****//
Tree::Tree()
{
    _parent = new node;
}

/**
 * @brief Prints tree to a file using 
 * in-order process
 * 
 * @param fileName name of file to write
 */
void Tree::printInOrder(string fileName)
{
       try
    {
        FILE* outputFile;
        if(_parent && (!_parent->value.empty()))
        {
            outputFile = fopen(fileName.c_str(), "w");
            doInOrder(_parent, 1, outputFile);
            fclose(outputFile);
        }
        else
        {
            cout << "The tree is empty" <<endl;
        }
    }
    catch(const std::exception& e)
    {
        cout << "There was a problem writing the postOrder file" <<endl;
        std::cerr << e.what() << '\n';
    }
}

void Tree::printPreOrder(string fileName)
{
       try
    {
        FILE* outputFile;
        if(_parent && (!_parent->value.empty()))
        {
            outputFile = fopen(fileName.c_str(), "w");
            doPreOrder(_parent, 1, outputFile);
            fclose(outputFile);
        }
        else
        {
            cout << "The tree is empty" <<endl;
        }
    }
    catch(const std::exception& e)
    {
        cout << "There was a problem writing the postOrder file" <<endl;
        std::cerr << e.what() << '\n';
    }
}

void Tree::printPostOrder(string fileName)
{
    try
    {
        FILE* outputFile;
        if(_parent && (!_parent->value.empty()))
        {
            outputFile = fopen(fileName.c_str(), "w");
            doPostOrder(_parent, 1, outputFile);
            fclose(outputFile);
        }
        else
        {
            cout << "The tree is empty" <<endl;
        }
    }
    catch(const std::exception& e)
    {
        cout << "There was a problem writing the postOrder file" <<endl;
        std::cerr << e.what() << '\n';
    }
}

/**
 * @brief Creates a binary tree from file content
 * 
 * @param fileName name of the file to read
 * @returns true if content parsing is successful,
 * otherwise false
 */
bool Tree::buildTree(string fileName)
{
    filebuf fb;
    bool result = false;
    if(fb.open(fileName, ios::in))
    {
        istream stream(&fb);
        string line;
        try
        {
            getline(stream, line);
            while(stream)
            {
                parseContent(line, _parent);
                getline(stream, line);
                
            }
            fb.close();
            result = true;
        }
        catch(const std::exception& e)
        {
            cout << "An error occurred while building the tree"<<endl;
            std::cerr << e.what() << '\n';
        } 
    }
    else
    {
        cout << fileName <<": File not found"<<endl;
    }

    return result;
}

/**
* @brief Gets the parent node of the tree
* 
* @return node* 
*/
node* Tree::getParent()
{
    return _parent;
}

Tree::~Tree() 
{
    try{
      // cleanup(_parent);
       //_parent = NULL;
    }
    catch(const exception& e)
    {

    }
    
}


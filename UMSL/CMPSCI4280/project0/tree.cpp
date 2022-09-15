#include "tree.h"
#include<string>
using namespace std;



//*****HELPER FUNCTIONS*******//

void printNode(node* Node, int depth)
{
    printf("%*c%d:%-9s ",depth*2,' ',depth,Node->value.c_str());
    printf("\n");
}

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
void doPreOrder(node* Node, int depth)
{
    if(!Node->value.empty())
    {
        printNode(Node, depth);
    }

    if(Node->left)
    {
        doPreOrder(Node->left, depth + 1);
    }

    if(Node->center)
    {
        doPreOrder(Node->center, depth + 1);
    }

    if(Node->right)
    {
        doPreOrder(Node->right, depth + 1);
    }
}

/**
 * @brief prints the tree in pre-order
 * fashion
 * @param Node the node to process 
 * @param depth the depth of the node
 */
void doInOrder(node* Node, int depth)
{
    
    //process left
    if(Node->left)
    {
        doPreOrder(Node->left, depth + 1);
    }

    //process root
    if(!Node->value.empty())
    {
        printNode(Node, depth);
    }

    //process center
    if(Node->center)
    {
        doPreOrder(Node->center, depth + 1);
    }

    //process right
    if(Node->right)
    {
        doPreOrder(Node->right, depth + 1);
    }
}

/**
 * @brief Iterates through the tree in 
 * a post-order fashion
 * @param Node the node to be printed
 * @param depth depth of the node in the tree
 */
void doPostOrder(node* Node, int depth)
{
    //process left
    if(Node->left)
    {
        doPreOrder(Node->left, depth + 1);
    }

    //process center
    if(Node->center)
    {
        doPreOrder(Node->center, depth + 1);
    }

    //process right
    if(Node->right)
    {
        doPreOrder(Node->right, depth + 1);
    }

    //process root
    if(!Node->value.empty())
    {
        printNode(Node, depth);
    }
}

//*****CLASS FUNCTIONS*****//
Tree::Tree()
{
    _parent = new node;
}

void Tree::printInOrder()
{
    doInOrder(_parent, 1);
}

void Tree::printPreOrder()
{
    doPreOrder(_parent, 1);
}

void Tree::printPostOrder()
{
    doPostOrder(_parent, 1);
}

void Tree::buildTree(string content)
{
    string delimeter = " ";
    string token = "";
    size_t position = 0;
        
    //split up input 
    while((position = content.find(delimeter)) != string::npos)
    {
        token = content.substr(0,position);
        content.erase(0, position + delimeter.length());
        addValueToNode(token, _parent);
    }
    //add final string if any
    if(!content.empty())
    {
        addValueToNode(content, _parent);
    } 
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


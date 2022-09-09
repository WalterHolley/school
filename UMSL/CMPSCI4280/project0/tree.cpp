#include "tree.h"
#include<string>
using namespace std;

class Tree{

    Tree()
    {
        
    }

    private:
    node* _parent;

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
                   Node->left = nextNode;
               }
               else
               {
                   nextNode = Node->left;
               }
           }//right for greater character
           else if(valueChar > nodeChar)
           {
               if(Node->right == NULL)
               {
                   Node->right = nextNode;
               }
               else
               {
                   nextNode = Node->right;
               }
           }//center for equal character
           else
           {
               if(Node->center == NULL)
               {
                   Node->center = nextNode;
               }
               else
               {
                   nextNode = Node->center;
               }
           }
            addValueToNode(value, nextNode);
        }  
    }

    public:
    void add(string value)
    {
        addValueToNode(value, _parent);
    }

};
#include "tree.h"
#include<string>
using namespace std;

class Tree{

    Tree(node ParentNode)
    {
        parentNode = &ParentNode;
    }

    private:
    node* parentNode;

    void addValueToNode(string value, node& Node)
    {
        //add string value to empty node value
        if(Node.value.empty())
        {
            Node.value = value;
        }
        else
        {
           char nodeChar = Node.value.at(0);
           char valueChar = value.at(0);

            //compare first character
            //left for lesser character
           if(valueChar < nodeChar)
           {
               addValueToNode(value, Node.left);
           }//right for greater character
           else if(valueChar > nodeChar)
           {
               
           }//center for equal character
           else
           {

           }

        }  
    }

    public:
    void add(string value)
    {
        addValueToNode(value, &parentNode)
    }

};
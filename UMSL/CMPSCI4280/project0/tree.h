#ifndef TREE_H
#define TREE_H

#include "node.h"
#include <iostream>
#include <fstream>
#include  <string>
using std::string;

class Tree
{
    private:
        node* _parent;
    public:
        Tree();
        bool buildTree(string fileName);
        void printInOrder(string fileName);
        void printPreOrder(string fileName);
        void printPostOrder(string fileName);
        ~Tree();
        node* getParent();
};
#endif

#ifndef TREE_H
#define TREE_H

#include "node.h"
#include <iostream>
#include  <string>
using std::string;

class Tree
{
    private:
        PrintOrder printOrder;
        node* _parent;
    public:
        Tree();
        void buildTree(string value);
        void printInOrder();
        void printPreOrder();
        void printPostOrder();
        node* getParent();
};
#endif

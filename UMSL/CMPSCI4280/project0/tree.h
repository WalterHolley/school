#ifndef TREE_H
#define TREE_H
#include "node.h"
#include <iostream>
#include  <string>
using std::string;

enum PrintOrder
{
    InOrder = 0,
    PreOrder = 1,
    PostOrder = 2
};

class Tree
{
    private:
        PrintOrder printOrder;
    public:
        Tree();
        void add(string value );
        //node getTree();
        void print();
        void setPrintOrder(PrintOrder order);
};

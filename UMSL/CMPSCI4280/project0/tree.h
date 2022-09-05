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

void add(string value );
node getTree();
void print();
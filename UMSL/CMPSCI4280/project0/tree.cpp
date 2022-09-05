#include "tree.h"

class Tree{

    Tree(node ParentNode)
    {
        parentNode = ParentNode;
    }

    public:
    PrintOrder printOrder;

    private:
    node parentNode;


};
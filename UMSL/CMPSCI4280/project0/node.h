//node.h
//Defines a node structure with threee leaves
#include <string>
struct node
{
    std::string value;
    struct node* left;
    struct node* center;
    struct node* right;
        
};

enum PrintOrder
{
    InOrder = 0,
    PreOrder = 1,
    PostOrder = 2
};
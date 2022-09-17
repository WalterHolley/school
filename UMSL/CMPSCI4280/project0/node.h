//node.h
//Defines a node structure with three leaves
#include <string>
struct node
{
    std::string value;
    struct node* left = NULL;
    struct node* center = NULL;
    struct node* right = NULL;
        
};

enum PrintOrder
{
    InOrder = 0,
    PreOrder = 1,
    PostOrder = 2
};
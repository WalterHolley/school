/**
 * semantics.h
 * @author Walter Holley III
 * 11/9/2022
 * Implementation for semantics.h
 * Creates a symbol table from the variables
 * in the parser tree
 */

#include <iostream>
#include <stdio.h>
#include "semantics.h"



void Semantics::insert(std::string value)
{
    symbolTable.push_back(value);
}

vector<string> Semantics::getSymbolTable()
{
    return symbolTable;
}

bool Semantics::verify(std::string value)
{
    bool result = false;

    for(int i = 0; i < symbolTable.size(); i++)
    {
        if( symbolTable.at(i) == value)
        {
            result = true;
            break;
        }
    }

    return result;
}

void Semantics::push(StackVariable var)
{

    symbolStack.push(var);
    printDebug(symbolStack.top(), "ADDED");
}

/**
 * Removes a semantic from the top of the stack
 */
void Semantics::pop()
{
    if(symbolStack.size() > 0)
    {
        printDebug(symbolStack.top(), "REMOVED");
        symbolStack.pop();
    }

}
/**
 * Retrieves the variable at the top of the stack
 * @return top stack variable, or var with -1 scope if empty
 */
StackVariable Semantics::top()
{
    StackVariable result;
    if(symbolStack.size() > 0)
    {
        result = symbolStack.top();
    }
    else
    {
        result.ID = "NULL";
        result.line = 0;
        result.scope = -1;
    }
    return result;
}

/**
 * Searches for variable, and returns
 * the distance of that variable from the
 * top of the stack
 * @param varName Name of variable to finde(case sensitive)
 * @return distance from top of stack, or -1 if not found
 */
int Semantics::find(string varName)
{
    int result = -1;
    stack<StackVariable> searchStack = symbolStack;

    for(int i = 0; i < symbolStack.size(); i++)
    {
        if(searchStack.top().ID != varName)
        {
            searchStack.pop();
        }
        else
        {
            printDebug(searchStack.top(), "FOUND");
            result = i;
            break;
        }
    }
    return result;
}

void Semantics::printDebug(StackVariable var, string action)
{
    if(DEBUG)
    {
        char* message;
        printf("variable %s %s at scope %i \n", var.ID.c_str(), action.c_str(), var.scope);
        //cout << message << endl;

    }
}

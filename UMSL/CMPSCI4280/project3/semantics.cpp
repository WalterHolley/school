/**
 * semantics.h
 * @author Walter Holley III
 * 11/9/2022
 * Implementation for semantics.h
 * Creates a symbol table from the variables
 * in the parser tree
 */

#include "semantics.h"


void Semantics::insert(std::string value)
{
    symbolTable.push_back(value);
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
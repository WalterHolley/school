//
// Created by Zero on 9/27/2022.
//

#ifndef TOKEN_H
#define TOKEN_H

#include <string>
using std::string;

string [][] TOKENS = {
        {"_","a","b","c","d","e","f","g","h","i","j","j","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"},
        {"0","1","2","3","4","5","6","7","8","9"},
        {"<",">","==", "!=", ":", "||", "&&"},
        {"+", "-","=", ":=", "*", "/", "^"}
};

enum tokenType {
    IDTOKEN = 0,
    NUMTOKEN = 1,
    COMPTOKEN = 2,
    OPTOKEN = 3,
    LPARAM = 4,
    RPARAM = 5,
    LBRACE = 6,
    RBRACE = 7,
    LBRACK = 8,
    RBRACK = 9,
    RWORD = 10
    

};

#endif //TOKEN_H
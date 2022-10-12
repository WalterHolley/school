//
// Created by Zero on 9/27/2022.
//

#ifndef TOKEN_H
#define TOKEN_H
#define MAX_TOKENS 80

#define MAX_TOKEN_SIZE = 8
#define COMMENT_TOKEN = "#"
#include <string>

using std::string;


const string TOKENS[17][80] = {
        {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
         "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
         "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1",
         "2", "3", "4", "5", "6", "7", "8", "9", "<", ">", "=", "!", ":", "*", "/", "^", "(", ")",
         "{", "}", "[", "]", "_", "&", " ", "."},//existing tokens
        {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
         "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
         "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "3", "3",
         "3", "3", "3", "3", "3", "3", "3", "3", "4", "4", "12", "15", "13", "4", "4", "4", "5", "-999",
         "7", "-999", "9", "-999", "0", "16", "17"},//Start State
        {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
         "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
         "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
         "2", "2", "2", "2", "2", "2", "2", "2", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
         "-999", "-999", "-999", "-999", "-999", "-999", "2", ""},//IDTokens
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
         "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
         "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
         "3", "3", "3", "3", "3", "3", "3", "3", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
         "-999", "-999", "-999", "-999", "-999", "-999", "19", "18"},//Number Tokens
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
         "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
         "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
         "-999", "-999", "-999", "-99", "-999", "-999", "-999", "-999", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
         "-999", "-999", "-999", "-999", "-999", "-999", "19", "-999"},//Comparison tokens
        {},//Operation Tokens
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
                "-999", "-999", "-999", "-99", "-999", "-999", "-999", "-999", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "19", "-999"},//Left Parenthesis
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
                "-999", "-999", "-999", "-99", "-999", "-999", "-999", "-999", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "19", "-999"},//Right Parenthesis
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
                "-999", "-999", "-999", "-99", "-999", "-999", "-999", "-999", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "19", "-999"},//Left Brace
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
                "-999", "-999", "-999", "-99", "-999", "-999", "-999", "-999", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "19", "-999"},//Right Brace
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
                "-999", "-999", "-999", "-99", "-999", "-999", "-999", "-999", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "19", "-999"},//Left Bracket
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
                "-999", "-999", "-999", "-99", "-999", "-999", "-999", "-999", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "19", "-999"},//Right Bracket
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
                "-999", "-999", "-999", "-99", "-999", "-999", "-999", "-999", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "19", "-999"},//Assign
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
                "-999", "-999", "-999", "-99", "-999", "-999", "-999", "-999", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "19", "-999"},//Colon
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
                "-999", "-999", "-999", "-99", "-999", "-999", "-999", "-999", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "19", "-999"},//vertical bar
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
                "-999", "-999", "-999", "-99", "-999", "-999", "-999", "-999", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
                "-999", "-999", "-999", "-999", "-999", "-999", "19", "-999"}

};

const string TOKEN_NAME[]  = {"START","IDTOKEN","NUMTOKEN","COMPTOKEN","OPTOKEN","LPAREN","RPAREN","LBRACE","RBRACE","LBRACK","RBRACK","RWORD","ASSIGN","COLON","VERTBAR","EXCLAIM","AMPERSND","DOT","FINAL"};
//The type of tokens available
enum TokenState {
    START = 1,
    IDTOKEN = 2,
    NUMTOKEN = 3,
    COMPTOKEN = 4,
    OPTOKEN = 5,
    LPAREN = 6,
    RPAREN = 7,
    LBRACE = 8,
    RBRACE = 9,
    LBRACK = 10,
    RBRACK = 11,
    RWORD = 12,
    ASSIGN = 13,
    COLON = 14,
    VERTBAR = 15,
    EXCLAIM = 16,
    AMPERSND = 17,
    DOT = 18,
    FINAL = 19,
    ERROR = -999

};



//Token properties
struct Token{
    TokenState ID;
    string value;
    int line;
    int col;
};

#endif //TOKEN_H
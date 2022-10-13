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


const string TOKENS[17][83] = {
        {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
         "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
         "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1",
         "2", "3", "4", "5", "6", "7", "8", "9", "<", ">", "=", "!", ":", "*", "/", "^", "(", ")",
         "{", "}", "[", "]", "_", "&", "|", ".", "+","-", ","},//existing tokens
        {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
         "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
         "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "3", "3",
         "3", "3", "3", "3", "3", "3", "3", "3", "4", "4", "4", "4", "4", "4", "4", "4", "5", "5",
         "5", "5", "9", "5", "2", "4", "4", "5", "4", "4", "5"},//Start State
        {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
         "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
         "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
         "2", "2", "2", "2", "2", "2", "2", "2", "4", "4", "4", "4", "5", "4", "4", "4", "5", "5",
         "5", "5", "5", "5", "-999", "4", "4", "5", "4", "4", "5"},//IDTokens
        {"-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
         "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999",
         "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "-999", "3", "3",
         "3", "3", "3", "3", "3", "3", "3", "3", "4", "4", "4", "4", "4", "4", "4", "4", "5", "5",
         "5", "5", "5", "5", "-999", "4", "4", "5","4","4","5"},//Number Tokens
        {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
                "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
                "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "3", "3",
                "3", "3", "3", "3", "3", "3", "3", "3", "-999", "-999", "4", "-999", "-999", "-999", "-999", "-999", "5", "5",
         "5", "5", "5", "5", "-999", "4", "4", "5", "-999", "-999","5"},//Operation tokens
        {"2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
                "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2",
                "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "3", "3",
                "3", "3", "3", "3", "3", "3", "3", "3", "4", "4", "4", "4", "4", "4", "4", "4", "-999", "-999",
                "-999", "-999", "-999", "-999", "2", "4", "4", "-999", "4", "4", "5"}//Delimiter token
};

const string TOKEN_NAME[]  = {"START","IDTOKEN","NUMTOKEN","OPTOKEN","DELIMTOKEN","FINAL"};

const string RESERVED_WORDS[] = {"begin", "end", "do", "while", "whole", "label", "return", "input", "output", "program", "warp", "if", "then", "pick", "declare", "assign", "func"};
//The type of tokens available
enum TokenState {
    START = 1,
    IDTOKEN = 2,
    NUMTOKEN = 3,
    OPTOKEN = 4,
    DELIMTOKEN = 5,
    RWORD = 6,
    FINAL = 7,
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
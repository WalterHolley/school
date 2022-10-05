//
// Created by Zero on 9/27/2022.
//

#ifndef TOKEN_H
#define TOKEN_H

#define MAX_TOKEN_SIZE = 8

#include <string>
#include <map>
using std::string;

map<string, int> TOKENS;
TOKENS.insert({"a",0});
TOKENS.insert({"b",0});
TOKENS.insert({"c",0});
TOKENS.insert({"d",0});
TOKENS.insert({"e",0});
TOKENS.insert({"f",0});
TOKENS.insert({"g",0});
TOKENS.insert({"h",0});
TOKENS.insert({"i",0});
TOKENS.insert({"j",0});
TOKENS.insert({"k",0});
TOKENS.insert({"l",0});
TOKENS.insert({"m",0});
TOKENS.insert({"n",0});
TOKENS.insert({"o",0});
TOKENS.insert({"p",0});
TOKENS.insert({"q",0});
TOKENS.insert({"r",0});
TOKENS.insert({"s",0});
TOKENS.insert({"t",0});
TOKENS.insert({"u",0});
TOKENS.insert({"v",0});
TOKENS.insert({"w",0});
TOKENS.insert({"x",0});
TOKENS.insert({"y",0});
TOKENS.insert({"z",0});
TOKENS.insert({"A",0});
TOKENS.insert({"B",0});
TOKENS.insert({"C",0});
TOKENS.insert({"D",0});
TOKENS.insert({"E",0});
TOKENS.insert({"F",0});
TOKENS.insert({"G",0});
TOKENS.insert({"H",0});
TOKENS.insert({"I",0});
TOKENS.insert({"J",0});
TOKENS.insert({"K",0});
TOKENS.insert({"L",0});
TOKENS.insert({"M",0});
TOKENS.insert({"N",0});
TOKENS.insert({"O",0});
TOKENS.insert({"P",0});
TOKENS.insert({"Q",0});
TOKENS.insert({"R",0});
TOKENS.insert({"S",0});
TOKENS.insert({"T",0});
TOKENS.insert({"U",0});
TOKENS.insert({"V",0});
TOKENS.insert({"W",0});
TOKENS.insert({"X",0});
TOKENS.insert({"Y",0});
TOKENS.insert({"Z",0});
TOKENS.insert({"0",1});
TOKENS.insert({"1",1});
TOKENS.insert({"2",1});
TOKENS.insert({"3",1});
TOKENS.insert({"4",1});
TOKENS.insert({"5",1});
TOKENS.insert({"6",1});
TOKENS.insert({"7",1});
TOKENS.insert({"8",1});
TOKENS.insert({"9",1});
TOKENS.insert({"<",2});
TOKENS.insert({">",2});
TOKENS.insert({"==",2});
TOKENS.insert({"!=",2});
TOKENS.insert({":",2});
TOKENS.insert({"||",3});
TOKENS.insert({"+",3});
TOKENS.insert({"-",3});
TOKENS.insert({"=",3});
TOKENS.insert({":=",3});
TOKENS.insert({"*",3});
TOKENS.insert({"/",3});
TOKENS.insert({"^",3});
TOKENS.insert({"(",4});
TOKENS.insert({")",5});
TOKENS.insert({"{",6});
TOKENS.insert({"}",7});
TOKENS.insert({"[",8});
TOKENS.insert({"]",9});

//The type of tokens available
enum TokenType {
    IDTOKEN = 0,
    NUMTOKEN = 1,
    COMPTOKEN = 2,
    OPTOKEN = 3,
    LPAREN = 4,
    RPAREN = 5,
    LBRACE = 6,
    RBRACE = 7,
    LBRACK = 8,
    RBRACK = 9,
    RWORD = 10,
    UNKNOWN = -999
    

};

//States for processing a token instance
enum TokenState{
    START = 100,
    NEXT = 200,
    FINAL = 300,
    ERROR = -999
};

//Token properties
struct Token{
    TokenType ID;
    string value;
    int line;
    int col;
};

#endif //TOKEN_H
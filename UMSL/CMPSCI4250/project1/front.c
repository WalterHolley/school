/* front.c - a lexical analyzer system for simple arithmetic expressions
* Original code taken from Concept of Programming Languages:12th edition
* by Robert Sebesta.  That sample will be modified for this project
*/

/*CMPSCI 4250
* Project 1
* February, 2022
* Walter Holley III
*/

#include <stdio.h>
#include <ctype.h>

/* Global declarations */
/* Variables */

int charClass;
char lexeme[100];
char nextChar;
int lexLen;
int token;
int nextToken;
FILE *in_fp;

/* Character classes */
#define LETTER 0
#define DIGIT 1
#define UNKNOWN 99

/* Token codes */
#define INT_LIT 10
#define IDENT 11
#define ASSIGN_OP 20
#define ADD_OP 21
#define SUB_OP 22
#define MULT_OP 23
#define DIV_OP 24
#define LEFT_PAREN 25
#define RIGHT_PAREN 26

/*******************************/
//main entrypoint

main(){

    //open file for data processing
    if((in_fp = fopen("front.in", "r")) == NULL)
        printf("ERROR - cannot open file \n");
    else{
        getChar();

        do{
            lex();
            
        }
        while(nextToken != EOF);
    }

}

/**
 * @brief looks up operators and parentheses,
 * and returns the token
 * 
 * @param ch character sent for alaysis
 * @return int token value
 */
int lookup(char ch){

    switch(ch){
        case '(':
            addChar();
            nextToken = LEFT_PAREN;
            break;
        case ')':
            addChar();
            nextToken = RIGHT_PAREN;
            break;
        case '+':
            addChar();
            nextToken = ADD_OP;
            break;
        case '-':
            addChar();
            nextToken = SUB_OP;
            break;
        case '*':
            addChar();
            nextToken = MULT_OP;
            break;
        case '/':
            addChar();
            nextToken = DIV_OP;
            break;
        default:
            addChar();
            nextToken = EOF;
            break;
    }

    return nextToken;
}

/**
 * @brief adds the next available
 * character to the lexeme
 * 
 */
void addchar(){
    if (lexLen <= 98){
        lexeme[lexLen++] = nextChar;
        lexeme[lexLen] = 0;
    }
    else{
        printf("ERROR - lexeme is too long \n");
    }
}

/**
 * @brief Gets next character of input
 * and determines the character class
 * 
 */
void getChar(){
    if((nextChar = getc(in_fp)) == EOF){
        if(isalpha(nextChar))
            charClass = LETTER;
        else if(isdigit(nextChar))
            charClass = DIGIT;
        else
            charClass = UNKNOWN;

    }
    else
        charClass = EOF;
}

/**
 * @brief skips past blank spaces
 * until non-blank space is detected
 * 
 */
void getNonBlank(){
    while(isspace(nextChar))
        getChar();
}

/**
 * @brief a simple lexical analyzer 
 * for arithmetic expressions 
 * 
 * @return int 
 */
int lex(){
    lexLen = 0;
    getNonBlank();

    switch(charClass){

        //parse identifiers
        case LETTER:
            addChar();
            getChar();
            while (charClass == LETTER || charClass == DIGIT){
                addChar();
                getChar();
            }
            nextToken = INT_LIT;
            break;
        
        //parens and operators
        case UNKNOWN:
            lookup(nextChar);
            getChar();
            break;

        //EOF
        case EOF:
            nextToken - EOF;
            lexeme[0] = 'E';
            lexeme[1] = 'O';
            lexeme[2] = 'F';
            lexeme[3] = 0;
            break;
    }

    printf("Next token is: %d, next lexeme is %s\n", nextToken, lexeme);
    return nextToken;

}

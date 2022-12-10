/**
 * generator.cpp
 * Implementation of generator header
 * Converts toy source code to ACC Assembler code
 */

#include <stdexcept>
#include <iostream>
#include <sstream>
#include "generator.h"
#include "semantics.h"


int varScope = 0;
Semantics semantics;
Semantics tempSemantics;
int tempPos = 0;
int conditionCount = 0;
int loopCount = 0;
bool ioUsed = false;
bool tempUsed = false;

const string IO_VAR = "W";

/**
 * Converts an integer to a string
 * @param number the integer to convert
 * @return the resulting string from the conversion
 */
string convertIntToString(int number)
{
    stringstream  ss;
    string result;
    ss << number;
    ss >> result;

    return  result;
}

/**
 * Retrieves the ACC Assembler stack value for an IDTOKEN, or the
 * numeric value for a NUMTOKEN
 * @param token
 * @return
 */
string getTokenValue(Token token)
{
    string result;
    if(token.ID == IDTOKEN)
    {
        int stackPos = semantics.find(token.value);
        if(stackPos == -1)
        {
            throw std::invalid_argument("Identifier not found: " + token.value);
        }
        else
        {
            //in ACC TOP of stack index is zero
            int topIndex = semantics.find(semantics.top().ID);
            stackPos = topIndex - stackPos;
            result = convertIntToString(stackPos);
        }
    }
    else if(token.ID == NUMTOKEN)
    {
        result = token.value;
    }
    else
    {
        throw std::invalid_argument("Integer/Identifier not found");
    }

    return result;
}

/**
 * Creates a temp variable to be used
 * in ACC Assembler
 * @return
 */
string createTempVar()
{
    string newVar = "T" + convertIntToString(tempPos);
    tempPos++;
    StackVariable var;
    var.ID = newVar;
    tempSemantics.push(var);
    tempUsed = true;
    return newVar;
}

/**
 * Creates a unique label for conditional statements.
 *
 * @return
 */
string createConditionLabel()
{
    string condLabel = "COND" + convertIntToString(conditionCount);
    conditionCount++;
    return condLabel;
}

/**
 * Creates a unique label for loops.
 *
 * @return
 */
string createLoopLabel()
{
    string loopLabel = "LOOP" + convertIntToString(conditionCount);
    loopCount++;
    return loopLabel;
}

/**
 * Gets the ACC assembler equivalent of a conditional operator
 * @param relationOperator
 * @return
 */
string getRelationalOperator(Token relationOperator)
{
    string accOperator;
    switch(relationOperator.ID)
    {
        case LT:
            accOperator = "BRZPOS";
            break;
        case GT:
            accOperator = "BRZNEG";
            break;
        case COMP:
            accOperator = "BRNEG";
            break;
        case NEQ:
            accOperator = "BRZERO";
            break;
        default:
            throw std::invalid_argument("Operator not found");
            break;
    }

    return accOperator;
}

/**
 * Writes an ACC conditional operator to a file.
 * partially translates the <if> non-terminal
 * @param relation
 * @param failureConditionLabel
 * @param outputFile
 */
void writeConditionalOperator(Token relation, string failureConditionLabel, FILE* outputFile)
{
    if(relation.ID == EQ)
    {
        fprintf(outputFile, "BRPOS %s\n", failureConditionLabel.c_str());
        fprintf(outputFile, "BRNEG %s\n", failureConditionLabel.c_str());
    }
    else
    {
        fprintf(outputFile, "%s %s\n", getRelationalOperator(relation).c_str(), failureConditionLabel.c_str());
    }
}

/**
 * Writes a math expression for the ACC Assembler
 * @param outputFile
 * @param operations
 */
void writeMathExpression(FILE* outputFile, vector<TokenOperation*> operations, string tempLeft, string tempRight)
{
    string op;

    for(int i = 0; i < operations.size(); i++)
    {
        TokenOperation* currentOperation = operations.at(i);

        //get operation
        switch(currentOperation->operation)
        {
            case ADD:
                op = "ADD";
                break;
            case SUB:
                op = "SUB";
                break;
            case MUL:
                op = "MULT";
                break;
            case DIV:
                op = "DIV";
                break;
            default:
                op = "";
                break;
        }

        if(i == 0)
        {
            switch(currentOperation->valueToken.ID)
            {
                case NUMTOKEN:
                    fprintf(outputFile, "LOAD %s\n", getTokenValue(currentOperation->valueToken).c_str());
                    break;
                case IDTOKEN:
                    fprintf(outputFile, "STACKR %s\n", getTokenValue(currentOperation->valueToken).c_str());
                    break;
                default:
                    break;
            }

        }

        if(currentOperation->operation == ASSN)
        {
            continue;
        }




            //Store current
            fprintf(outputFile, "STORE %s\n", tempLeft.c_str());

            //load next value
            switch(currentOperation->valueToken.ID)
            {
                case IDTOKEN:
                    fprintf(outputFile, "STACKR %s\n", getTokenValue(currentOperation->valueToken).c_str());
                    break;
                case NUMTOKEN:
                    fprintf(outputFile, "LOAD %s\n", getTokenValue(currentOperation->valueToken).c_str());
                    break;
            }

            //check for negation
            if(i + 1 < operations.size())
            {
                if(operations.at(i + 1)->operation == COLON)
                {
                    fprintf(outputFile, "MULT -1\n");
                    i++;
                }

            }

            //do operation
            fprintf(outputFile, "STORE %s\n", tempRight.c_str());
            fprintf(outputFile, "LOAD %s\n", tempLeft.c_str());
            fprintf(outputFile, "%s %s\n", op.c_str(), tempRight.c_str());
    }
}

/**
 * Writes the end of the ACC Assembler document
 * @param outputFile
 */
void writeEndOfDocument(FILE* outputFile)
{
    fprintf(outputFile, "STOP\n");

    //User I/O
    if(ioUsed)
    {
        fprintf(outputFile, "W 0\n");
    }

    //Temp vars
    if(tempUsed)
    {
        while(tempSemantics.top().scope != -1)
        {
            fprintf(outputFile, "%s 0\n", tempSemantics.top().ID.c_str());
            tempSemantics.pop();
        }
    }
}


TokenOperation* Generator::handleR(ParserNode* node, FILE* outputFile)
{
    TokenOperation* result;
    if(node->children.empty())
    {
        throw std::invalid_argument("Children for <R> not found");
    }
    else
    {
        for(int i= 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            if(child->nonTerminal == EXPR)
            {
                handleExpr(child, outputFile);
                continue;
            }
            else
            {
                switch(child->value.ID)
                {
                    case LPAREN:
                        break;
                    case RPAREN:
                        break;
                    case IDTOKEN:
                    case NUMTOKEN:
                        result = new TokenOperation;
                        result->valueToken = child->value;
                        result->operation = ASSN;
                        break;
                    default:
                        throw std::invalid_argument("Invalid expression/value token found:" + child->value.name);
                }
            }
        }
    }

    if(result->valueToken.ID != IDTOKEN && result->valueToken.ID != NUMTOKEN)
    {
        throw std::invalid_argument("Invalid identifier/integer found: " + result->valueToken.value);
    }

    return result;
}



vector<TokenOperation*> Generator::handleM(ParserNode* node, FILE* outputFile)
{
    TokenOperation* result;
    vector<TokenOperation*> mResult;

    vector<TokenOperation*> resultContainer;

    if(node->children.empty())
    {
        throw std::invalid_argument("Children for <M> not found");
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            switch(child->value.ID)
            {
                case COLON:
                    //syntax for negation
                    i++;
                    mResult = handleM(node->children.at(i), outputFile);
                    result = mResult.at(0);
                    result->operation = COLON;
                    resultContainer.push_back(result);
                    break;
                default:
                    result = handleR(child, outputFile);
                    resultContainer.push_back(result);
                    break;
            }
        }
    }
    return resultContainer;
}

vector<TokenOperation*> Generator::handleA(ParserNode* node, FILE* outputFile)
{

    vector<TokenOperation*> operations;
    vector<TokenOperation*> incomingOperations;
    if(node->children.empty())
    {
        throw std::invalid_argument("Children for <A> not found");
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            switch(child->nonTerminal)
            {
                case TERMM://handle M
                     incomingOperations = handleM(child, outputFile);
                     operations.insert(operations.end(), incomingOperations.begin(), incomingOperations.end());
                    break;
                case TERMB://handle B
                    incomingOperations = handleB(child, outputFile);
                    operations.insert(operations.end(), incomingOperations.begin(), incomingOperations.end());
                    break;
            }
        }
    }



    return operations;
}

/**
 * Iterates through branches of <N> non-terminal
 * @param node
 * @param outputFile
 * @return variable or value of result
 */
vector<TokenOperation*> Generator::handleN(ParserNode* node, FILE* outputFile)
{
    Token result;
    vector<TokenOperation*> operations;
    vector<TokenOperation*> incomingOperations;
    TokenOperation* nextOperation;
    if(node->children.size() == 0)
    {
        throw std::invalid_argument("Children for <N> not found");
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            switch(child->nonTerminal)
            {
                case TERMA:
                    incomingOperations = handleA(child, outputFile);
                    operations.insert(operations.end(), incomingOperations.begin(), incomingOperations.end());
                    break;
                case TERMN:
                    if(child->value.ID == ADD || child->value.ID == MUL)
                    {
                        nextOperation = new TokenOperation;
                        nextOperation->operation = child->value.ID;
                    }
                    else
                    {
                        vector<TokenOperation*> nContent = handleN(node->children.at(i), outputFile);
                        nextOperation->valueToken = nContent.at(0)->valueToken;
                        operations.push_back(nextOperation);
                        operations.insert(operations.end(),nContent.begin(), nContent.end());
                    }

                default:
                    break;

            }
        }
    }


    return operations;
}

vector<TokenOperation*> Generator::handleB(ParserNode* node, FILE* outputFile)
{
    vector<TokenOperation*> resultContainer;
    vector<TokenOperation*> bResult;
    vector<TokenOperation*> mResult;
    TokenOperation* operation;

   for(int i = 0; i < node->children.size(); i++)
   {
       ParserNode* child = node->children.at(i);
       if(i == 0)
       {
           if(child->value.ID != DIV)
           {
               break;
           }
           else
           {
               operation = new TokenOperation;
               operation->operation = DIV;
           }
       }
       switch(child->nonTerminal)
       {
           case TERMM:
               mResult = handleM(child, outputFile);
               operation->valueToken = mResult.at(0)->valueToken;
               resultContainer.push_back(operation);
               resultContainer.insert(resultContainer.end(),mResult.begin(),mResult.end());
               break;
           case TERMB:
               bResult = handleB(child, outputFile);
               if(!bResult.empty())
               {
                   resultContainer.insert(resultContainer.end(), bResult.begin(), bResult.end());
               }
               break;
       }
   }

   return resultContainer;
}

/**
 * Generates the source file for ACC Assembler
 * @param root parse tree node root
 * @param fileName name of the file to create
 */
void Generator::genASMFile(ParserNode* root, std::string fileName)
{
    try
    {
        fileName = fileName + ".asm";
        FILE* outputFile = fopen(fileName.c_str(), "w");
        processNode(root, outputFile);
        fclose(outputFile);
    }
    catch(const std::exception& ex)
    {
        cout << "A problem occurred while writing output file" << endl;
        std::cerr << ex.what() << '\n';
    }


}

/**
 * Writes syntax derived from <var> non-terminal
 * @param node
 * @param outputFile
 */
void Generator::handleVarNode(ParserNode* node, FILE* outputFile)
{
    if(node->children.size() > 0)
    {
        StackVariable var;
        string tokenPosition;
        for(int i = 0; i < node->children.size(); i++)
        {

            ParserNode* childNode = node->children.at(i);
            if(childNode == NULL)
            {
                continue;
            }
            if(childNode->value.ID == IDTOKEN)
            {
                string IDTokenName = childNode->value.value;
                if(semantics.find(IDTokenName) == -1)
                {

                    var.scope = varScope;
                    var.ID = IDTokenName;
                    var.line = childNode->value.line;
                    semantics.push(var);
                    tokenPosition = getTokenValue(childNode->value);
                }
                else
                {
                    throw std::invalid_argument("Cannot redefine variable '" + IDTokenName + "' at line " + convertIntToString(childNode->value.line));
                }
            }
            else if(childNode->value.ID == NUMTOKEN) // write variable
            {
                fprintf(outputFile, "LOAD %s\n", childNode->value.value.c_str() );
                fprintf(outputFile, "PUSH\n");
                fprintf(outputFile, "STACKW %s\n", tokenPosition.c_str());
                continue;
            }

            if(!childNode->children.empty())
            {
                handleVarNode(childNode, outputFile);
            }

        }
    }
}

/**
 * Writes syntax derived from the <in> non-terminal
 * @param node
 * @param outputFile
 */
void Generator::handleInputNode(ParserNode *node, FILE *outputFile)
{
    if(node->children.size() > 0)
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            if(child->value.ID == IDTOKEN)
            {
                if(semantics.find(child->value.value) != -1)
                {
                    fprintf(outputFile, "READ %s\n", IO_VAR.c_str());
                    fprintf(outputFile, "LOAD %s\n", IO_VAR.c_str());
                    fprintf(outputFile, "STACKW %s\n", getTokenValue(child->value).c_str());
                    ioUsed = true;
                }
                else
                {
                    throw std::invalid_argument("Variable '" + child->value.value + "' not defined at line " + convertIntToString(child->value.line));
                }
            }
        }
    }
}

/**
 * Handles the syntax associated with the <block> non-terminal
 * @param node
 * @param outputFile
 */
void Generator::handleBlock(ParserNode* node, FILE* outputFile)
{
    for(int i = 0; i < node->children.size(); i++)
    {
        ParserNode* child = node->children.at(i);
        if(child->value.value == "begin") //increase variable scope
        {
            varScope++;
        }
        else if(child->value.value == "end") //decrease variable scope. remove vars from stack
        {
            varScope--;
        }
        else //process additional nodes
        {
            processNode(child, outputFile);
        }
    }
}

/**
 * Writes ACC syntax for <if> non-terminal
 * @param node
 * @param outputFile
 */
void Generator::handleConditional(ParserNode *node, FILE *outputFile)
{
    Token relation;
    string leftTemp = createTempVar();
    string rightTemp = createTempVar();
    vector<ParserNode*> statements;
    vector<string> conditionLabels;
    int exprCount = 0;

    if(node->children.empty())
    {
        throw std::invalid_argument("Children in <if> not found");
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            switch(child->nonTerminal)
            {
                case TERMIF:
                    break;
                case TERMRO:
                    if(child->children.size() == 3 && child->children.at(0)->value.ID == LBRACKET && child->children.at(1)->value.ID == ASSN && child->children.at(2)->value.ID == RBRACKET)
                    {
                        relation.ID = COMP;
                    }
                    else
                    {
                        relation = child->children.at(0)->value;
                    }

                    break;
                case EXPR:
                    if(exprCount == 0)
                    {
                        writeMathExpression(outputFile, handleExpr(child, outputFile), leftTemp, rightTemp);
                        fprintf(outputFile, "STORE %s\n", leftTemp.c_str());
                        exprCount++;
                    }
                    else if(exprCount == 1)
                    {
                        writeMathExpression(outputFile, handleExpr(child, outputFile), rightTemp, createTempVar());
                        fprintf(outputFile, "STORE %s\n", rightTemp.c_str());
                        exprCount++;
                        string oper = "SUB";
                        //calculate ACC
                        fprintf(outputFile, "LOAD %s\n", leftTemp.c_str());
                        if(relation.ID == COMP)
                        {
                            oper = "MULT";
                        }
                        //evaluate
                        fprintf(outputFile, "%s %s\n", oper.c_str(), rightTemp.c_str());
                        exprCount++;

                    }
                    else
                    {
                        throw std::invalid_argument("Invalid number of expressions in <if>");
                    }
                    break;
                case STAT:
                    statements.push_back(child);
                    break;
                default:
                    throw std::invalid_argument("Unrecognized token in <if>");
                    break;


            }
        }

        //generate label(s)
        for(int i = 0; i < statements.size(); i++)
        {
            conditionLabels.push_back(createConditionLabel());
        }
        //write conditional operator and statements
        if(conditionLabels.size() > 1)
        {
            writeConditionalOperator(relation, conditionLabels.at(0), outputFile);
            processNode(statements.at(0), outputFile);
            fprintf(outputFile, "BR %s\n", conditionLabels.at(1).c_str());
            fprintf(outputFile, "%s: ", conditionLabels.at(0).c_str());
            processNode(statements.at(1), outputFile);
            fprintf(outputFile, "%s: NOOP\n", conditionLabels.at(1).c_str());
        }
        else
        {
            writeConditionalOperator(relation, conditionLabels.at(0), outputFile);
            processNode(statements.at(0), outputFile);
            fprintf(outputFile, "%s: NOOP\n", conditionLabels.at(0).c_str());
        }
    }
}

/**
 * processes expressions <expr> non-terminal
 * @param node
 * @param outputFile
 * @return stack value of the stored expression result
 */
vector<TokenOperation*> Generator::handleExpr(ParserNode* node, FILE* outputFile)
{
    vector<TokenOperation*> operations;
    vector<TokenOperation*> incomingOperations;
    TokenOperation* tokenOp;
    if(node->children.empty())
    {
        throw std::invalid_argument("children in <expr> not found");
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);

            switch(child->nonTerminal)
            {
                case TERMN:
                    incomingOperations = handleN(child, outputFile);
                    operations.insert(operations.end(), incomingOperations.begin(), incomingOperations.end());
                    break;
                case EXPR:
                    if(child->value.ID == SUB)
                    {
                        tokenOp = new TokenOperation;
                        tokenOp->operation = SUB;
                    }
                    else
                    {
                        incomingOperations = handleExpr(node->children.at(i), outputFile);
                        tokenOp->valueToken = incomingOperations.at(0)->valueToken;
                        operations.push_back(tokenOp);
                        operations.insert(operations.end(), incomingOperations.begin(), incomingOperations.end());
                    }
                    break;
                default:
                    throw std::invalid_argument("Invalid non-terminal in <expr>");
                    break;

            }
        }
    }
    return operations;
}

void Generator::handleOut(ParserNode *node, FILE *outputFile)
{

    if(node->children.empty())
    {
        throw std::invalid_argument("Children in <out> not found");
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode*  child = node->children.at(i);
            if(child->nonTerminal == EXPR)
            {
                Token result = handleExpr(child, outputFile).at(0)->valueToken;
                if(result.ID == NUMTOKEN)
                {
                    fprintf(outputFile, "LOAD %s\n", getTokenValue(result).c_str());
                }
                else
                {
                    fprintf(outputFile, "STACKR %s\n", getTokenValue(result).c_str());
                }
                fprintf(outputFile, "STORE W\n");
                fprintf(outputFile, "WRITE W\n");
                ioUsed = true;
            }
        }
    }

}

void Generator::handleProgram(ParserNode *node, FILE *outputFile)
{
    if(node->children.empty())
    {
        throw std::invalid_argument("Children in <program> not found");
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            switch(child->nonTerminal)
            {
                case VARS:
                case BLOCK:
                    processNode(child, outputFile);
                    break;
                case TERMPROGRAM:
                    break;
                default:
                    throw std::invalid_argument("Invalid non-terminal in <program>");
            }
        }
        //finalize document
        writeEndOfDocument(outputFile);
    }
}

/**
 * Translates <assign> non-terminals to ACC assembler
 * @param node node of ASSIGN
 * @param outputFile file for statement output
 */
void Generator::handleAssign(ParserNode *node, FILE *outputFile)
{
    string stackVar;
    Token exprValue;
    vector<TokenOperation*> exprs;
    if(node->children.empty())
    {
        throw std::invalid_argument("Children in <ASSIGN> not found");
    }
    else
    {
        for(int i = 0; i < node->children.size(); i++)
        {
            ParserNode* child = node->children.at(i);
            switch(child->nonTerminal)
            {
                case TERMASSIGN:
                    if(child->value.ID == IDTOKEN)
                    {
                        stackVar = getTokenValue(child->value);
                    }
                    break;
                case EXPR:
                    exprs = handleExpr(child, outputFile);
                    exprValue = exprs.at(0)->valueToken;
                    break;

            }

        }

        if(exprs.size() > 1)
        {
            writeMathExpression(outputFile, exprs, createTempVar(), createTempVar());
        }
        else
        {
            switch(exprValue.ID)
            {
                case IDTOKEN:
                    fprintf(outputFile, "STACKR %s\n", getTokenValue(exprValue).c_str());
                    break;
                case NUMTOKEN:
                    fprintf(outputFile, "LOAD %s\n", getTokenValue(exprValue).c_str());
                    break;
                default:
                    throw std::invalid_argument("Invalid token in  <ASSIGN> non-terminal");
            }
        }


        fprintf(outputFile, "STACKW %s\n", stackVar.c_str());
    }
}

void Generator::handleLoop(ParserNode* node, FILE* outputFile)
{
    vector<ParserNode*> expressions;
    int statementIndex = 0;
    string leftTemp = createTempVar();
    string rightTemp = createTempVar();
    string intTemp = createTempVar();
    string loopLabel;
    string condLabel;
    string oper = "SUB";
    Token relation;

    //get loop properties
    for(int i = 0; i < node->children.size(); i++)
    {
        ParserNode* child = node->children.at(i);

        switch(child->nonTerminal)
        {
            case EXPR:
                expressions.push_back(child);
                break;
            case TERMRO:
                if(child->children.size() == 3 && child->children.at(0)->value.ID == LBRACKET && child->children.at(1)->value.ID == ASSN && child->children.at(2)->value.ID == RBRACKET)
                {
                    relation.ID = COMP;
                }
                else
                {
                    relation = child->children.at(0)->value;
                }
                break;
            case STAT:
                statementIndex = i;

        }
    }

    //

    //get loop and exit labels
    loopLabel = createLoopLabel();
    condLabel = createConditionLabel();

    //start loop
    fprintf(outputFile, "%s: ", loopLabel.c_str());
    //math expressions for loop condition
    writeMathExpression(outputFile, handleExpr(expressions.at(0), outputFile), leftTemp, rightTemp);
    fprintf(outputFile, "STORE %s\n", leftTemp.c_str());
    writeMathExpression(outputFile, handleExpr(expressions.at(1), outputFile), rightTemp, intTemp);
    fprintf(outputFile, "STORE %s\n", rightTemp.c_str());

    //write loop relational operator

    //calculate ACC
    fprintf(outputFile, "LOAD %s\n", leftTemp.c_str());
    if(relation.ID == COMP)
    {
        oper = "MULT";
    }
    //evaluate
    fprintf(outputFile, "%s %s\n", oper.c_str(), rightTemp.c_str());

    writeConditionalOperator(relation, condLabel, outputFile);
    //loop statements
    processNode(node->children.at(statementIndex), outputFile);
    //jump to start of loop
    fprintf(outputFile, "BR %s\n", loopLabel.c_str());
    //exit point
    fprintf(outputFile, "%s: NOOP\n", condLabel.c_str());


}

/**
 * Translates the <mStat> non-terminal for
 * the ACC Assembler
 * @param node
 * @param outputFile
 */
void Generator::handleMStats(ParserNode *node, FILE *outputFile)
{
    for(int i = 0; i < node->children.size(); i++)
    {
        ParserNode* child = node->children.at(i);
        switch(child->nonTerminal)
        {
            case STAT:
                processNode(child, outputFile);
                break;
            case MSTAT:
                handleMStats(child, outputFile);
        }
    }
}

/**
 * Translates the <label> non-terminal for the
 * ACC assembler
 * @param node
 * @param outputFile
 */
void Generator::handleLabel(ParserNode *node, FILE *outputFile)
{
    for(int i = 0; i < node->children.size(); i++)
    {
        ParserNode* child = node->children.at(i);
        if(child->value.ID == IDTOKEN)
        {
            fprintf(outputFile, "%s: NOOP\n", child->value.value.c_str());
        }
    }
}

/**
 * Handles the <goto> non-terminal for
 * the ACC assembler
 * @param node
 * @param outputFile
 */
void Generator::handleGoto(ParserNode *node, FILE *outputFile)
{
    for(int i = 0; i < node->children.size(); i++)
    {
        ParserNode* child = node->children.at(i);
        if(child->value.ID == IDTOKEN)
        {
            fprintf(outputFile, "BR %s\n", child->value.value.c_str());
        }
    }
}

/**
 * Handles <stats> non-terminal translation for the ACC Assembler
 * @param node
 * @param outputFile
 */
void Generator::handleStats(ParserNode *node, FILE *outputFile)
{
    for(int i = 0; i < node->children.size(); i++)
    {
        ParserNode* child = node->children.at(i);

        switch(child->nonTerminal)
        {
            case STAT:
            case MSTAT:
                processNode(child, outputFile);
                break;
            default:
                throw std::invalid_argument("Invalid non-terminal in <stats>");
        }
    }
}


/**
 * General node processing.  Identifies nodes in the
 * parse tree and routes handling of the given node
 * @param node
 * @param outputFile
 */
void Generator::processNode(ParserNode* node, FILE* outputFile)
{
    //check for code generating nodes
    switch(node->nonTerminal)
    {
        case VARS:
            handleVarNode(node, outputFile);
            break;
        case TERMASSIGN:   //assign node
            handleAssign(node, outputFile);
            break;
        case BLOCK:
            handleBlock(node, outputFile);
            break;
        case IN: //handle Input
            handleInputNode(node, outputFile);
            break;
        case OUT:
            handleOut(node, outputFile);
            break;
        case TERMIF:
            handleConditional(node, outputFile);
            break;
        case LOOP:
            handleLoop(node, outputFile);
            break;
        case STATS:
            handleStats(node, outputFile);
            break;
        case MSTAT:
            handleMStats(node, outputFile);
            break;
        case STAT:
            if(node->children.size() > 0)
            {
                for(int i = 0; i < node->children.size(); i++)
                {
                    processNode(node->children.at(i), outputFile);
                }
            }
            else if(node->value.ID != SEMICOLON)
            {
                throw std::invalid_argument("No statements in <stat> found");
            }
            break;
        case TERMLABEL:
            handleLabel(node, outputFile);
            break;
        case TERMGOTO:
            handleGoto(node, outputFile);
            break;
        case TERMPROGRAM:
            handleProgram(node, outputFile);
            break;
        default://recurse through child nodes if any
            if(node->children.size() > 0)
            {
                for(int i = 0; i < node->children.size(); i++)
                {
                    processNode(node->children.at(i), outputFile);
                }
            }
    }

}



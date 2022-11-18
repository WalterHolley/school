//
// Created by Zero on 9/26/2022.
//

#ifndef SCANNER_H
#define SCANNER_H

#include "token.h"
#include <string>
#include <vector>

using namespace std;

class Scanner
{

    private:
        int findToken(char firstChar);
        string handleNewLines(string value);
    public:
        vector<Token> scanFile(string fileName);
        vector<Token> verifyTokens(string tokens, int lineNumber);
};
#endif //SCANNER_H

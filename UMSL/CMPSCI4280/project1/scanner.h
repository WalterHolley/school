//
// Created by Zero on 9/26/2022.
//

#ifndef SCANNER_H
#define SCANNER_H

#include "token.h"
#include <string>
#include <fstream>

class Scanner
{
    private:
        TokenType findToken(char firstChar);
        vector<Token> verifyToken(string token, int lineNumber);
    public:
        void scanFile(std::filebuf fb);
};
#endif //SCANNER_H

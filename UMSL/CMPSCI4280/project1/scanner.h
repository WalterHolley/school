//
// Created by Zero on 9/26/2022.
//

#ifndef SCANNER_H
#define SCANNER_H

#include "token.h"
#include <string>

class Scanner
{
    private:
        int findToken(char firstChar);
    public:
        vector<Token> verifyToken(string token);
};
#endif //SCANNER_H

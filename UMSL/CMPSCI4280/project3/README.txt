CMPSCI 4280
Fall 2022

Project 3: Static Semantics
Takes in a file, and analyzes its contents.  After separating the individual file tokens, and
parsing the file against its BNF grammar, the semantics program reviews the BNF parse tree for
semantic variable usage issues.  The application halts if the following problems are found:
-redefining existing variables
-using variables that do not exist

Invocation:
> statSem [file]
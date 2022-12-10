Storage=Local
CMPSCI 4280
Fall 2022

Project 4: Code Generation
Takes in a file written in the source language described for the class, and analyzes its contents, separating the individual file tokens,
parses the file into a tree structure, generates the static semantics for the document,
and outputs a file containing code that can be used to work with the ASM interpreter
used by Univ. Of Missouri.

Invocation:
> compfs [file]

Output:
[file].asm

Example source file input:
whole x := 0 ;
program
begin
input x ;
end

ACC Assembler file output
LOAD 0
PUSH
STACKW 0
READ W
LOAD W
STACKW 0
STOP
W 0

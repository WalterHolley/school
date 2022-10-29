CMPSCI 4280
Fall 2022
Walter Holley III

Project 1: Scanner

Usage:
./scanner filename
./scanner << filename

Implement scanner for the provided lexical definitions.

The scanner is embedded and thus it will return one token every time it is called. Since the parser is not available yet, we will use a tester program to call the scanner.

Scanner Style:  FSA Table and driver(option 3)

Token Breakdown:
IDTOKEN (Identifiers)
NUMTOKEN (Numbers. Integers only.  No real numbers)
OPTOKEN (Operators: =  <  > == != :=  +  -  *  /   ^   || &&)
DELIMTOKEN (Delimiters and grouping tokens: . : (  ) , { } ; [ ])
RWORD (Reserved word token: begin end do while whole label return input output program warp if then pick declare assign func )
#This is a comment.  nothing should be captured until another # has been found.
Reserved words:
begin end do while whole label return input output program warp if then pick declare assign func
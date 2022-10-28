CC=g++
EXEC=scanner


scanner: testScanner.o scanner.o  main.cpp
	$(CC) testScanner.o scanner.o main.cpp -o $(EXEC)

scanner.o: token.h scanner.h scanner.cpp
	$(CC) -g -c scanner.cpp

scanner.a: token.h scanner.h scanner.cpp
	$(CC) scanner.cpp scanner.a

testScanner.o: token.h testScanner.h testScanner.cpp
	$(CC) -g -c testScanner.cpp






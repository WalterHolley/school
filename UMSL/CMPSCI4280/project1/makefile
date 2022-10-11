CC=g++
EXEC=scanner


scanner: main.cpp scanner.o testScanner.o
	$(CC) scanner.o main.cpp -o $(EXEC)

scanner.o: token.h scanner.h scanner.cpp
	$(CC) -g -c scanner.cpp

testScanner.o: testScanner.h testScanner.cpp
	$(CC) -g -c testScanner.cpp





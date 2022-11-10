CC=g++
EXEC=statSem


statSem: scanner.o parser.o main.cpp
	$(CC) scanner.o parser.o main.cpp -o $(EXEC)

scanner.o: token.h scanner.h scanner.cpp
	$(CC) -g -c scanner.cpp

parser.o: token.h parser.h parser.cpp
	$(CC) -g -c parser.cpp
CC=g++
EXEC=statSem


statSem: scanner.o parser.o semantics.o main.cpp
	$(CC) scanner.o parser.o semantics.o main.cpp -o $(EXEC)

semantics.o: semantics.h semantics.cpp
	$(CC) -g -c semantics.cpp

scanner.o: token.h scanner.h scanner.cpp
	$(CC) -g -c scanner.cpp

parser.o: token.h parser.h parser.cpp
	$(CC) -g -c parser.cpp
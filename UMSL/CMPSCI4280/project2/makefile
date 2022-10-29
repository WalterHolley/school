CC=g++
EXEC=frontend


frontend: scanner.o main.cpp
	$(CC) scanner.o main.cpp -o $(EXEC)

scanner.o: token.h scanner.h scanner.cpp
	$(CC) -g -c scanner.cpp
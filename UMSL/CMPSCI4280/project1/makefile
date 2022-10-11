CC=g++
EXEC=scanner


scanner: main.cpp
	$(CC) scanner.o main.cpp -o $(EXEC)

scanner.o: scanner.h token.h scanner.cpp
	$(CC) -g -c scanner.cpp





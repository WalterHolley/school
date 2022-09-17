CC=g++
EXEC=p0


p0: main.cpp tree.o
	$(CC) tree.o main.cpp -o $(EXEC)

tree.o: tree.h node.h tree.cpp
	$(CC) -g -c tree.cpp




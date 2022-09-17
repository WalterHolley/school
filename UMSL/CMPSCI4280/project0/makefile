CC=g++


p0: tree.o main.cpp
	$(CC) -g tree.o main.cpp -o p0

tree.o: tree.h node.h tree.cpp
	$(CC) tree.h node.h tree.cpp
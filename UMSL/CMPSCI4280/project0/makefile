CC=g++

p0: tree.o main.cpp
	$(CC) tree.o main.cpp -o p0

tree.o: tree.h node.h tree.cpp
	$(CC) -c tree.h node.h tree.cpp
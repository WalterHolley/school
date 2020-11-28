CC=gcc

calc: mathHelper.o main.c
	$(CC) mathHelper.o main.c -o calc

mathHelper.o: mathHelper.h mathHelper.c
	$(CC) -c mathHelper.h mathHelper.c

CC=gcc

calc: mathHelper.o mathTests.o main.c
	$(CC) mathHelper.o mathTests.o main.c -o calc

mathHelper.o: mathHelper.h mathHelper.c
	$(CC) -c mathHelper.h mathHelper.c

mathTests.o: mathTests.h mathTests.c
	$(CC) -c mathTests.h mathTests.c

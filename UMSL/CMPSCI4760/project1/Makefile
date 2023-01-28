CC=gcc
EXEC1=oss
EXEC2=worker

all: oss worker

oss: oss.c
	$(CC) oss.c -o -g $(EXEC1)

worker: worker.c
	$(CC) worker.c -o -g $(EXEC2)

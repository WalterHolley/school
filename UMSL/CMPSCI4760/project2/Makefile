CC=gcc
CFLAGS=-g -o
EXEC1=oss
EXEC2=worker

all: oss worker



oss: oss.c
	$(CC) $(CFLAGS) $(EXEC1) oss.c

worker: worker.c
	$(CC) $(CFLAGS) $(EXEC2) worker.c

clean:
	rm -f *.o $(EXEC1) $(EXEC2)

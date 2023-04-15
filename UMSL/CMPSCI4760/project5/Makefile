CC=gcc
CFLAGS=-g -o
EXEC1=oss
EXEC2=user_proc

all: oss user_proc



oss: oss.c
	$(CC) $(CFLAGS) $(EXEC1) oss.c

user_proc: worker.c
	$(CC) $(CFLAGS) $(EXEC2) worker.c

clean:
	rm -f *.o $(EXEC1) $(EXEC2)

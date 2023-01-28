#define MAX_CONCURRENT_WORKERS 19

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

//globals
int totalWorkers;
int maxIterations;
int maxSimultaneous;

int main(int argCount, char *argv[])
{
    int options;
    while((options = getopt(argCount, argv, ":hn:s:t:")) != -1)
    {
        switch(options)
        {
            case 'h':
                printf("help option \n");
                return 0;
            case 'n':
            case 's':
            case 't':
                printf("%c value: %s\n", options, optarg);
                break;
            case ':':
                printf("Missing option value\n");
                break;
            case '?':
            default:
                printf("Unknown option: %c\n", optopt);

        }
    }
    return 0;
}
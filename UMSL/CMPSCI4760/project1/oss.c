#define MAX_CONCURRENT_WORKERS 19

#include <stdio.h>
#include <unistd.h>

//globals
int totalWorkers;
int maxIterations;

int main(int argCount, char *argv[])
{
    int options;
    while((options = getopt(argCount, argv, ":hn:s:t:")) != -1)
    {
        switch(options)
        {
            case 'h':
                printf("help option \n");
                break;
            case 'n':
            case 's':
            case 't':
                printf("%s value: %s\n", options, optarg);
                break;
            case ':':
                printf("Missing option value\n");
                break;
            case '?':
            default:
                printf("Unknown option: %s\n", options);

        }
    }
    return 0;
}
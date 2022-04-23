#include <stdio.h>
#define DEFAULT_SIZE 1000


int main(){
    f1();
    return 0;
}

void f1(){
    
    static int n = 0;
    char* test[DEFAULT_SIZE];
    static long int addr;

    n++;

    if(n <= 10){
        printf("Call #%d at %x\n", n, &test);
        printf("AR Size #%d is %d \n\n", n, addr - (long)&test);
        addr = &test;
        f1();
    }
    
    
}
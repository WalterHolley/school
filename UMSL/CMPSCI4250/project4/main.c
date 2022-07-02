#include <stdio.h>
#include <stdlib.h>

#define DEFAULT_SIZE 1000
#define ALLOC_ARR(size)((char*) malloc(size))

int main(){
    //f1();
    //f2();
    f3();
    return 0;
}

void f1(){
    
    static int n = 0;
    char* test[DEFAULT_SIZE];
    static long int addr;

    n++;

    //TODO: review ARI calculations
    if(n <= 10){
        printf("Call #%d at %x\n", n, &test);
        printf("AR Size #%d is %d \n\n", n, addr - (long)&test);
        addr = &test;
        f1();
    }
    
    
}

void f2(){
    
    static int n = 0;
    char* test[DEFAULT_SIZE];
    static long int addr;

    n++;

        printf("Call #%d at %x\n", n, &test);
        printf("AR Size #%d is %d \n", n, addr - (long)&test);
        printf("Stack Size #%d is %d \n\n", n, n * (addr - (long)&test) );
        addr = &test;
        f2();    
}

void f3(){
    static int n = 0;
    //allocate array
    char* test = ALLOC_ARR(DEFAULT_SIZE);
    char * currentAddr = (char*)&test;
    static long int addr;

    n++;

    if(n <= 10){
        printf("Call #%d at %x\n", n, &test);
        printf("AR Size #%d is %d \n\n", n, addr - (long int)currentAddr);
        //free array
        free(test);
        addr = (long)currentAddr;
        f3();
    }

    if(test)
        free(test); 
}
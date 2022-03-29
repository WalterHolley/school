#include <stdio.h>

void swap(int a, int b){
     int temp;
     temp = a;
     a = b;
     b = temp;
}

void main(int argc, char** argv) {
     int value = 2, list[5] = {1, 3, 5, 7, 9};
     swap(value, list[0]);
     printf("swap result 1: %d %d \n", value, list[0]);
     swap(list[0], list[1]);
     printf("swap result 2: %d %d \n", value, list[1]);
     swap(value, list[value]);
     printf("swap result 1: %d %d \n", value, list[value]);
}

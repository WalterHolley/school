#include <limits.h>
#include "mathHelper.h"

int add(int value1, int value2){
  return value1 + value2;
}

int subtract(int value1, int value2){
  return value1 - value2;
}

int multiply(int value1, int value2){
  return value1 * value2;
}

int divide(int value1, int value2){
  int answer;
  if(value2 == 0){
    printf("Cannot divide by zero\n");
    answer = INT_MIN;
  }
  else
    answer = value1 / value2;
  return answer;
}

int modulus(int value1, int value2){
  return value1 % value2;
}

void printHelp(){
  printf("USAGE\n");
  printf("-h \t Shows help\n");
  printf("-t \t Runs tests for application\n");
}

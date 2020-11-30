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
    answer = 0;
  }
  else
    answer = value1 / value2;
  return answer;
}

int modulus(int value1, int value2){
  return value1 % value2;
}

void printHelp(){

}

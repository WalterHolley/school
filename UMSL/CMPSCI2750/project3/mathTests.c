#include <limits.h>
#include <stdlib.h>
#include "mathTests.h"
#include "mathHelper.h"

void runAllMathTests(){
  printf("==Beginning Addition Test==\n");
  runAdditionTest();
  printf("==Beginning Subtraction Test==\n");
  runSubtractionTest();
  printf("==Beginning Multiplication Test==\n");
  runMultiplicationTest();
  printf("==Beginning Division Test==\n");
  runDivisionTest();
  printf("==Beginning Remainder/Modulus Test==\n");
  runModulusTest();


}
void runAdditionTest(){
  int val1 = rand();
  int val2 = rand();
  int expected = val1 + val2;
  int actual = add(val1, val2);
  printf("Value 1: %d Value 2: %d\n", val1, val2);
  printf("Expected: %d | Actual: %d\n", expected, actual);
  if(expected == actual){
    printf("Addition Test Passed\n");
  }
  else{
    printf("Addition Test Failed \n");
  }
}

void runSubtractionTest(){
  int val1 = rand();
  int val2 = rand();
  int expected = val1 - val2;
  int actual = subtract(val1, val2);
  printf("Value 1: %d Value 2: %d\n", val1, val2);
  printf("Expected: %d | Actual: %d\n", expected, actual);
  if(expected == actual){
    printf("Subtraction Test Passed\n");
  }
  else{
    printf("Subtraction Test Failed \n");
  }
}

void runMultiplicationTest(){
  int val1 = rand();
  int val2 = rand();
  int expected = val1 * val2;
  int actual = multiply(val1, val2);
  printf("Value 1: %d Value 2: %d\n", val1, val2);
  printf("Expected: %d | Actual: %d\n", expected, actual);
  if(expected == actual){
    printf("Multiplication Test Passed\n");
  }
  else{
    printf("Multiplication Test Failed \n");
  }
}

void runDivisionTest(){
  int totalTests = 2;
  int testPassed = 0;
  int val1 = rand();
  int val2 = rand();
  int expected = val1 / val2;
  int actual = divide(val1, val2);
  printf("Value 1: %d Value 2: %d\n", val1, val2);
  printf("Expected: %d | Actual: %d\n", expected, actual);
  if(expected == actual){
    printf("Division Test Passed\n");
    testPassed++;
  }
  else{
    printf("Division Test Failed \n");
  }

  val2 = 0;
  expected = INT_MIN;
  actual = divide(val1, val2);
  printf("Division By Zero Test\n");
  printf("Value 1: %d Value 2: %d\n", val1, val2);
  printf("Expected: %d | Actual: %d\n", expected, actual);
  if(expected == actual){
    printf("Division by zero Test Passed\n");
    testPassed++;
  }
  else{
    printf("Division by zero Test Failed \n");
  }

  if(testPassed == totalTests){
    printf("All division tests passed\n");
  }
  else{
    printf("All or some division tests failed\n");
  }
}
void runModulusTest(){
  int val1 = rand();
  int val2 = rand();
  int expected = val1 % val2;
  int actual = modulus(val1, val2);
  printf("Value 1: %d Value 2: %d\n", val1, val2);
  printf("Expected: %d | Actual: %d\n", expected, actual);
  if(expected == actual){
    printf("Remainder Test Passed\n");
  }
  else{
    printf("Remainder Test Failed \n");
  }
}

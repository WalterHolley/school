#include <stdio.h>

int main(int argc, char** argv){
  int option;

  while ((option = getopt(argc, argv, "hta:s:m:d:r:")) != -1){
    switch(option){
      case 'h':
        //help function
        break;
      case 't':
        //test function
        break;
      case 'a':
        //addition function
        break;
      case 's':
        //subtraction function
        break;
      case 'm':
        //multiplication function
        break;
      case 'd':
        //division function
        break;
      case 'r':
        //remainder function
        break;

    }
  }
}

int  getValue(){
  int result = 0;
  return result;
}
int validateDivision(int value1, int value2){
  int result = 0;
  if(value1 < value2){
    printf("BAD_NUMBER_PAIR");
  }
  else
    result = 1;
  return result;
}

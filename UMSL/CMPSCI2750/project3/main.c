#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "mathHelper.h"
#include "mathTests.h"

#define OPTIONS "ht"
#define MAX_BUFFER_SIZE 12

//Asks for an integer from the user.
//loops until an integer is received.
int getValidNumberSelection(char* message){
  int validNumber = 0;
  char input[MAX_BUFFER_SIZE];
  int result = 0;

  while(!validNumber){
    printf("%s", message);
    fgets(input, MAX_BUFFER_SIZE, stdin);
    int selection = atoi(input);
     if(selection == 0){
       if(input[0] != '0'){
         printf("your selection is not a valid number\n");
       }
       else{

         result = selection;
         validNumber = 1;
       }

     }
     else{
       result = selection;
       validNumber = 1;
     }
  }
  return result;
}

//Processes the math function asked for by the main menu
void processSelection(int selection){
  int value1 = getValidNumberSelection("Enter the first number:");
  int value2 = getValidNumberSelection("Enter the second number:");
  system("clear");
  int answer;
  switch(selection){
    case 1:
      answer = add(value1, value2);
      break;
    case 2:
      answer = subtract(value1,value2);
      break;
    case 3:
      answer = multiply(value1, value2);
      break;
    case 4:
      answer = divide(value1, value2);
      break;
    case 5:
      answer = modulus(value1, value2);
      break;
  }

  printf("Answer: %d\n", answer);
}

//Validates the selection option from the main menu
int isValidMenuSelection(int selection){
  int result = 0;
  if(selection >= 1 && selection <= 6)
  result = 1;
  return result;
}

int getArgIndex(char* arg, char** argv, int argc){
  int result = -1;
  int i;
  printf("total args: %d", argc);
    for(i = 0; i < argc; i++){
    if(arg == argv[i]){
      result = i;
      break;
    }
  }
  return result;
}

//TODO: Fix argument validation
//Determines if two arguments are provided
//Determines the arguments provided are integers
int isValidArgs(char* arg1, char* arg2){
  int result = 0;
  int intValue1 = atoi(arg1);
  int intValue2 = atoi(arg2);
  char* strValue1;
  char* strValue2;
  printf("assign text \n");
  sprintf(strValue1, "%d \n", intValue1);
  sprintf(strValue2, "%d \n", intValue2);
  printf("%s \n", strValue1);
  printf("%s \n", strValue2);

  if(strValue1 == arg1 && strValue1 == arg2){
    result = 1;
  }
  else{
    printf("Argument(s) are invalid.\n");
  }
  return result;
}

void showMainMenu(){
  int showMenu = 1;
  int selectionLoop;
  char selection;

  while(showMenu == 1){
    printf("MAIN MENU\n");
    printf("1>>add\n2>>subtract\n3>>multiply\n4>>divide\n5>>remainder\n6>>exit\n");
    selectionLoop = 1;
    while(selectionLoop){
      selection = getValidNumberSelection("Make Selection:");
      if(isValidMenuSelection(selection)){
        selectionLoop = 0;

        if(selection == 6){
          printf("Exiting Program\n");
          showMenu = 0;
        }
        else{
          processSelection(selection);
        }
      }
      else{
        printf("Invalid menu selection\n");
      }
    }
  }
}

int main(int argc, char** argv){
  int option;
  if(argc > 1){
    while ((option = getopt(argc, argv, OPTIONS)) != -1){
      switch(option){
        case 'h':
          //help function
          help();
          break;
        case 't':
          //test function
          runAllMathTests();
          break;
      }
    }

  }
  else{
    showMainMenu();
  }
}

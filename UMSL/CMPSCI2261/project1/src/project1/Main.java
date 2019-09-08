package project1;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * 
 * @author Walter Holley III
 * PROJECT 1
 * CMP_SCI 2261
 * 
 * OBJECTIVE
 * Write a program with a menu that allows the user to select from any of the below subprograms 
 * (along with a quitting feature). 
 * Your program must implement all 3 of the subprograms.
 *
 * ANAGRAM DETECTION
 * Write a function that takes in two parameters:
 * -A string that can be any word or phrase.
 * -A second string.
 * Your function should then check to see if your second string is an anagram of your first string.
 * 
 * FIBONACCI NUMBERS
 * Write a function that accepts a number and returns the number at that position in the fibonnaci sequence.
 * 
 * FIZZBUZZ
 * Fizzbuzz is a toy problem that counts upward. For every number that is:
 * -divisible by 3, the program prints fizz.
 * -divisible by 5, the program prints buzz.
 * -divisible by both 3 and 5, the program prints fizzbuzz.
 * -all other numbers are printed regularly.
 * Write a function that accepts a number to which the program will count. Implement fizzbuzz.
 */
public class Main {
	private static final String INT_ERRROR_MESSAGE = "Value must be a positive integer greater than zero.";
	private static final String TRY_AGAIN_MESSAGE = "Would you like to try again? y/n: ";
	private static final String INVALID_SELECTION_MESSAGE = "Invalid selection";
	private static Scanner consoleReader;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean runProgram = true;
		consoleReader = new Scanner(System.in);
		
		while(runProgram) {
			System.out.println("*** PROJECT 1 ***");
			System.out.println("Please enter the number of your selection:");
			System.out.println("********************");
			System.out.println("1 >>>> Anagram Detection");
			System.out.println("2 >>>> Fibonacci Numbers");
			System.out.println("3 >>>> FizzBuzz");
			System.out.println("4 >>>> Exit Program");
			
			try {
				int selection = consoleReader.nextInt();
				
				switch(selection) {
				case 1:
					//anagram detection
					getAnagram();
					break;
				case 2:
					//fibonacci numbers
					getFibonacci();
					break;
				case 3:
					//fizzbuzz
					getFizzBuzz();
					break;
				case 4:
					//end program
					runProgram = false;
					break;
				default:
					System.out.println(INVALID_SELECTION_MESSAGE);
						
				}
				
			}
			catch (InputMismatchException e) {
				System.out.println(INVALID_SELECTION_MESSAGE);
				consoleReader.nextLine();
			}
			
			
		}
		
		consoleReader.close();
	}
	
	/**
	 * UI for handling fizzbuzz operation
	 */
	private static void getFizzBuzz() {
		int count = 0;
		boolean tryAgain = false;
		do {
			System.out.println("Enter a number you wish to count to:");
			try {
				count = consoleReader.nextInt();

				if (count <= 0)
					throw new InputMismatchException();
				else
					FizzBuzz.doFizzBuzz(count);
				
			} catch (InputMismatchException e) {
				System.out.println(INT_ERRROR_MESSAGE);
				
			} 
			
			consoleReader.nextLine();
			tryAgain = yesOrNoResponse(TRY_AGAIN_MESSAGE);
			
		} while (tryAgain);
		
		
		
	}
	
	/**
	 * UI for handling anagram operation
	 */
	private static void getAnagram() {
		String sourcePhrase;
		String anagramCandidate;
		boolean tryAgain = false;
		
		consoleReader.nextLine();
		
		do {
				//get strings to compare
				System.out.println("Enter a word or phrase:");
				sourcePhrase = consoleReader.nextLine();
				System.out.println("Enter phrase to check for anagram");
				anagramCandidate = consoleReader.nextLine();
				
				
				if(Anagram.isAnagram(sourcePhrase, anagramCandidate)) {
					System.out.println(anagramCandidate + " is an anagram of " + sourcePhrase +".");
				}
				else{	
					System.out.println(anagramCandidate + " is NOT an anagram of " + sourcePhrase + ".");	
				}
				
				tryAgain = yesOrNoResponse(TRY_AGAIN_MESSAGE);
				
		}while(tryAgain);
	}
	
	/**
	 * UI for handling fibonacci operation
	 */
	private static void getFibonacci() {
		int num = 0;
		long result = 0;
		boolean tryAgain = false;
		
		do {
		
		try {
			System.out.println("Enter the number of the sequence position you wish to see:");
			num = consoleReader.nextInt();
			result = Fibonacci.doFibonacci(0, 1, num);
			
			System.out.println("At position " + num + " of the fibonacci sequence, the number is: " + String.format("%,d", result));
			consoleReader.nextLine();
		}
		catch(InputMismatchException e) {
			System.out.println(INT_ERRROR_MESSAGE);
			consoleReader.nextLine();
		}
		
		tryAgain = yesOrNoResponse(TRY_AGAIN_MESSAGE);
		
		}while(tryAgain);
	}
	
	/**
	 * Presents a question to the user that requires a
	 * yes/no answer
	 * @param question Text of the question to ask
	 * @return true if answered yes, otherwise false.
	 */
	private static boolean yesOrNoResponse(String question){
		boolean isYes = false;
		boolean isValidInput = false;
		String input;
		
		while(!isValidInput){
			System.out.println(question);
			input = consoleReader.nextLine().toLowerCase().trim();
			
			if(input.equals("y") || input.equals("n")){
				isValidInput = true;
				if(input.equals("y")){
					isYes = true;
				}
			}
			else{
				System.out.println(INVALID_SELECTION_MESSAGE);
			}
		}
		return isYes;
	}

}

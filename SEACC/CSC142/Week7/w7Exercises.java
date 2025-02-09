/*CSC 142 - Week 7 practice exercises
 * Walter Holley III
 * 
 */

import java.util.*;
import java.io.*;


public class w7Exercises {
	public static final String NUMBER_FILE_NAME = "numberFile.txt";
	public static final String WORD_FILE_NAME = "wordFile.txt";
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	throws FileNotFoundException{
		Scanner numberFileReader = createNumberFile();
		Scanner wordFileReader = createWordFile();
		evenNumbers(numberFileReader);
		collapseSpaces(wordFileReader);
		
		
	
		//cleanup files
		File cleanupFile = new File(NUMBER_FILE_NAME);
		cleanupFile.delete();
		cleanupFile = new File(WORD_FILE_NAME);
		cleanupFile.delete();
		
		

	}
	
	//Reads numbers from a file, and prints details for the even numbers 
	//to the console
	public static void evenNumbers(Scanner fileInput){
		int totalNumbers = 0;
		int sum = 0;
		int totalEven = 0;
		double averageEvenNumber = 0;
			
		while(fileInput.hasNextInt()){
			int number = fileInput.nextInt();
			totalNumbers++;
			sum += number;
			
			if(number % 2 == 0){
				totalEven++;
			}		
		}
		
		fileInput.close();
		
		averageEvenNumber = (double)totalEven / (double)totalNumbers * 100;
				
		System.out.println("Total numbers: " + totalNumbers);
		System.out.println("Sum: " + sum);
		System.out.printf(totalEven + " even numbers (%.2f%%)", averageEvenNumber);
		System.out.println();
		
	}
	
	//Reads words from a file, and prints those words, 
	//separated by spaces, to the console.
	public static void collapseSpaces(Scanner fileInput){
		
		while(fileInput.hasNextLine()){
			String[] words = fileInput.nextLine().split("\\s+");
			
			for(int i = 0; i < words.length; i++){
				System.out.print(words[i] + " ");
			}
		}
		
		fileInput.close();
		
	}
	
	//Creates test number file
	private static Scanner createNumberFile()
	throws FileNotFoundException{
		String fileInput = "6 20 23 1523 98 55 43 62 85 44\n21 23 55 88 99 75";
		
		PrintStream fileToWrite = new PrintStream(new File(NUMBER_FILE_NAME));
		
		fileToWrite.print(fileInput);
		fileToWrite.close();
		
		return new Scanner(new File(NUMBER_FILE_NAME));
	}
	
	//Creates test word file 
	private static Scanner createWordFile()
	throws FileNotFoundException{
		PrintStream fileToWrite = new PrintStream(new File(WORD_FILE_NAME));
		fileToWrite.println("many     spaces         on                      this   line!");
		fileToWrite.println("so");
		fileToWrite.println("many");
		fileToWrite.println("spaces");
		fileToWrite.println("on");
		fileToWrite.println("this");
		fileToWrite.println("line!!");
		
		fileToWrite.flush();
		fileToWrite.close();
		
		return new Scanner(new File(WORD_FILE_NAME));
		
		
	}

}

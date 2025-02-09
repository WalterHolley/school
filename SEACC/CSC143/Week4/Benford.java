/**
 * CSC 143 - Assignment 2: Benford's Law
 * @author Walter Holley III
 * Benford Object parses through text files looking for the first
 * significant Digit(1 - 9) on each line, and averages their occurrences.
 * Can also tell the user if the data collected adheres to Benford's Law.
 * 
 */
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Benford {
	private ArrayList<Integer> benfordList;
	
	public Benford(){
		benfordList = new ArrayList<Integer>();
	}
	
	/**
	 * Takes in a file and determines if data follows Benford's law
	 * @param fileName File object
	 * @throws FileNotFoundException If the file is not found.
	 * @throws IOException If any issue occurs while reading the file.
	 */
	public Benford(File fileName) throws FileNotFoundException, IOException{
		readCounts(fileName);
	}
	
	/**
	 * Reads file data and determines if the data follows Benford's Law.
	 * @param fileName File object
	 * @throws FileNotFoundException If the file is not found.
	 * @throws IOException If any issue occurs while reading the file.
	 */
	public void readCounts(File fileName)throws FileNotFoundException, IOException{
		Scanner fileReader = new Scanner(fileName);
		benfordList = new ArrayList<Integer>();
		
		while(fileReader.hasNextLine()){
			int number = getFirstNumber(fileReader.nextLine());
			if(number > 0 && number < 10){
				benfordList.add(number);
			}			
		}
		fileReader.close();
		
		//check to ensure data was read into ArrayList
		if(this.benfordList.size() == 0){
			throw new IOException();
		}	
	}
	
	/**
	 * Provides and array if data based on the significant number 
	 * occurrences of the most recently read dataset.
	 * @return double array indicating the occurrence percentage of
	 * each significant digit.  Index 0 = occurence percentage for
	 * the number 1.
	 */
	public double[] benfordPercents(){
		double[] percentages = {0,0,0,0,0,0,0,0,0};
		for(int number : benfordList){
			percentages[number - 1]++;
		}
		
		for(int i = 0; i < percentages.length; i++){
			percentages[i] = percentages[i] / benfordList.size() * 100;
		}
		
		return percentages;
		
	}
	
	/**
	 * Determines if the data within the Benford object matches
	 * the criteria of Benford's law.
	 * @return true if the dataset matches Benford frequencies.
	 * Otherwise, false.
	 */
	public boolean isBenfordsLaw(){
		boolean isCompliant = true;
		double[] percentages = benfordPercents();
		
		for(int i = 0; i < percentages.length - 1; i++){
			if(percentages[i] <  percentages[i+1]){
				isCompliant = false;
				break;				
			}
			else if(i > 0){
				if(percentages[i] >= percentages[i - 1]){
					isCompliant = false;
					break;
				}
			}		
		}	
		return isCompliant;
	}
	
	/**
	 * Returns the significant number from a string of text.
	 * @param textString the string of text containing the significant number
	 * @return an int of the significant number, otherwise zero if no
	 * significant number is found.
	 */
	private int getFirstNumber(String textString){
		int firstNumber = 0;
		for(int i = 0; i < textString.length(); i++){
			if((int)textString.charAt(i) >= 49 && (int)textString.charAt(i) <= 57){
				firstNumber = (int)textString.charAt(i) - 48;
				break;
			}
		}	
		return firstNumber;
	}
}

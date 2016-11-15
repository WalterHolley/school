/* CSC 142 - Assignment Four
 * Walter Holley III
 * DateClient.java
 * Handles the UI related functions for the
 * assignment.  Gathers and validates data
 * to be processed by the Date Object.
 */
package com.assignment.four;

import java.util.Scanner;

public class DateClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean endProgram = false;
		Scanner console = new Scanner(System.in);
		
		while(!endProgram){
			
		}

	}
	
	public static void getDates(Scanner console){
		int[]todaysDate = new int[3];
		int[]birthDate = new int[3];
		boolean inputIsValid = false;
		
		while(!inputIsValid){
			System.out.print("What is today's date(month day year)? ");
			try{
				todaysDate[0] = console.nextInt();
				todaysDate[1] = console.nextInt();
				todaysDate[2] = console.nextInt();
				
				System.out.print("What is today's date(month day year)? ");
				birthDate[0] = console.nextInt();
				birthDate[1] = console.nextInt();
				birthDate[2] = console.nextInt();
			}
			catch(Exception e){
				System.out.println("Input is invalid, please try again");
				continue;
			}
			
			inputIsValid = validateInput(todaysDate, birthDate);
		}
		
	}
	
	public static boolean validateInput(int[] date1, int[] date2){
		boolean isValid = true;
		
		if(!isValidDay(date1[0]) || !isValidDay(date2[0])){
			isValid = false;
		}
		else if(!isValidMonth(date1[1]) || !isValidMonth(date2[1])){
			isValid = false;
		}
		else if(!isValidYear(date1[2]) || !isValidYear(date2[2])){
			isValid = false;
		}
		else if(date1[2] < date2[2]){
			isValid = false;
		}
		else if(date1[2] == date2[2]){
			if(date1[1] < date2[1]){
				isValid = false;
			}
			else if(date1[0] < date2[0]){
				isValid = false;
			}
		}
		else if(date1[1] == date2[1]){
			if(date1[0] < date2[0]){
				isValid = false;
			}
		}
		return isValid;
	}
	
	private static boolean isValidDay(int day){
		return (day > 0 && day <= 31);
	}
	
	private static boolean isValidMonth(int month){
		return (month > 0 && month <= 12);
	}
	
	private static boolean isValidYear(int year){
		return (year > 0);
	}

}

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

private static final String INPUT_ERROR_MSG = "Input is invalid, please try again";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean endProgram = false;

		Scanner console = new Scanner(System.in);
		
		while(!endProgram){
			boolean validResponse = false;
			Date[] dates = getDates(console);
			displayResults(dates);
			System.out.println();
			
			while(!validResponse){
				System.out.print("Check another date(y/n)?:");
				char response = console.next().toLowerCase().charAt(0);
				
				if(response != 'y' && response != 'n'){
					System.out.println(INPUT_ERROR_MSG);
				}
				else{
					validResponse = true;
					if(response == 'n'){
						endProgram = true;
					}
				}
			}		
		}

	}
	
	//Gathers and validates date input from the user
	public static Date[] getDates(Scanner console){
		int[]todaysDate = new int[3];
		int[]birthDate = new int[3];
		Date[] result = new Date[2];
		boolean inputIsValid = false;
		
		
		while(!inputIsValid){
			System.out.print("What is today's date(month day year)? ");
			try{
				todaysDate[0] = console.nextInt();
				todaysDate[1] = console.nextInt();
				todaysDate[2] = console.nextInt();
				
				System.out.print("What is your birthday(month day year)? ");
				birthDate[0] = console.nextInt();
				birthDate[1] = console.nextInt();
				birthDate[2] = console.nextInt();
			}
			catch(Exception e){
				System.out.println(INPUT_ERROR_MSG);
				continue;
			}
			
			if(!validateInput(todaysDate, birthDate)){
				System.out.println(INPUT_ERROR_MSG);
			}
			else{
				inputIsValid = true;
			}
		}
		result[0] = new Date(todaysDate[2], todaysDate[0], todaysDate[1]);
		result[1] = new Date(birthDate[2], birthDate[0], birthDate[1]);
		
		return result;
		
	}
	
	//Displays results gathered from user's date input
	public static void displayResults(Date[] dates){
		System.out.printf("You were born on %s/%s/%s, which was a %s.\n", 
				dates[1].getYear(), dates[1].getMonth(), dates[1].getDay(), dates[1].getDayOfTheWeek());
		
		if(dates[1].isLeapYear()){
			System.out.printf("%s was a leap year.\n", dates[1].getYear());
		}
		
		System.out.printf("You are %s days old.\n", dates[1].advanceTo(dates[0]));
	}
	
	//Validates date input from the user
	public static boolean validateInput(int[] date1, int[] date2){
		boolean isValid = true;
		
		if(!isValidYear(date1[2]) || !isValidYear(date2[2])){
			isValid = false;
		}
		else if(!isValidMonth(date1[0]) || !isValidMonth(date2[0])){
			isValid = false;
		}
		else if(!isValidDay(date1[1], date1[0], date1[2]) || !isValidDay(date2[1], date2[0], date2[2])){
			isValid = false;
		}
		else if(date1[2] < date2[2]){
			isValid = false;
		}
		else if(date1[2] == date2[2]){
			if(date1[0] < date2[0]){
				isValid = false;
			}
			else if(date1[1] < date2[1]){
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
	
	//determines if the day selected is valid
	private static boolean isValidDay(int day, int month, int year){
		Date testDate = new Date(year, month, day);
		return (day > 0 && day <= testDate.getDaysInMonth());
	}
	
	//determines if the month selected is valid
	private static boolean isValidMonth(int month){
		return (month > 0 && month <= 12);
	}
	
	//determines if the year selected is valid
	private static boolean isValidYear(int year){
		return (year >= Date.MinDate.getYear());
	}

}

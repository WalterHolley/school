/*CSC143 - Walter Holley III
* Weekly Problem 2
* Write an application that determines if the provided code is a valid EAN 13
* Barcode.
* Barcode Information: https://en.wikipedia.org/wiki/International_Article_Number
*/

import java.util.Scanner;

public class WeeklyProblem2 {

	private static final String ERROR_MSG = "The code entered is not valid";

	public static void main(String[] args) {
 
		Scanner console = new Scanner(System.in);
		System.out.println("To leave, type 'q' or 'quit'");
		while(true){
			String value = "";
			System.out.println("Enter 13 digit numeric code");
			value = console.nextLine();

			if(value.toLowerCase().equals("q") || value.toLowerCase().equals("quit")){
				break;
			}

			if(!validateInput(value)){
				System.out.println(ERROR_MSG);
			}
			else if(!validateEAN13(value)){
				System.out.println(ERROR_MSG);
			}
			else{
				System.out.println("The code entered is valid");
			}
		}
		console.close();
		System.out.println("Thanks for using this application.");

	}
 
	//determines if the input received is valid
	private static boolean validateInput(String input){
		boolean isValid = false;
		try{
			if(input.length() != 13){
				throw new NumberFormatException();
			}	
			for(int i = 0; i < 13; i++){
				int value = Integer.parseInt(input.split("")[i]);
			}
			isValid = true;
		}
		catch(NumberFormatException ex){
			//nothing to handle, just skips
			//changing return value to true
		}
		return isValid;

	}

	//determines if the input received is a valid EAN 13 bar code
	private static boolean validateEAN13(String digits){
		boolean isValid = false;
		int checkSum = 0;

		//Determine checksum
		for(int i = 0; i < 12; i++){
			int value = Integer.parseInt(digits.split("")[i]);
			if(i % 2 == 0){
				checkSum += value * 1;
			}
			else{
				checkSum += value * 3;
			}
		}

		//validate checkSum against check digit
		double roundedCheckSum = Math.ceil(checkSum * Math.pow(10, -Math.floor(Math.log10(checkSum)))) / Math.pow(10, -Math.floor(Math.log10(checkSum)));
		int checkDigit = (int)roundedCheckSum - checkSum;
		if(checkDigit == Integer.parseInt(digits.split("")[12])){
			isValid = true;
		}
		return isValid;
	}
}
import java.util.Scanner;

public class WeeklyProblem2 {
	private static final String ERROR_MSG = "The code entered is not valid";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scanner console = new Scanner(System.in);
		
		while(true){
			int[] barcodeInput = new int[13];
			int digitCount = 0;
			System.out.println("Enter 13 digit numeric code");
			// TODO change input ti string.  int is too small
			while(console.hasNext()){
				if(console.hasNextBigInteger()){
					barcodeInput[digitCount] = scanner.nextInt();
					digitCount++;
					
				
				}
				
			}
			if(digitCount != 12){
				System.out.println(ERROR_MSG);
			}
			else{
				if(!validateEAN13(barcodeInput)){
					System.out.println(ERROR_MSG);
				}
				else{
					System.out.println("The code entered is valid");
				}
			}
		}
		

	}
	
	//determines if the input received is a valid EAN 13 bar code
	private static boolean validateEAN13(int[] digits){
		boolean isValid = false;
		int checkSum = 0;
		
		//Determine checksum 
		for(int i = 0; i < 11; i++){					
			if(i % 2 == 0){
				checkSum += digits[i] * 1;
			}
			else{
				checkSum += digits[i] * 3;
			}
		}
		
		//validate checkSum against check digit
		int checkDigit = (int)Math.round(Math.ceil((double)checkSum) - checkSum);
		if(checkDigit == digits[12]){
			isValid = false;
		}
		
		return isValid;
	}


}

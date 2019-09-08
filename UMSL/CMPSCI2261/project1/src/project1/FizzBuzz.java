package project1;

public class FizzBuzz {
	
	/**
	 * Performs the FizzBuzz operation explained in the main.java file.
	 * If a number in the sequence is divisible by 3, fizz is printed, if divisible by 5,
	 * buzz is printed, if divisible by 3 and 5, fizzbuzz is printed, otherwise only the number is printed.
	 * @param count the number the fizzbuzz sequence will count to.
	 */
	public static void doFizzBuzz(int count) {
		
		for(int i = 0; i <= count; ++i) {
			boolean writeNum = true;
			
			if( i >= 3) {
				
				if(i % 3 == 0) {
					System.out.print("Fizz");
					writeNum = false;
				}
				
				if( i >= 5) {
					if(i % 5 == 0) {
						System.out.print("Buzz");
						writeNum = false;
					}
				}
			}
			
			if(writeNum) {
				System.out.print(i);
			}
			
			System.out.println();
		}

	}

}

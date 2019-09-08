package project1;

public class Fibonacci {
	/**
	 * Performs the fibonacci sequence
	 * NOTE:  as the range gets higher, the risk for incorrect values is greater.
	 * This is due in part to limitations of the return type.
	 * @param num1 the first number in the sequence
	 * @param num2 the second number in the sequence
	 * @param position the location of the fibonacci value you wish to return
	 * @return value located at given position
	 */
	public static long doFibonacci(long num1, long num2, int position) {
		long result = num1 + num2;
		
		if(num1 == 0 && num2 == 1) {
			if(position <= 3) {
				if(position == 1)
					result = num1;
			}
			position = position - 3;
		}
		else 
			position--;
		
		if(position > 0)
			result = doFibonacci(num2, result, position);
		
		return result;
	}
}

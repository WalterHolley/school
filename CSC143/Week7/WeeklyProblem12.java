import java.util.Arrays;

/**
 * CSC 143 - Weekly Problem 12
 * @author Walter Holley III
 * From the assignment description:
 * 1. Write a recursive function that computes the sum of all
 * numbers from 1 to n, where n is given as parameter.
 * 2. Write a recursive function that finds and returns the minimum
 * element in an array, where the array and its size are given as
 * parameters.
 * 3. Write a recursive function that computes and returns the sum of
 * all elements in an array, where the array and its size are given
 * as parameters.
 *
 */
public class WeeklyProblem12 {
	private static final String INDEX_ERROR_MSG = "The reported size of the array was incompatible with the actual array.";
	public static void main(String[] args) {
		int[] testArray = {2,3,1,4,5, 100, 67, -13};
		int testLengthProblem2 = 8;
		int testLengthProblem3 = 5;
		int testInt = -4;
		System.out.printf("PROBLEM 1: Test number: %s\nResult: %s\n\n", testInt, recursiveSum(testInt));
		
		try{
			System.out.printf("PROBLEM 2:\nTest array: %s\nTest Length: %s\n", Arrays.toString(testArray), testLengthProblem2);
			System.out.printf("Result: %s\n\n",recursiveMinimum(testArray, testLengthProblem2));	
		}
		catch(IndexOutOfBoundsException ex){
			System.out.println(INDEX_ERROR_MSG);
		}
		
		try{
			System.out.printf("PROBLEM 3:\nTest array: %s\nTest Length: %s\n", Arrays.toString(testArray), testLengthProblem3);
			System.out.printf("Result: %s\n\n",recursiveArraySum(testArray, testLengthProblem3));	
		}
		catch(IndexOutOfBoundsException ex){
			System.out.println(INDEX_ERROR_MSG);
		}
		
		System.out.println("Exiting Program");
	}	
	/**
	 * Computes the sum of all numbers from 1 to n
	 * @param n the bounds of the sum
	 * @return sum of numbers from 1 to n
	 */
	private static int recursiveSum(int n){
		int sum = 0;
		sum += n;
		if(n > 1){
			n--;
			sum += recursiveSum(n);
		}
		else if(n < 1){
			n++;
			sum += recursiveSum(n);
		}
		return sum;
	}
	
	/**
	 * Returns the lowest number in the array
	 * @param numberArray the array of numbers to check
	 * @param arraySize size of the number array.  can be smaller than the actual array
	 * but must be greater than zero
	 * @return int of the smallest number in the array
	 * @throws IndexOutOfBoundsException if the arraySize value is larger than the actual array,
	 * or less than/equal to zero
	 */
	private static int recursiveMinimum(int[] numberArray, int arraySize) throws IndexOutOfBoundsException{
		Integer minimum = null;
		
		if(arraySize > numberArray.length || arraySize <= 0){
			throw new IndexOutOfBoundsException();
		}	
		if(arraySize == 1){
			minimum = numberArray[0];
		}
		else if(arraySize % 2 != 0){
			int[] tempArray = Arrays.copyOfRange(numberArray, 0, arraySize - 1);
			int newMinimum = recursiveMinimum(tempArray, arraySize - 1);
			
			if(newMinimum > numberArray[arraySize - 1]){
				minimum = numberArray[arraySize - 1];
			}
			else{
				minimum = newMinimum;
			}
		}
		else{
			int newArraysize = arraySize / 2;
			int minimum1 = recursiveMinimum(Arrays.copyOfRange(numberArray, 0, newArraysize), newArraysize);
			int minimum2 = recursiveMinimum(Arrays.copyOfRange(numberArray, newArraysize, arraySize), newArraysize);
			
			if(minimum1 > minimum2){
				minimum = minimum2;
			}
			else{
				minimum = minimum1;
			}
		}	
		return minimum;
	}
	
	/**
	 * Determines the sum of all elements in the array
	 * @param numberArray the int array to be calculated
	 * @param arraySize the size of the array to be calculated
	 * @return the sum of the array
	 * @throws IndexOutOfBoundsException if the arraySize is greater than the
	 * length of numberArray, or arraySize is less than/equal to zero
	 */
	private static int recursiveArraySum(int[] numberArray, int arraySize) throws IndexOutOfBoundsException{
		int sum = 0;
		
		if(arraySize > numberArray.length || arraySize <= 0){
			throw new IndexOutOfBoundsException();
		}	
		if(arraySize == 1){
			sum += numberArray[0];
		}
		else if(arraySize % 2 != 0){
			sum += numberArray[arraySize - 1];
			sum += recursiveArraySum(Arrays.copyOfRange(numberArray, 0, arraySize - 1), arraySize - 1);
		}
		else{
			int newArraysize = arraySize / 2;
			sum += recursiveArraySum(Arrays.copyOfRange(numberArray, 0, newArraysize), newArraysize);
			sum += recursiveArraySum(Arrays.copyOfRange(numberArray, newArraysize, arraySize), newArraysize);			
		}	
		return sum;
	}
}

package com.umsl.cmpsci3130;

import java.math.BigInteger;
import java.util.Stack;

public class Project1 {
	
	//Default matrix for logarithmic algorithm
	private static BigInteger[][] baseMatrix = {{new BigInteger("0"),new BigInteger("1")},
			{new BigInteger("1"), new BigInteger("1")}};
	
	private static BigInteger[][] placeHolder;

	public static void main(String[] args) {

		Test();

	}
	
	/**
	 * Linear implementation of the fibonacci sequence
	 * @param n the number of places in the sequence to count
	 * @return total answer to the sequence;
	 */
	public static BigInteger linearFibonacci(int n) {
		BigInteger bigInt;
		BigInteger previousFib1 = new BigInteger("0");
		BigInteger previousFib2 = new BigInteger("0");

		if(n >= 1) {
			bigInt = new BigInteger("1");
		}
		else {
			bigInt = new BigInteger("0");
		}
		
		for(int i = 2; i <= n; i++) {
			previousFib2 = previousFib1;
			previousFib1 = bigInt;
			bigInt = previousFib1.add(previousFib2);
		}
		
		return bigInt;
	}
	
	/**
	 * Logarithmic implementation of fibonacci sequence
	 * @param n position of fibonacci sequence
	 * @return BigInteger of value at n position of sequence
	 */
	public static BigInteger logarithmicFibonacci(int n) {
		BigInteger result = null;
		Stack<BigInteger[][]> matrixList = new Stack<BigInteger[][]>();
		String binaryString = Integer.toBinaryString(n);
		int targetPower = 0;
		
		if(n <= 0) {
			result = new BigInteger("0");
		}
		else if(n == 1) {
			result = new BigInteger("1");
		}
		else {
			for(int i = binaryString.length() - 1; i >= 0; i--) {
				
				if(binaryString.charAt(i) == '0') {
					continue;
				}
				else if(binaryString.charAt(i) == '1' && i == (binaryString.length() - 1)) {
					matrixList.push(baseMatrix);
					continue;
				}
				else {
					targetPower = (int) Math.pow(2, (binaryString.length() - 1 - i));
				}
						
				matrixList.push(getMatrixPower(baseMatrix, targetPower));
				//clear placeholder
				placeHolder = null;
			}
			
			if(result == null) {
				result = getMatrixAnswer(matrixList)[0][1];
			}
			
		}
		
		return result;
	}
	
	/**
	 * Raises a given matrix by the given power
	 * @param matrix The matrix to raise
	 * @param power exponent of matrix
	 * @return 2 dimensional BigInteger array representation of a matrix
	 */
	private static BigInteger[][] getMatrixPower(BigInteger[][] matrix, int power){
		BigInteger[][] result;
		
		if( power == 1) {
			result = matrix;
		}
		else if(placeHolder != null) {
			result = placeHolder;
		}
		else {
			matrix = getMatrixPower(matrix, power / 2);
			result = multiplyMatrices(matrix, matrix);
			placeHolder = result;
		}
		
		return result;
		
	}
	
	/**
	 * Multiplies two matrices together
	 * @param matrix1 matrix on the left side of the equation
	 * @param matrix2 matrix on the right side of the equation
	 * @return dot product result
	 */
	private static BigInteger[][] multiplyMatrices(BigInteger[][] matrix1, BigInteger[][] matrix2){
		BigInteger[][] result = new BigInteger[2][2];
		
			result[0][0] = matrix1[0][0].multiply(matrix2[0][0]).add(matrix1[0][1].multiply(matrix2[1][0]));
			result[0][1] = matrix1[0][0].multiply(matrix2[0][1]).add(matrix1[0][1].multiply(matrix2[1][1]));
			result[1][0] = matrix1[1][0].multiply(matrix2[0][0]).add(matrix1[1][1].multiply(matrix2[1][0]));
			result[1][1] = matrix1[1][0].multiply(matrix2[0][1]).add(matrix1[1][1].multiply(matrix2[1][1]));
		
		
		return result;
		
	}
	
	/**
	 * Multiplies matrices in a list and returns the result
	 * @param matrixList
	 * @return 2 dimensional BigInteger array representation of a matrix
	 */
	private static BigInteger[][] getMatrixAnswer(Stack<BigInteger[][]> matrixList) {
		BigInteger[][] result;

		while(matrixList.size() > 1) {
			result = multiplyMatrices(matrixList.pop(), matrixList.pop());
			matrixList.push(result);
		}
		
		
		
		return matrixList.pop();
	}
	
	/**
	 * Calculates the average value for an array
	 * of long values
	 * @param values
	 * @return average of all values in long array
	 */
	private static long average(long[] values) {
		long result = 0;
		if(values.length > 0) {
			for(int i = 0; i < values.length; i++) {
				result += values[i];
			}
			
			result = result / values.length;
		}
		
		return result;
	}
	
	public static void Test() {
		long[] linearAlgorithmTimes = new long[6];
		long[] logAlgorithmTimes = new long[6];
		BigInteger[] linearResults = new BigInteger[6];
		BigInteger[] logResults = new BigInteger[6];
		
		int[] problemSizes = {8,16,32,(int)Math.pow(2, 10),(int)Math.pow(2, 12),(int)Math.pow(2, 15) };
		
		//get Test Results
		for(int i = 0; i < linearAlgorithmTimes.length; i++) {
			long startTime = System.nanoTime();
			linearResults[i] = linearFibonacci(problemSizes[i]);
			long endTime = System.nanoTime();
			linearAlgorithmTimes[i] = endTime - startTime;
			
			startTime = System.nanoTime();
			logResults[i] = logarithmicFibonacci(problemSizes[i]);
			endTime = System.nanoTime();
			logAlgorithmTimes[i] = endTime - startTime;
		}
		
		System.out.println("=====FIBONACCI ALGORITHM TEST=====");
		
		System.out.print("In this program, we're testing the execution time \n"
				+ "of two different fibonacci sequence implementations. \n"
				+ "One with a linear O(n) time  complexity, another with \n"
				+ "logarithmic O(logn) time complexity.\n \n");
		
		System.out.println("Lets look at a few small requests for numbers \n"
				+ "in early positions of the fibonacci sequence.");
		
		for(int i = 0; i <= 2; i++) {
			System.out.println(String.format("===TRIAL %d===\n", i + 1));
			System.out.println(String.format("Fibonacci Sequence Position: %d", problemSizes[i]));
			System.out.println(String.format("Linear Algorithm Answer: %s", linearResults[i].toString()));
			System.out.println(String.format("Logarithmic Algorithm Answer: %s\n", logResults[i].toString()));
			System.out.println(String.format("Linear Algorithm time: %d nanoseconds", linearAlgorithmTimes[i]));
			System.out.println(String.format("Logarithmic Algorithm time: %d nanoseconds", logAlgorithmTimes[i]));
			System.out.print("\n\n");
			
			
		}
		
		System.out.println("Now, lets look at a few larger requests for numbers \n"
				+ "in later positions of the fibonacci sequence.");
		
		for(int i = 3; i <= 5; i++) {
			System.out.println(String.format("===TRIAL %d===\n", i + 1));
			System.out.println(String.format("Fibonacci Sequence Position: %d", problemSizes[i]));
			System.out.println(String.format("Linear Algorithm Answer: %s", linearResults[i].toString()));
			System.out.println(String.format("Logarithmic Algorithm Answer: %s\n", logResults[i].toString()));
			System.out.println(String.format("Linear Algorithm time: %d nanoseconds", linearAlgorithmTimes[i]));
			System.out.println(String.format("Logarithmic Algorithm time: %d nanoseconds", logAlgorithmTimes[i]));
			System.out.print("\n\n");
			
			
		}
		
		System.out.println("===RESULTS===");
		System.out.println(String.format("Average linear algorithm time: %d nanoseconds", average(linearAlgorithmTimes)));
		System.out.println(String.format("Average logarithmic algorithm time: %d nanoseconds", average(logAlgorithmTimes)));
		System.out.print("\n\n");
		System.out.println("While the linear algorithm worked well in earlier positions, the \n"
				+ "logarithmic function proved more efficient as the datasets grew larger.\n"
				+ "Overall, the logarithmic algorithm has the better execution time.");
		
	}

}

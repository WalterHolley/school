package com.umsl.cmpsci3130;

import java.math.BigInteger;
import java.util.Stack;

public class Project1 {
	
	private static BigInteger[][] baseMatrix = {{new BigInteger("1"),new BigInteger("1")},
			{new BigInteger("1"), new BigInteger("0")}};
	
	private static BigInteger[][] placeHolder;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(linearFibonacci(6).toString());
		System.out.println(logarithmicFibonacci(6).toString());

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
	
	public static BigInteger logarithmicFibonacci(int n) {
		BigInteger result;
		BigInteger[][] matrixResult;
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
			}
			
			
			result = getMatrixAnswer(matrixList)[0][0];
		}
		
		return result;
	}
	
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
	
	private static BigInteger[][] getMatrixAnswer(Stack<BigInteger[][]> matrixList) {
		BigInteger[][] result;

		while(matrixList.size() > 1) {
			result = multiplyMatrices(matrixList.pop(), matrixList.pop());
			matrixList.push(result);
		}
		
		
		
		return matrixList.pop();
	}

}

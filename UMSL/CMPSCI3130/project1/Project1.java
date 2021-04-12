package com.umsl.cmpsci3130;

import java.math.BigInteger;

public class Project1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.print(linearFibonacci(8));

	}
	
	/**
	 * Linear implementation of the fibonacci sequence
	 * @param n the number of places in the sequence to count
	 * @return total answer to the sequence;
	 */
	private static BigInteger linearFibonacci(int n) {
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
	
	private static BigInteger logarithmicFibonacci(int n) {
		BigInteger bigInt = new BigInteger("0");
		
		if(n <= 0) {
			bigInt = new BigInteger("0");
		}
		else if(n == 1) {
			bigInt = new BigInteger("1");
		}
		
		return bigInt;
	}

}

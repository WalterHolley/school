package project2;

/**
 * BankAccount.java
 * @author Walter Holley III
 * This class represents an investment bank
 * account owned by a user.
 */
public class BankAccount {
	private double _funds;
	
	public BankAccount() {

	}
	
	/**
	 * Deposits funds into the bank account
	 * @param amount the amount to deposit
	 * @return false if the amount is negative or smaller than 0.01
	 */
	public boolean depositFunds(double amount) {
		boolean result = false;
		
		if(amount >= 0.01) {
			_funds += amount;
			result = true;
		}

		return result;
	}
	
	/**
	 * Withdraws funds from the bank account
	 * @param amount the amount you wish to withdraw
	 * @return false if the amount is negative or overdraws the account
	 */
	public boolean withdrawFunds(double amount) {
		boolean result = false;
		if(amount > 0.00 && _funds - amount >= 0) {
			_funds -= amount;
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return balance of the bank account
	 */
	public double getBalance() {
		return _funds;
	}

}

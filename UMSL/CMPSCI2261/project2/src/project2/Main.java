package project2;

import java.util.InputMismatchException;
import java.util.Scanner;
import market.*;

/**
 * @author Walter Holley III
 * 
 *  Main.java
 *  
 *  Conditions for Main class:
 *  
 * Your main function must:
 *
 * Prompt the user with a menu asking whether they would like to:
 *
 * Buy a stock
 * Sell a stock
 * View a list of the stocks and their prices
 * Add "money" to their account
 * Print their current portfolio
 * Print their current assets (i.e. how much money they have in total from stocks).
 * 
 * If a user chooses to buy a stock whose symbol does not exist, reprompt the user to choose an already existing symbol.
 * In order to buy a stock the user must have adequate "funds" in their account.
 * At the end of each loop, run the provided function "marketVolatility()" (this function adds or subtracts an 
 * amount to each stock to simulate growth).
 *
 *@implNote The market package is provided by the course instructor, but has been modified to suit
 * the needs of this project.
 **/
public class Main {
	private static final String INT_ERROR_MESSAGE = "Value must be a positive integer greater than zero.";
	private static final String CANCEL_TRANS = "Transaction canceled.";
	private static final String MSG_SUCCESS = "Transaction successful.";
	private static final String MSG_FAIL = "Transaction failed.";
	private static final String INVALID_SELECTION_MESSAGE = "Invalid selection";
	private static Scanner consoleReader;
	private static Portfolio userPortfolio;

	public static void main(String[] args) {
		init();
		boolean runProgram = true;
		
		while(runProgram) {
			System.out.println("*** Portfolio Manager***");
			System.out.println("Please enter the number of your selection:");
			System.out.println("********************");
			System.out.println("1 >>>> Buy Stock");
			System.out.println("2 >>>> Sell Stock");
			System.out.println("3 >>>> View Stocks");
			System.out.println("4 >>>> Add money to account");
			System.out.println("5 >>>> Print Portfolio");
			System.out.println("6 >>>> Print Assets");
			System.out.println("7 >>>> Exit Program");
			
			try {
				int selection = consoleReader.nextInt();
				
				switch(selection) {
				case 1:
					processStocks(true);
					break;
				case 2:
					//sell stock
					processStocks(false);
					break;
				case 3:
					//view available stocks
					printStocks();
					break;
				case 4:
					//Add money
					addFunds();
					break;
				case 5:
					//view portfolio
					userPortfolio.printPortfolio();
					System.out.println("Press ENTER to continue");
					consoleReader.nextLine();
					consoleReader.nextLine();
					break;
				case 6:
					//view assets
					printAssets();
					break;
				case 7:
					//end program
					runProgram = false;
					break;
				default:
					System.out.println(INVALID_SELECTION_MESSAGE);
					
				}
			}
			catch(InputMismatchException ex) {
				System.out.println(INVALID_SELECTION_MESSAGE);
				consoleReader.nextLine();
			}
			//Refresh market prices
			if(runProgram) {
				Market.randomWalk();
			}
			
		}
		
		//close console reader
		consoleReader.close();

	}
	
	/**
	 * Initializes application
	 */
	private static void init() {
		consoleReader = new Scanner(System.in);
		userPortfolio = new Portfolio();
	
	}
	/**
	 * Prints a list of available stocks and their prices
	 */
	private static void printStocks() {
		//print header
		System.out.print(String.format("%-20s", "COMPANY"));
		System.out.print(String.format("%-6s", "SYM"));
		System.out.print(String.format("%-9s", "$(USD)"));
		System.out.println();
		
		Market.printMarket();
		System.out.println("Press ENTER to continue");
		consoleReader.nextLine();
		consoleReader.nextLine();
		
	}
	
	/**
	 * returns the price of a stock
	 * @param tickerSymbol the ticker symbol associated with the stock
	 * @return price of the requested stock, or -1 if the stock is not found
	 */
	private static double getTickerPrice(String tickerSymbol) {
		double price = -1;
		
		MarketStock stock = Market.getStock(tickerSymbol);
		if(stock != null) {
			price = stock.getPrice();
		}
		
		return price;
		
	}
	
	/**
	 * UI for buying and selling stocks
	 * @param isBuy if true, will process the transaction a stock purchase.
	 * if false, as a stock sale.
	 */
	private static void processStocks(boolean isBuy) {
		String tickerSymbol = null;
		double price = -1;
		int numberOfShares;
		boolean result = false;
		boolean validateTicker = true;
		String buyOrSell = "";
		consoleReader.nextLine();
		
		if(isBuy) {
			buyOrSell = "buy";
		}
		else {
			buyOrSell = "sell";
		}
		
		//ask for stock ticker, reprompt on failure
		do {
			System.out.println("Enter ticker symbol");
			
			tickerSymbol = consoleReader.nextLine();
			if(tickerSymbol.length() > 4) {
				System.out.println("Ticker symbol can only be up to 4 characters in length.");
				continue;
			}
			//Verify existence of stock
			price = getTickerPrice(tickerSymbol);
			
			if(price < 0) {
				System.out.println("Ticker symbol " + tickerSymbol.toUpperCase() + " does not exist.");
			}
			else {
				validateTicker = false;
			}			
		}while(validateTicker);
		
		//determine size of transaction
		System.out.println(String.format("Price per share(USD): $%,.2f", price));
		System.out.println("Enter number of shares to " + buyOrSell);
		try {
			numberOfShares = consoleReader.nextInt();
			if(isBuy) {
				result = userPortfolio.buyStock(tickerSymbol, numberOfShares);
			}
			else {
				result = userPortfolio.sellStock(tickerSymbol, numberOfShares);
			}		
		}
		catch(InputMismatchException ex) {
			System.out.println(INT_ERROR_MESSAGE);
			consoleReader.nextLine();
		}
			
		if(result) {
			System.out.println(MSG_SUCCESS);
		}
		else {
			System.out.println(MSG_FAIL);
		}
	}
	
	/**
	 * Adds funds to the portfolio bank account
	 */
	private static void addFunds() {
		double depositAmount;
		boolean result = false;
		
		System.out.println("Enter the amount of the deposit.");
		try {
			depositAmount = consoleReader.nextDouble();
			
			if(depositAmount >= 0.01) {
				result = userPortfolio.depositFunds(depositAmount);
				
				if(result) {
					System.out.println(MSG_SUCCESS);
				}
				else {
					System.out.println(MSG_FAIL);
				}
			}
			else {
				System.out.println("Deposit amount is insufficient.  Must be at least $0.01");
			}
		}
		catch(InputMismatchException ex) {
			System.out.println("Value must be number greater than or equal to $0.01");
			System.out.println(CANCEL_TRANS);
			consoleReader.nextLine();
		}
		
	}
	
	private static void printAssets() {
		System.out.println(String.format("Total value of currently held assets: $%,.2f", userPortfolio.getTotalStockValue()));
		System.out.println(String.format("Total cash in portfolio bank account: $%,.2f", userPortfolio.getCashBalance()));
		System.out.println(String.format("Total cash value of portfolio:        $%,.2f", userPortfolio.getCashBalance() + userPortfolio.getTotalStockValue()));
		
		System.out.println("Press ENTER to continue");
		consoleReader.nextLine();
		consoleReader.nextLine();
	}
	

	


}

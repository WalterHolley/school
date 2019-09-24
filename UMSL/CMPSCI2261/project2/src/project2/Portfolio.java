package project2;
import java.util.ArrayList;
import market.Market;
import market.MarketStock;
/**
 * Project 2 -> Portfolio.java
 * @author Walter Holley III
 * This class represents a porfolio held by the user.
 * Its purpose is to manage the stock and associated investment
 * bank account owned by the user.
 */

public class Portfolio {
	private ArrayList<Stock> _stockList;
	private BankAccount _account;
	private final String NSF_MESSAGE = "Insufficient funds for transaction";
	private final String STOCK_DNE_MESSAGE = "The requested stock does not exist";
	
	public Portfolio() {
		_account = new BankAccount();
		_stockList = new ArrayList<Stock>();
	}
	
	/**
	 * Processes a stock purchase transaction
	 * @param tickerSymbol the symbol for the stock you wish to buy
	 * @param numberOfShares the number of shares to purchase
	 * @return false if the purchase fails, otherwise true.
	 */
	public boolean buyStock(String tickerSymbol, int numberOfShares) {
		boolean result = false;
		double transactionPrice;
		MarketStock stock = getStockFromMarket(tickerSymbol);
		if(stock == null) {
			System.out.println(STOCK_DNE_MESSAGE);
		}
		else {
			transactionPrice = numberOfShares * stock.getPrice();
			if(_account.getBalance() < transactionPrice) {
				System.out.println(NSF_MESSAGE);
			}
			else {
				result = _account.withdrawFunds(transactionPrice);
			}
			
			if(result) {
				Stock updatedStock = getStockFromPortfolio(tickerSymbol);
				
				if(updatedStock == null) {
					updatedStock = new Stock(stock.getCompanyName(), stock.getTickerSymbol());
					updatedStock.addShares(numberOfShares);
					_stockList.add(updatedStock);
				}
				else {
					for(Stock s: _stockList) {
						if(s.getTickerSymbol().equals(tickerSymbol)) {
							s.addShares(numberOfShares);
							break;
						}
					}
				}
			}
			
		}
		return result;
	}
	
	/**
	 * Sells a stock from you portfolio, and deposits the funds
	 * into the bank account.
	 * @param tickerSymbol the stock you wish to sell from your portfolio
	 * @param numberOfShares the number of shares you wish to sell
	 * @return false if the transaction fails, otherwise true.
	 */
	public boolean sellStock(String tickerSymbol, int numberOfShares) {
		boolean result = false;
		Stock stock = getStockFromPortfolio(tickerSymbol);
		double salePrice;
		
		if(stock == null) {
			System.out.println(STOCK_DNE_MESSAGE + " in your portfolio");
		}
		else if(numberOfShares > stock.getStockOwned()) {
			System.out.println("You own fewer than the number of shares you wish to sell");
		}
		else {
			salePrice = numberOfShares * stock.getPricePerShare();
			
			if(stock.getStockOwned() - numberOfShares == 0) {
				result = _stockList.remove(stock);
			}
			else {
				result = _stockList.get(_stockList.indexOf(stock)).removeShares(numberOfShares);
			}
			
			if(result) {
				System.out.println("Stocks sold successfully");
				result = _account.depositFunds(salePrice);
			}
		
		}
		return result;
	}
	
	/**
	 * Adds funds to the portfolio bank account
	 * @param amount the amount to add
	 * @return false if deposit fails, otherwise true
	 */
	public boolean depositFunds(double amount){
		boolean result = false;
		
		if(amount > 0) {
			result = _account.depositFunds(amount);
		}
		
		return result;
	}
	
	/**
	 * Prints the portfolio
	 */
	public void printPortfolio() {
		//print bank account info
		System.out.println("BANK ACCOUNT");
		System.out.print(String.format("%-35s", "ACCT. BALANCE(USD)"));
		System.out.println(String.format("$%,.2f", _account.getBalance()));
		System.out.println("====================");
		System.out.println();
		
		//Print stocks
		if(_stockList.size() > 0) {
			printStockList();
		}
		
	}
	
	/**
	 * Returns total cash value of stock in portfolio.
	 * Value is based on the current price of each stock
	 * in the market.
	 */
	public double getTotalStockValue() {
		double totalValue = 0;
		for(Stock s : _stockList){
			totalValue += s.getPricePerShare()*s.getStockOwned();
		}
		
		return totalValue;
	}
	
	/**
	 * Retrieves balance from portfolio bank account
	 * @return cash amount in bank account
	 */
	public double getCashBalance() {
		return _account.getBalance();
	}
	/**
	 * Retrieves information on individual stocks from the market
	 * @param tickerSymbol the ticker symbol of the stock you wish to find
	 * @return MarketStock representation of a stock 
	 */
	private MarketStock getStockFromMarket(String tickerSymbol) {
		return Market.getStock(tickerSymbol);
	}
	
	/**
	 * Retrieves a stock object from the user's portfolio
	 * @param tickerSymbol ticker symbol of the stock object
	 * @return Stock representation of the stock, or null if it doesn't
	 * exist.
	 */
	private Stock getStockFromPortfolio(String tickerSymbol) {
		Stock _result = null;
		for(Stock stock : this._stockList) {
			if(stock.getTickerSymbol().equals(tickerSymbol.toUpperCase())) {
				_result = stock;
				break;
			}
		}
		
		return _result;
	}
	
	
	/**
	 * Prints the user's portfolio(List of stocks owned)
	 */
	private void printStockList() {
	
		//print stock header
		System.out.print(String.format("%-20s", "COMPANY NAME"));
		System.out.print(String.format("%-6s", "SYM"));
		System.out.println(String.format("%-9s", "#OWNED"));
		
		for(Stock s : _stockList) {
			System.out.print(String.format("%-20s", s.getCompanyName()));
			System.out.print(String.format("%-6s", s.getTickerSymbol()));
			System.out.println(String.format("%-9s", s.getStockOwned()));
		}
	}
}

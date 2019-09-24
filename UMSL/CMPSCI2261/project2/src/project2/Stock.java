package project2;
import market.Market;

/**
 * 
 * @author Walter Holley III
 * Stock.java
 * this class represents a stock asset that would 
 * be owned by a user.  Used to manage
 * stocks in the Portfolio class.
 *
 */
public class Stock {
	private String _companyName;
	private String _tickerSymbol;
	private int _totalOwned;
	private final String INT_ERROR_MESSAGE = "Value must be a positive integer greater than zero.";
	
	public Stock(String companyName, String tickerSymbol) {
		this._companyName = companyName;
		this._tickerSymbol = tickerSymbol;
		this._totalOwned = 0;
	}
	
	/**
	 * 
	 * @return String of the company name
	 */
	public String getCompanyName() {
		return _companyName;
	}
	
	/**
	 * 
	 * @return String of the stock ticker symbol
	 */
	public String getTickerSymbol() {
		return _tickerSymbol;
	}
	
	/**
	 * 
	 * @return integer of stock owned by the user
	 */
	public int getStockOwned() {
		return _totalOwned;
	}
	
	/**
	 * Adds to the total number of stock owned
	 * @param numberOfShares the number of shares to be added
	 * @return false on failed add, otherwise true.
	 */
	public boolean addShares(int numberOfShares) {
		boolean result = false;
		
		if(numberOfShares <= 0) {
			System.out.println(INT_ERROR_MESSAGE);
		}
		else {
			_totalOwned += numberOfShares;
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Subtracts from the total number of stock owned
	 * @param numberOfShares the number of shares to be removed
	 * @return false if an error occurs, otherwise true.
	 */
	public boolean removeShares(int numberOfShares) {
		boolean result = false;
		if(numberOfShares <= 0) {
			System.out.println(INT_ERROR_MESSAGE);
		}
		else {
			_totalOwned -= numberOfShares;
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Gets the current market price per share for this stock
	 * @return the price per share
	 */
	public double getPricePerShare() {
		return Market.getStock(this._tickerSymbol).getPrice();
	}
}

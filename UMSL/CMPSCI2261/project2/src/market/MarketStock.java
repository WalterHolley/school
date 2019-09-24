package market;

public class MarketStock {
	double price; 
	String identifier; 
	String companyName; 
	
	MarketStock(double price, String identifier, String companyName){
		this.price = price; 
		this.identifier = identifier; 
		this.companyName = companyName; 
	}
	
	/**
	 * 
	 * @return Retrieves the current price of the stock.
	 */
	public double getPrice() {
		return this.price;
	}
	
	/**
	 * 
	 * @return Retrieves the ticker symbol of the stock
	 */
	public String getTickerSymbol() {
		return this.identifier;
	}
	
	/**
	 * 
	 * @return Retrieves the company name of the stock.
	 */
	public String getCompanyName() {
		return this.companyName;
	}
}

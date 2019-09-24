package market;

import java.util.Date;
import java.util.Random;

public class Market {
	static MarketStock[] marketList = {
			new MarketStock(1.83, "CHK", "Chesapeake Energy"),
			new MarketStock(29.33, "BAC", "Bank of America"),
			new MarketStock(9.42, "F", "Ford Motor Company"),
			new MarketStock(1206.00, "GOOG", "Alphabet"),
			new MarketStock(37.36, "PFE", "Pfizer"),
			new MarketStock(1.36, "JCP", "JC Penney"),
			new MarketStock(48.33, "WFC", "Wells Fargo"),
			new MarketStock(17.11, "M", "Macy's"),
			new MarketStock(12.59, "CTL", "Centurytel"),
			new MarketStock(68.99, "C", "Citigroup"),
			new MarketStock(20.01, "HAL", "Haliburton"),
			new MarketStock(5.06, "GME", "Gamestop"),
			new MarketStock(176.35, "V", "Visa"),
			new MarketStock(116.76, "JPM", "JP Morgan Chase"),
			new MarketStock(59.71, "VZ", "Verizon"),
			new MarketStock(186.17, "FB", "Facebook"),
			new MarketStock(72.05, "XOM", "Exxon Mobil"),
			new MarketStock(43.56, "MS", "Morgan Stanley"),
			new MarketStock(1820.55, "AMZN", "Amazon.com Inc."),
			new MarketStock(54.39, "KO", "Coca Cola"),
			new MarketStock(25.82, "KR", "Kroger"),
			new MarketStock(135.72, "DIS", "Disney"),
			new MarketStock(41.84, "BSX",  "Boston Scientific"),
			new MarketStock(63.59, "CVS", "CVS"),
			new MarketStock(18.19, "HPQ", "Hewlett Packard"),
			new MarketStock(18.37, "GPS", "Gap"),
			new MarketStock(121.89, "CVX", "Chevron"),
			new MarketStock(272.39, "MA", "Mastercard"),
			new MarketStock(129.50, "JNJ", "Johnson & Johnson"),
			new MarketStock(43.25, "TWTR", "Twitter Inc.")
	};
	
	public static void printMarket() {
		for (MarketStock stock: marketList) {
			System.out.print(String.format("%-20s", stock.companyName)); 
			System.out.print(String.format("%-6s", stock.identifier));
			System.out.print(String.format("%-7.2f", stock.price)); 
			System.out.print("\n");

		}
	}

	// This method introduces chaos into the prices so that they change "daily"
	public static void randomWalk() {
		double currentPrice;
		Date dateObject = new Date(); 
		Random distribution = new Random(dateObject.getTime()); 

		for (int i = 0; i < marketList.length; i++) {
			currentPrice = marketList[i].price; 
			//System.out.println("Current Price: " + currentPrice); 
			currentPrice *= 0.05 * distribution.nextGaussian();
			//System.out.println("Gaussian Price: " + currentPrice); 

			marketList[i].price += currentPrice; 
		}
	}
	
	/**
	 * returns a stock based on the given ticker symbol
	 * @param tickerSymbol sting of ticker symbol for requested stock
	 * @return MarketStock representation of stock, or null if not found
	 */
	public static MarketStock getStock(String tickerSymbol) {
		MarketStock _requestedStock = null;
		
		for(MarketStock stock : marketList) {
			if(stock.identifier.equals(tickerSymbol.toUpperCase())) {
				_requestedStock = stock;
				break;
			}
		}
		
		return _requestedStock;
	}
	
}

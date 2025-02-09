/**
 * @author Walter Holley III
 * CSC143 Weekly Problems 5 & 6
 * Drinks.  Inherits the Drinks Superclass
 */
public class Drinks {
	protected String name;
	private float _price_per_oz = 0.1f;
	protected String drinkSize = "small";
	
	public Drinks(){
		
	}
	
	/**
	 * 
	 * @param drinkName name of the drink
	 * @param size size of the drink
	 */
	public Drinks(String drinkName, String size){
		this.name = drinkName;
		this.drinkSize = size;
	}
	
	public String toString(){
		return String.format("%s, size %s, cost: $%.2f", this.name, this.drinkSize, getPrice());
	}
	
	/**
	 * determines the price of a drink
	 * @return float of drink price
	 * 
	 */
	public float getPrice(){
		float price = 0f;
		
		switch(this.drinkSize.toLowerCase()){
		case "small":  
			price = this._price_per_oz * 6;
			break;
		case "medium": 
			price = this._price_per_oz * 12;
			break;
		case "large":
			price = this._price_per_oz * 16;			
			break;
		default:
			break;
		}
		
		return price;
	}
	
	/**
	 * Sets the price per ounce
	 * @param price the new price per ounce
	 */
	protected void setPricePerOunce(float price){
		this._price_per_oz = price;
	}
	
	/**
	 * Retrieves the price per ounce
	 */
	public float getPricePerOunce(){
		return this._price_per_oz;
	}
	
}

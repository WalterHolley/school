/**
 * @author Walter Holley III
 * CSC143 Weekly Problems 5 & 6
 * Caffeinated Drinks.  Inherits the Drinks Superclass
 */
public class CaffeinatedDrinks extends Drinks {
	
	public CaffeinatedDrinks(){
		setPricePerOunce(getPricePerOunce() * 3);
	}
	
	/**
	 * @param drinkName name of the drink
	 * @param size Size of the drink
	 */
	public CaffeinatedDrinks(String drinkName, String size){
		this.name = drinkName;
		this.drinkSize = size;
		setPricePerOunce(getPricePerOunce() * 3);
	}
	
}

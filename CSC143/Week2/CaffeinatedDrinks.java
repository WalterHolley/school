
public class CaffeinatedDrinks extends Drinks {
	
	public CaffeinatedDrinks(){
		this.price_per_oz = this.price_per_oz * 3;
	}
	
	public CaffeinatedDrinks(String drinkName, String size){
		this.price_per_oz = this.price_per_oz * 3;
		this.name = drinkName;
		this.drinkSize = size;
	}

}

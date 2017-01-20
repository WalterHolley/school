
public class NonCaffeinatedDrinks extends Drinks {
	
	public NonCaffeinatedDrinks(){
		this.price_per_oz = this.price_per_oz * 2;
	}
	
	public NonCaffeinatedDrinks(String drinkName){
		this.price_per_oz = this.price_per_oz * 2;
		this.name = drinkName;
	}
	
}

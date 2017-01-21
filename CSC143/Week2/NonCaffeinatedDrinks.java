
public class NonCaffeinatedDrinks extends Drinks {
	
	public NonCaffeinatedDrinks(){
		setPricePerOunce(getPricePerOunce() * 2);
	}
	
	public NonCaffeinatedDrinks(String drinkName){
		if(!drinkName.toLowerCase().equals("water")){
			setPricePerOunce(getPricePerOunce() * 2); 
		}
		this.name = drinkName;
	}
	
}

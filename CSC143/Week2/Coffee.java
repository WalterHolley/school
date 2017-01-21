
public class Coffee extends CaffeinatedDrinks {
	private float priceSurcharge = 0f;
	public String coffeeType = "drip";
	
	public Coffee(){
		this.name = "Coffee";
	}
	
	public Coffee(String coffeeType){
		this.name = "Coffee";
		this.coffeeType = coffeeType;
		
		
	}
	
	public float getPrice(){
		
		if(this.drinkSize.toLowerCase().equals("medium")){
			this.priceSurcharge = 0.5f;
		}
		else if(this.drinkSize.toLowerCase().equals("large")){
			this.priceSurcharge = 1.0f;
		}
		
		return new CaffeinatedDrinks().getPrice() + this.priceSurcharge;
	}
	
	public String toString(){
		return String.format("%s,  type %s, size %s, cost: $%.2f",this.name, this.coffeeType, this.drinkSize, this.getPrice());
	}
}

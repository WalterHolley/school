
public class Coffee extends CaffeinatedDrinks {
	private float priceSurcharge = 0f;
	private String _coffeeType = "drip";
	
	public Coffee(){
		this.name = "Coffee";
	}
	
	public Coffee(String coffeeType){
		this.name = "Coffee";
		this._coffeeType = coffeeType;
		
		if(this.drinkSize.toLowerCase().equals("medium")){
			this.priceSurcharge = 0.5f;
		}
		else if(this.drinkSize.toLowerCase().equals("large")){
			this.priceSurcharge = 1.0f;
		}
	}
	
	public float getPrice(){
		return super.getPrice() + this.priceSurcharge;
	}
}

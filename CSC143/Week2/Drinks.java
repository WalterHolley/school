
public class Drinks {
	protected String name;
	protected float price_per_oz = 0.1f;
	protected String drinkSize = "small";
	private int _smallOunces = 6;
	
	public Drinks(){
		
	}
	
	public Drinks(String drinkName, String size){
		this.name = drinkName;
		this.drinkSize = size;
	}
	
	public String toString(){
		return String.format("%s, size %s, cost: %f\n", this.name, this.drinkSize, getPrice());
	}
	
	public float getPrice(){
		float price = 0f;
		
		switch(this.drinkSize.toLowerCase()){
		case "small":  
			price = this.price_per_oz * 6;
			break;
		case "medium": 
			price = this.price_per_oz * 12;
			break;
		case "large":
			price = this.price_per_oz * 16;			
			break;
		default:
			break;
		}
		
		return price_per_oz;
	}
	
}

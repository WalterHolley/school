
public class Tea extends CaffeinatedDrinks {
	public String teaType = "English Breakfast";
	
	public Tea(){
		this.name = "Tea";
	}
	
	public Tea(String type){
		this.name = "Tea";
		this.teaType = type;
		this.drinkSize = size;
	}
	
	public String toString(){
		return String.format("%s, flavor %s, size %s, cost: %f", this.name, this.teaType, this.drinkSize, getPrice());
	}
}

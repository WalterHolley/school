
public class Tea extends CaffeinatedDrinks {
	public String teaFlavor = "English Breakfast";
	
	public Tea(){
		this.name = "Tea";
	}
	/**
	 * 
	 * @param flavor the name of the tea's flavor
	 */
	public Tea(String flavor){
		this.name = "Tea";
		this.teaFlavor = flavor;
	}
	
	public String toString(){
		return String.format("%s, flavor %s, size %s, cost: $%.2f", this.name, this.teaFlavor, this.drinkSize, getPrice());
	}
}

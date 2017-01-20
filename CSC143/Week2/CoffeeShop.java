import java.util.Scanner;
public class CoffeeShop {

	/**
	 * Entry point for application
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner console = new Scanner(System.in);

	}
		
		private static Drinks[][] getDrinkList(){
			Drinks[] coffeeDrinks = new CaffeinatedDrinks[]{new Coffee(), new Coffee("Mocha"), new Coffee("Latte"), new Coffee("Espresso")};
			Drinks[] teaDrinks = new CaffeinatedDrinks[]{new Tea(), new Tea("Earl Grey"), new Tea("Oolong")};
			Drinks[] noCaffDrinks = {new NonCaffeinatedDrinks("Water"), new NonCaffeinatedDrinks("Juice")}; 
			
			return new Drinks[][]{coffeeDrinks, teaDrinks, noCaffDrinks};
		}

}

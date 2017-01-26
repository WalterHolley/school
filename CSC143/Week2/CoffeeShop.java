import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

public class CoffeeShop {
	public static final String ERROR_MSG_INVALID_INPUT = "Invalid input.  Please try again";
	private static Scanner _console;
	private static ArrayList<Drinks> drinkOrder;
	/**
	 * Entry point for application
	 * @param args
	 */
	public static void main(String[] args) {
		boolean runApplication = true;
		drinkOrder = new ArrayList<Drinks>();
		_console = new Scanner(System.in);
		
		while(runApplication){
			showMainMenu();
			int selection = getMenuSelection(1, 3);
			//Start new order, or add to existing order.
			if(selection == 1){
				Drinks drink = getDrinkForOrder();
				if(drink != null){
					System.out.println("Drink added to order.");
					drinkOrder.add(drink);
				}				
			}
			//view drink order
			else if(selection == 2){
				showDrinkOrder(drinkOrder);
			}
			//End transaction and exit program
			else if(selection == 3){
				
				float total = 0f;
				showDrinkOrder(drinkOrder);
				
				if(drinkOrder.size() > 0){
					for(int i = 0; i < drinkOrder.size(); i++){
						total += drinkOrder.get(i).getPrice();
					}
					System.out.printf("Order Total: $%.2f\n", total);
				}
				System.out.println("Thanks for stopping by!");
				runApplication = false;			
			}
		}
		
		//cleanup
		_console.close();
	}
	
	/**
	 * Prints the primary application menu
	 * to the console
	 */
	private static void showMainMenu(){
		System.out.println("++Main MENU++");
		if(drinkOrder.size() > 0){
			System.out.println("1. Add Drink");
		}
		else{
			System.out.println("1. New Order");
		}
		
		System.out.println("2. View Current Order");
		if(drinkOrder.size() > 0){
			System.out.println("3. Exit Program");
		}
		else{
			System.out.println("3. Complete Order");
		}
		
	}
	
	/**
	 * Print the Main Drinks menu to the console
	 */
	private static void showDrinksMenu(){
		System.out.println("++DRINKS MENU++");
		System.out.println("1. Caffeinated Drinks");
		System.out.println("2. Non-Caffeinated Drinks");
		System.out.println("3. Back to Main Menu");
	}
	
	/**
	 * Prints the Caffeinated Drinks selection to the console
	 */
	private static void showCaffDrinksMenu(){
		System.out.println("++Caffeinated Drinks++");
		System.out.println("1. Coffee");
		System.out.println("2. Tea");
		System.out.println("3. Back");
	}
	
	/**
	 * Prints a list of drinks to the console
	 * @param drinkList the list of Drinks objects
	 */
	private static void showDrinkOrder(ArrayList<Drinks> drinkList){
		if(drinkList.size() > 0){
			for(int i = 0; i < drinkList.size(); i++){
				System.out.println(drinkList.get(i).toString());
			}
		}
		else{
			System.out.println("You have no drinks in your order.");
		}
	}
	
	/**
	 * Selects a non-caffeinated drink for the user
	 * @return NonCaffeinatedDrinks object of the user's choice,
	 * null if the user didn't make a choice
	 */
	private static NonCaffeinatedDrinks getNoCaffDrinkSelection(){
		String[] drinkList = {"Water", "Juice", "Back"};
		NonCaffeinatedDrinks drink  = null;
		System.out.println("++Non-Caffeinated Drinks++");
		printListMenu(drinkList);
		
		int selection = getMenuSelection(1, 3);
		if(selection < 3){
			drink = new NonCaffeinatedDrinks(drinkList[selection - 1]);
		}		
		return drink;
	}
	
	/**
	 * Selects a Coffee drink for the user
	 * @return Coffee Object of the user's choice,
	 * null if the user didn't make a choice.
	 */
	private static Coffee getCoffeeSelection(){
		String[] coffeeList = {"Drip", "Mocha", "Espresso", "Macchiato", "Back"};
		Coffee coffee = null;
		System.out.println("Select Coffee:");
		printListMenu(coffeeList);
		int selection = getMenuSelection(1, 5);
		
		if(selection < 5){
			coffee = new Coffee(coffeeList[selection - 1]);
		}	
		return coffee;
	}
	
	/**
	 * Performs the overall selection process for drinks
	 * @return Drinks object of the user's choice.
	 * null if the user doesn't make a choice.
	 */
	private static Drinks getDrinkForOrder(){
		Drinks drink = null;
		boolean gettingDrink = true;
		while(gettingDrink){
			showDrinksMenu();
			int selection = getMenuSelection(1, 3);	
			//decision process for caffeinated drinks
			if(selection == 1){
				boolean selectingCaffDrink = true;
				while(selectingCaffDrink){
					showCaffDrinksMenu();
					selection  = getMenuSelection(1, 3);
				
					if(selection == 1){
						drink = getCoffeeSelection();
						if(drink == null){
							continue;
						}
						else{
							drink = getDrinkSize(drink);
							if(drink.drinkSize == null){
								continue;
							}
							else{
								gettingDrink = false;
								selectingCaffDrink = false;
							}
						}
					}
					else if(selection == 2){
						drink = getTeaSelection();
						if(drink == null){
							continue;
						}
						else{
							drink = getDrinkSize(drink);
							if(drink.drinkSize == null){
								continue;
							}
							else{
								gettingDrink = false;
								selectingCaffDrink = false;
							}
						}
					}
					else if(selection == 3){
						selectingCaffDrink = false;
					}
				}
			}
			//decision process for non-caffeinated drinks
			else if(selection == 2){
				boolean selectingNoCaffDrink = true;
				while(selectingNoCaffDrink){
					drink = getNoCaffDrinkSelection();
					if(drink == null){
						selectingNoCaffDrink = false;
					}
					else{
						drink = getDrinkSize(drink);
						if(drink.drinkSize == null){
							continue;
						}
						else{
							gettingDrink = false;
							selectingNoCaffDrink = false;
						}
					}
				}
			}
			else if(selection == 3){
				gettingDrink = false;
			}
		}
		return drink;
	}
	
	/**
	 * Allows the user to select a Tea Drink
	 * @return Tea object of the user's choice,
	 * null if the user doesn't make a selection.
	 */
	private static Tea getTeaSelection(){
		String[] teaList = {"English Breakfast", "Earl Grey", "Oolong", "Back"};
		Tea tea = null;
		System.out.println("Select tea:");
		printListMenu(teaList);
		int selection = getMenuSelection(1, 4);
		
		if(selection < 4){
			tea = new Tea(teaList[selection - 1]);
		}		
		return tea;
	}
	
	/**
	 * Selects a drink size
	 * @param drink the drink object that needs it's size selected
	 * @return Drinks object with determined size
	 */
	private static Drinks getDrinkSize(Drinks drink){
		String[] drinkSizes = {"Small", "Medium", "Large", "Back"};
		System.out.println("Select Drink Size:");
		printListMenu(drinkSizes);
		int selection = getMenuSelection(1, 4);
		
		if(selection < 4){
			drink.drinkSize = drinkSizes[selection - 1].toLowerCase();
		}
		else{
			drink.drinkSize = null;
		}	
		return drink;
	}
	
	/**
	 * prints out a list of strings to be used in a menu
	 * @param menu the list of strings to print.
	 */
	private static void printListMenu(String[] menu){
		for(int i = 0; i < menu.length; i++){
			System.out.printf("%s. %s\n", i + 1, menu[i]);
		}
	}
	
	/**
	 * Gets the user's selection from a console menu
	 * @param lowOption the lowest menu choice
	 * @param highOption the highest menu choice
	 * @return int indicating chosen menu selection.
	 */
	private static int getMenuSelection(int lowOption, int highOption){
		boolean isValidSelection = false;
		int menuSelection = 0;
		
		while(!isValidSelection){
			System.out.println("Enter Selection:");
			try{	
				menuSelection = _console.nextInt();
			}
			catch(InputMismatchException e){
				System.out.println(ERROR_MSG_INVALID_INPUT);
				_console.nextLine();
				continue;
			}
			
			if(menuSelection >= lowOption && menuSelection <= highOption){
				isValidSelection = true;			
			}
			else{
				System.out.println(ERROR_MSG_INVALID_INPUT);
			}
			_console.nextLine();
		}
		
		return menuSelection;
	}

}

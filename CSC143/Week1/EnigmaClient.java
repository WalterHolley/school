import java.util.InputMismatchException;
import java.util.Scanner;

public class EnigmaClient {
public static final String ERROR_MSG_INVALID_INPUT = "Invalid input.  Please try again";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner console = new Scanner(System.in);
		int menuSelection = 0;
		String secretMessage = "THIS IS A FAIRLY LENGTHY MESSAGE IT MAY BE DIFFICULT TO DECODE";
		System.out.println("Welcome to Enigma Console");
		
		while(menuSelection != 4){
			showMainMenu();
			
			boolean isValidSelection = false;
				
				while(!isValidSelection){
					System.out.println("Enter Selection:");
					try{	
						menuSelection = console.nextInt();
					}
					catch(InputMismatchException e){
						System.out.println(ERROR_MSG_INVALID_INPUT);
						continue;
					}
					
					if(menuSelection > 0 && menuSelection < 5){
						isValidSelection = true;
					}
					else{
						System.out.println(ERROR_MSG_INVALID_INPUT);
					}
				}
			
				if(menuSelection != 4){
					processMainMenuSelection(menuSelection, console);
				}
					
		}

		
		

	}
	
	private static void showMainMenu(){
		System.out.println("**MAIN MENU**");
		System.out.println("1. Use default rotor settings");
		System.out.println("2. Input custom rotor settings");
		System.out.println("3. View rotor settings");
		System.out.println("4. Exit Program");

	}
	
	private static void showEnigmaMenu(){
		System.out.println("**ENIGMA MENU**");
		System.out.println("1. Encrypt");
		System.out.println("2. Decrypt");
		System.out.println("3. Run Default Example");
		System.out.println("4. Return to Main Menu");
		System.out.println("Enter Selection:");
	}
	
	private static void processMainMenuSelection(int selection, Scanner console){
		Enigma enigma = null;
		
		if(selection == 1){
			enigma = new Enigma();
		}
		else if(selection == 2){
			//process custom rotors
			boolean tryAgain = true;
			while(tryAgain){
				String innerRotor = "";
				String middleRotor = "";
				System.out.println("Type 'q' to return to the main menu");
				System.out.println("Enter inner rotor:");			
				innerRotor = console.nextLine();
				if(innerRotor.toLowerCase().equals("q")){
					break;
				}
				
				System.out.println("Enter middle rotor:");
				middleRotor = console.nextLine();
				if(innerRotor.toLowerCase().equals("q")){
					break;
				}
				
				enigma = new Enigma(innerRotor, middleRotor);
				if(innerRotor.equals(enigma.innerRotor) && middleRotor.equals(enigma.middleRotor)){
					System.out.println("Settings Accepted");
					tryAgain = false;
				}
				else{
					System.out.println(ERROR_MSG_INVALID_INPUT);
				}
				
				
			}
		}
		else if(selection == 3){
			System.out.printf("Inner Rotor: %s\nMiddle Rotor: %s\nOuter Rotor: %s\n", 
					enigma.innerRotor, enigma.middleRotor, enigma.outerRotor);
		}
	}

}

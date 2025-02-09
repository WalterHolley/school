import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * @author Walter Holley III
 * CSC 143 - Assignment #1 Enigma
 * 
 * @version 1.0
 */
public class EnigmaClient {
public static final String ERROR_MSG_INVALID_INPUT = "Invalid input.  Please try again";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner console = new Scanner(System.in);
		int menuSelection = 0;
		System.out.println("Welcome to the Enigma Console");		
		while(menuSelection != 4){
			showMainMenu();
			menuSelection = getMenuSelection(1, 4, console);
			if(menuSelection != 4){
				processMainMenuSelection(menuSelection, console);
			}
		}
		System.out.println("Thanks for using the Enigma Console.  Exiting program.");
		
		//cleanup
		console.close();
	}
	
	/**
	 * Prints the main menu of the application
	 */
	private static void showMainMenu(){
		System.out.println("\n**MAIN MENU**");
		System.out.println("1. Use default rotor settings");
		System.out.println("2. Input custom rotor settings");
		System.out.println("3. View rotor settings");
		System.out.println("4. Exit Program");

	}
	
	/**
	 * Prints the enigma options menu
	 */
	private static void showEnigmaMenu(){
		System.out.println("\n**ENIGMA MENU**");
		System.out.println("1. Encrypt");
		System.out.println("2. Decrypt");
		System.out.println("3. Run Default Example");
		System.out.println("4. Return to Main Menu");
	}
	
	/**
	 * Prints the rotor settings of the given Enigma object
	 * @param enigma the Enigma object
	 */
	private static void showRotorSettings(Enigma enigma){
		System.out.println("The Enigma console will use the following settings:");
		System.out.printf("\tOuter Rotor: %s\n\tMiddle Rotor: %s\n\tInner Rotor: %s\n\n",
				enigma.outerRotor, enigma.middleRotor, enigma.innerRotor);
	}
	
	/**
	 * Handles the menu option selected in the main menu
	 * @param selection the user's selection from the menu
	 * @param console object representing the command line
	 */
	private static void processMainMenuSelection(int selection, Scanner console){
		Enigma enigma = null;
		
		if(selection == 1){
			enigma = new Enigma();
		}
		//prepare custom enigma
		else if(selection == 2){
			boolean tryAgain = true;
			while(tryAgain){
				String innerRotor = "";
				String middleRotor = "";
				System.out.println("\nEach rotor must meet the following criteria:");
				System.out.println("1. Each rotor must be exactly 27 characters long.");
				System.out.println("2. Each rotor must begin with a '#' symbol.");
				System.out.println("3. Numbers, special characters, and spaces are not allowed");
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
		//show settings
		else if(selection == 3){
			showRotorSettings(enigma);
		}
		
		processEnigma(enigma, console);
	}
	
	/**
	 * Gets the user's selection from a menu
	 * @param console object representing the command line
	 * @return int indicating chosen menu selection.
	 */
	private static int getMenuSelection(int lowOption, int highOption, Scanner console){
		boolean isValidSelection = false;
		int menuSelection = 0;
		
		while(!isValidSelection){
			System.out.println("Enter Selection:");
			try{	
				menuSelection = console.nextInt();
			}
			catch(InputMismatchException e){
				System.out.println(ERROR_MSG_INVALID_INPUT);
				console.nextLine();
				continue;
			}
			
			if(menuSelection >= lowOption && menuSelection <= highOption){
				isValidSelection = true;
				
			}
			else{
				System.out.println(ERROR_MSG_INVALID_INPUT);
			}
			console.nextLine();
		}
		
		return menuSelection;
	}
	
	/**
	 * Takes the user's enigma object and performs
	 * 
	 * @param enigma Enigma object
	 * @param console object that represents the command line
	 */
	private static void processEnigma(Enigma enigma, Scanner console){
		int selection = 0;
		while(selection != 4){
			showEnigmaMenu();
			selection = getMenuSelection(1, 4, console);
			//Encrypt string
			if(selection == 1){
				System.out.println("Enter string to encrypt.  spaces and alpha characters only.");
				String messageToEncrypt = console.nextLine().toUpperCase();
				if(!validateEncryptDecryptInput(messageToEncrypt)){
					System.out.println(ERROR_MSG_INVALID_INPUT);
				}
				else{
					enigma.reset();
					System.out.printf("Encrypted message:[%s]\n", enigma.encrypt(messageToEncrypt));
					
				}
			}
			//Decrypt string
			else if(selection == 2){
				System.out.println("Enter string to decrypt.  Ensure your rotor settings are correct.");
				String messageToDecrypt = console.nextLine().toUpperCase();
				if(!validateEncryptDecryptInput(messageToDecrypt)){
					System.out.println(ERROR_MSG_INVALID_INPUT);
				}
				else{
					enigma.reset();
					System.out.printf("Decrypted message: [%s]\n", enigma.decrypt(messageToDecrypt));
					
				}
			}
			//Run Demonstration program
			else if(selection == 3){
				runDemo();
			}					
		}		
	}
	
	/**
	 * Determines if the given message is a a valid encryption message.
	 * Messages contain only spaces and alpha characters.
	 * @param message the message to validate
	 * @return true if message is valid; otherwise false.
	 */
	private static boolean validateEncryptDecryptInput(String message){
		boolean isValid = true;
		
		for(int i = 0; i < message.length(); i++){
			int x = (int)message.toUpperCase().toCharArray()[i];
			
			if(x != 32){
				if(x < 65 || x > 90){
					isValid = false;
					break;
				}
			}
		}
		
		return isValid;
	}
	
	/**
	 * executes a demonstration of the enigma console at default settings.
	 * 
	 */
	private static void runDemo(){
		Enigma enigma = new Enigma();
		String demoString = "WE CAN SHOW THE ENIGMA IN ACTION BY ENCRYPTING THIS";
		System.out.println("Enigma Demo");
		System.out.println("The enigma console takes in strings and encrypts them into an unreadable pattern.");
		System.out.println("The console will also take a string encrypted by it, and decrypt it back to its original message.");
		System.out.println("");
		System.out.printf("When encrypting, you can enter a string like this:\n%s\n\n", demoString);
		demoString = enigma.encrypt(demoString);
		System.out.printf("And the application will return something like this:\n%s\n\n", demoString);
		enigma.reset();
		System.out.printf("If you attempt to decrypt the same string, the application will return this:\n%s\n\n", enigma.decrypt(demoString));
		System.out.println("The application encrypts strings based on its rotor settings, which can be set within the application");
		showRotorSettings(enigma);
		System.out.println("NOTE: The above settings are the default settings, and are the settings that were used in this demo.");
		System.out.println("You can change the inner and middle rotor settings in this application.  Details can be found in the settings menu.");
		System.out.println();
		
	}

}

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
			menuSelection = getMenuSelection(1, 4, console);
			if(menuSelection != 4){
				processMainMenuSelection(menuSelection, console);
			}
		}
		System.out.println("Thanks for using the Enigma Console.  Exiting program.");
	}
	
	/*
	 * Prints the main menu of the application
	 */
	private static void showMainMenu(){
		System.out.println("**MAIN MENU**");
		System.out.println("1. Use default rotor settings");
		System.out.println("2. Input custom rotor settings");
		System.out.println("3. View rotor settings");
		System.out.println("4. Exit Program");

	}
	
	/*
	 * Prints the enigma options menu
	 */
	private static void showEnigmaMenu(){
		System.out.println("**ENIGMA MENU**");
		System.out.println("1. Encrypt");
		System.out.println("2. Decrypt");
		System.out.println("3. Run Default Example");
		System.out.println("4. Return to Main Menu");
	}
	
	/*
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
			System.out.printf("Inner Rotor: %s\nMiddle Rotor: %s\nOuter Rotor: %s\n", 
					enigma.innerRotor, enigma.middleRotor, enigma.outerRotor);
		}
		
		processEnigma(enigma, console);
	}
	
	/*
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
	
	/*
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
				String messageToEncrypt = console.nextLine();
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
				String messageToDecrypt = console.nextLine();
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
		console.nextLine();
	}
	
	/*
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
	
	private static void runDemo(){
		
	}

}

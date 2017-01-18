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
	}
	
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
	
	private static int getEnigmaMenuSelection(Scanner console){
		showEnigmaMenu();
		boolean isValidSelection = false;
		int menuSelection = 0;
		
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
		
		return menuSelection;
	}
	
	private static void processEnigma(Enigma enigma, Scanner console){
		int selection = 0;
		
		while(selection != 4){
			showEnigmaMenu();
			selection = getEnigmaMenuSelection(console);
			if(selection == 1){
				System.out.println("Enter string to encrypt.  spaces and alpha characters only.");
				String messageToEncrypt = console.nextLine();
				if(!validateEncryptInput(messageToEncrypt)){
					System.out.println(ERROR_MSG_INVALID_INPUT);
				}
				else{
					System.out.printf("Encrypted message: %s\n", enigma.encrypt(messageToEncrypt));
					enigma.reset();
				}
			}
			else if(selection == 2){
				System.out.println("Enter string to decrypt.  Ensure your rotor settings are correct.");
				String messageToDecrypt = console.nextLine();
				if(!validateEncryptInput(messageToDecrypt)){
					System.out.println(ERROR_MSG_INVALID_INPUT);
				}
				else{
					System.out.printf("Dencrypted message: %s\n", enigma.decrypt(messageToDecrypt));
					enigma.reset();
				}
			}
			else if(selection == 3){
				runDemo();
			}
		}
	}
	
	private static boolean validateEncryptInput(String message){
		boolean isValid = true;
		
		for(int i = 0; i < message.length(); i++){
			int x = (int)message.toUpperCase().toCharArray()[i];
			
			if(x != 32){
				if(x < 65 && x > 90){
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

import java.util.Scanner;

public class EnigmaClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner console = new Scanner(System.in);
		String secretMessage = "THIS IS THE BEST TEST";
		System.out.println("Welcome to Enigma Console");
		System.out.printf("Encoding Message: %s\n", secretMessage);
		Enigma enigma = new Enigma();
		String encodedMessage = enigma.encrypt(secretMessage);
		System.out.printf("Encoded Message: %s\n", encodedMessage);
		String decodedMessage = enigma.decrypt(encodedMessage);
		System.out.printf("Decoded Message: %s",decodedMessage);
		
		

	}

}

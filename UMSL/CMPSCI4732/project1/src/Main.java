import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Walter Holley III
 * CMP SCI 4732 - Introduction to Cryptography
 * Project 1
 *
 * OBJECTIVE
 * Write an application that can encrypt plain text using a vigenere cipher(provided key and plaintext message)
 * Decrypt a message using a vigenere cipher(provided key and encrypted message)
 * Brute Force a vigenere cipher(provided encrypted message and partial plaintext message)
 */

public class Main {
    private static final String TRY_AGAIN_MESSAGE = "Would you like to try again? y/n: ";
    private static final String INVALID_SELECTION_MESSAGE = "Invalid selection";
    private static Scanner consoleReader;
    private static Vigenere vigenere;

    public static void main(String[] args){
        consoleReader = new Scanner(System.in);
        boolean runProgram = true;

        while(runProgram){

            System.out.println("*** PROJECT 1 ***");
            System.out.println("Please enter the number of your selection:");
            System.out.println("********************");
            System.out.println("1 >>>> Vigenere Encryption");
            System.out.println("2 >>>> Vigenere Decryption");
            System.out.println("3 >>>> Brute-Force Vigenere Encryption");
            System.out.println("4 >>>> Exit Program");

            try {
                int selection = consoleReader.nextInt();

                switch(selection) {
                    case 1:
                        //encryption
                        doEncrypt();
                        break;
                    case 2:
                        //decryption
                        doDecrypt();
                        break;
                    case 3:
                        //brute force
                        doBruteForce();
                        break;
                    case 4:
                        //end program
                        runProgram = false;
                        break;
                    default:
                        System.out.println(INVALID_SELECTION_MESSAGE);

                }

            }
            catch (InputMismatchException e) {
                System.out.println(INVALID_SELECTION_MESSAGE);
                consoleReader.nextLine();
            }

        }
        consoleReader.close();
    }


    /**
     * Handles the UI Orchestration for encrypting a message.
     * Asks for plaintext and key input, then prints a
     * ciphertext message to the screen
     */
    private static void doEncrypt(){
        String plainText = askForText("Please provide a plaintext message.  Letters only.  No numbers, no special characters:");
        String messageKey;

        //if plaintext message was obtained, get the message key next
        if(plainText != null && !plainText.isEmpty()){
            messageKey = askForText("Provide a key for your message.  Letters only. No numbers, no special characters");

            //if message key has been obtained, print encrypted value
            if(messageKey != null && !messageKey.isEmpty()){

            }
        }




    }

    /**
     * Handles UI orchestration for decrypting a message.
     * Asks for ciphertext and key input, then
     * prints a plaintext message to the screen
     */
    private static void doDecrypt(){
        String cipherText = askForText("Please provide the encrypted message text.  Letters only.  No numbers, no special characters:");
        String messageKey;

        //if ciphertext message was obtained, get the message key next
        if(cipherText != null && !cipherText.isEmpty()){
            messageKey = askForText("Provide a key for your message.  Letters only. No numbers, no special characters");

            //if message key has been obtained, print encrypted value
            if(messageKey != null && !messageKey.isEmpty()){

            }
        }
    }

    /**
     * Handles UI orchestration for brute forcing a message
     * Asks for ciphertext and a portion of the unencrypted
     * plaintext message, then attempts to brute force
     * the message
     */
    private static void doBruteForce(){

    }

    /**
     * Asks for text that's compatible with vigenere cipher use cases.
     * Checks for non-alpha characters, and asks the user for input
     * until they provide a correct input, or no longer wish to try.
     * @return String of vigenere compliant text, or null if the
     * user provides no such text.
     */
    private static String askForText(String questionText){
        String input;
        boolean tryAgain;

        consoleReader.nextLine();

        do {
            //get input from user
            System.out.println(questionText);
            input = consoleReader.nextLine();

            if(!Vigenere.isVigenereText(input)){
                tryAgain = yesOrNoResponse(TRY_AGAIN_MESSAGE);
            }
            else
                tryAgain = false;




        }while(tryAgain);

        return input;
    }

    /**
     * Presents a question to the user that requires a
     * yes/no answer
     * @param question Text of the question to ask
     * @return true if answered yes, otherwise false.
     */
    private static boolean yesOrNoResponse(String question){
        boolean isYes = false;
        boolean isValidInput = false;
        String input;

        while(!isValidInput){
            System.out.println(question);
            input = consoleReader.nextLine().toLowerCase().trim();

            if(input.equals("y") || input.equals("n")){
                isValidInput = true;
                if(input.equals("y")){
                    isYes = true;
                }
            }
            else{
                System.out.println(INVALID_SELECTION_MESSAGE);
            }
        }
        return isYes;
    }
}

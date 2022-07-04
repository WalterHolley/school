package com.umsl.cmpsci4732;

import java.util.HashMap;
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
    private static final String ENCRYPTED_MESSAGE_QUESTION = "Please provide the encrypted message text.  Letters only.  No numbers, no special characters:";
    private static final String KEY_MESSAGE_QUESTION = "Provide a key for your message.  Letters only. No numbers, no special characters";
    private static final String CIPHERTEXT_ERROR = "There was a problem with your ciphertext input.  Please make sure the text is valid";
    private static final String MESSAGE_KEY_ERROR = "There was a problem with your message key input.  Please make sure the text is valid";
    private static final String PLAINTEXT_ERROR = "There was a problem with your plain text input.  Please make sure the text is valid";
    private static Scanner consoleReader;

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
                consoleReader.nextLine();

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
        String plainText;
        String messageKey;
        //TODO fix delay in receiving input
        plainText = askForText("Please provide a plaintext message.  Letters only.  No numbers, no special characters:");

        //if plaintext message was obtained, get the message key next
        if(plainText != null && !plainText.isEmpty()){
            messageKey = askForText(KEY_MESSAGE_QUESTION);

            //if message key has been obtained, print encrypted value
            if(messageKey != null && !messageKey.isEmpty()){
                System.out.println("Encrypted Message: " + Vigenere.encrypt(plainText, messageKey));
                System.out.println();
            }
            else
                System.out.println(MESSAGE_KEY_ERROR);
        }
        else
            System.out.println(PLAINTEXT_ERROR);




    }

    /**
     * Handles UI orchestration for decrypting a message.
     * Asks for ciphertext and key input, then
     * prints a plaintext message to the screen
     */
    private static void doDecrypt(){
        String cipherText;
        String messageKey;

        cipherText = askForText(ENCRYPTED_MESSAGE_QUESTION);

        //if ciphertext message was obtained, get the message key next
        if(cipherText != null && !cipherText.isEmpty() && Vigenere.isVigenereText(cipherText)){
            messageKey = askForText(KEY_MESSAGE_QUESTION);

            //if message key has been obtained, print encrypted value
            if(messageKey != null && !messageKey.isEmpty()){
                System.out.println("Decrypted Message: " + Vigenere.decrypt(cipherText, messageKey));
            }
            else
                System.out.println(MESSAGE_KEY_ERROR);

        }
        else
            System.out.println(CIPHERTEXT_ERROR);

    }

    /**
     * Handles UI orchestration for brute forcing a message
     * Asks for ciphertext and a portion of the unencrypted
     * plaintext message, then attempts to brute force
     * the message
     */
    private static void doBruteForce(){
        String cipherText;
        String partialPlainText;
        int keySize;

        cipherText = askForText(ENCRYPTED_MESSAGE_QUESTION);

        if(Vigenere.isVigenereText(cipherText) && cipherText != null && !cipherText.isEmpty()){
            partialPlainText = askForText("Provide a portion of the unencrypted message:");

            if(Vigenere.isVigenereText(partialPlainText) && partialPlainText != null && !partialPlainText.isEmpty()){
                keySize = askForNumber();

                //check key size
                if(keySize > 0){

                    //perform brute force analysis
                    for(int i = 1; i <= keySize; i++){
                        HashMap<String, String> results = Vigenere.bruteForceDecrypt(cipherText,partialPlainText, i);

                        //show results
                        if(results.size() > 0){
                            System.out.println("Results for keysize: " + i);
                            for (String k: results.keySet()) {
                                System.out.printf("Key: %s ==> %s \n",k, results.get(k));
                            }

                        }
                        else{
                            System.out.println("No results found for Key Size " + i);

                        }

                        //ask to continue analysis
                        if(!yesOrNoResponse("Would you like to continue analysis? "))
                            break;
                    }

                }
                else
                    System.out.println("Keysize is invalid");
            }
            else
                System.out.println(PLAINTEXT_ERROR);
        }
        else
            System.out.println(CIPHERTEXT_ERROR);
    }


    /**
     * Asks for a whole number from the user
     * @return The number given by the user.  Returns the minimum value integer if
     * nothing was selected
     */
    private static int askForNumber(){
        int input = Integer.MIN_VALUE;
        boolean tryAgain = false;

        consoleReader.nextLine();

        do {
            //get input from user
            System.out.println("Enter the maximum size for the key. Use whole numbers greater than zero:");
            try{
                input = consoleReader.nextInt();

                if(input <= 0)
                    throw new IllegalArgumentException("Number less than or equal to zero");
            }
            catch (Exception ex){
                System.out.println("The input should be a whole number.  No special or alpha characters. No Spaces.  Greater than zero.");
                tryAgain = yesOrNoResponse(TRY_AGAIN_MESSAGE);
            }
            consoleReader.nextLine();


        }while(tryAgain);

        return input;
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




        do {
            //get input from user
            System.out.println(questionText);

            input = consoleReader.nextLine();

            //Strip all whitespaces from entries
            input = input.trim();
            input = input.replaceAll(" ", "");

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

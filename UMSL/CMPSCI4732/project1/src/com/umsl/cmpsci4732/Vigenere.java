package com.umsl.cmpsci4732;

import java.nio.charset.StandardCharsets;

public class Vigenere {
    private static final int MINIMUM_ALLOWED_ASCII_VALUE = (int)'a';
    private static final int MAX_ALLOWED_ASCII_VALUE = (int)'z';
    private static final int MAX_UNIQUE_CHARACTERS = 26;
    /**
     * Tests a given string to determine if it can be
     * generated, or used by a vigenere cipher
     * @param message The string value to test
     * @return false if it cannot be used, otherwise true
     */
    public static boolean isVigenereText(String message){
        boolean result = true;

        message = message.toLowerCase();

        if(message != null && !message.isEmpty()){

            //ensure characters of message do not fall out of the desired ascii range
            for (char c: message.toCharArray()
                 ) {
                if((int)c < MINIMUM_ALLOWED_ASCII_VALUE || (int)c > MAX_ALLOWED_ASCII_VALUE){
                    result = false;
                    break;
                }

            }
        }

        return result;
    }


    /**
     * Encrypts a string with the vigenere cipher
     * @param plainTextMessage The readable message to be encrypted
     * @param key the key to encrypt the data against
     * @return String of the encrypted message
     */
    public static String encrypt(String plainTextMessage, String key){
        String result = "";
        int keyCount = 0;

        //Checks for valid strings, then encrypt
        if(isVigenereText(plainTextMessage) && isVigenereText(key)){

            for(int i = 0; i < plainTextMessage.length(); i++){
                //encrypt character
                result += encryptChar(plainTextMessage.charAt(i), key.charAt(keyCount));

                keyCount++;
                if(keyCount >= key.length())
                    keyCount = 0;
            }
        }
        else
            result = null;

        return result;
    }

    /**
     * Takes a plaintext character, and produces an encrypted value
     * @param plainChar the character to be encrypted
     * @param keyChar The key character to encrypt against
     * @return the encoded character
     */
    private static char encryptChar(char plainChar, char keyChar){
        int keyASCII = (int)keyChar;
        int plainASCII = (int)plainChar;
        int keyOffset = keyASCII - MINIMUM_ALLOWED_ASCII_VALUE;
        int plainOffest = plainASCII - MINIMUM_ALLOWED_ASCII_VALUE;
        int encCharOffest = (keyOffset + plainOffest) % MAX_UNIQUE_CHARACTERS;
        int encCharASCII = MINIMUM_ALLOWED_ASCII_VALUE + encCharOffest;

        return (char)encCharASCII;
    }
}

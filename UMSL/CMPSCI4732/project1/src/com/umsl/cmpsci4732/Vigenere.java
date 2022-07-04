package com.umsl.cmpsci4732;

import java.util.HashMap;

public class Vigenere {
    private static final int MINIMUM_ALLOWED_ASCII_VALUE = 'a';
    private static final int MAX_ALLOWED_ASCII_VALUE = 'z';
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

        if(!message.isEmpty()){

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
        StringBuilder result = new StringBuilder();
        int keyCount = 0;

        //Checks for valid strings, then encrypt
        if(isVigenereText(plainTextMessage) && isVigenereText(key)){

            for(int i = 0; i < plainTextMessage.length(); i++){
                //encrypt character
                result.append(encryptChar(plainTextMessage.charAt(i), key.charAt(keyCount)));

                keyCount++;
                if(keyCount >= key.length())
                    keyCount = 0;
            }
        }
        else
            result = null;

        assert result != null;
        return result.toString();
    }

    /**
     * Decrypts an encoded vigenere cipher message
     * @param cipherText The encoded message
     * @param key the key used to encrypt the message
     * @return String of the plaintext message
     */
    public static String decrypt(String cipherText, String key){
        StringBuilder result = new StringBuilder();
        int keyCount = 0;

        //Checks for valid strings, then encrypt
        if(isVigenereText(cipherText) && isVigenereText(key)){

            for(int i = 0; i < cipherText.length(); i++){
                //encrypt character
                result.append(decryptChar(cipherText.charAt(i), key.charAt(keyCount)));

                keyCount++;
                if(keyCount >= key.length())
                    keyCount = 0;
            }
        }
        else
            result = null;

        return result.toString();
    }

    /**
     * Performs brute force analysis on a given message
     * @param cipherMessage the encrypted message to analyze
     * @param partialPlainMessage some known plain text portion of the message
     * @param keySize the size of the key to use for analysis
     * @return Hashmap of keys and their messages that contain the partial plain text
     */
    public static HashMap<String, String> bruteForceDecrypt(String cipherMessage, String partialPlainMessage, int keySize){
        String testKey = createKey(keySize, false);
        String finalKey = createKey(keySize, true);
        String testMessage;
        HashMap<String, String> possibleResults = new HashMap<>();
        boolean keysExhausted = false;

        while(!keysExhausted){
            testMessage = decrypt(cipherMessage,testKey);

            //sample string found
            if(testMessage.contains(partialPlainMessage))
                possibleResults.put(testKey, testMessage);

            //check for final key
            if(testKey.equals(finalKey))
                keysExhausted = true;
            else
                testKey = iterateKey(testKey);
        }

        return possibleResults;
    }

    /**
     * Creates a basic encryption key
     * @param size The expected size of the key
     * @param makeFinalKey if true, produces the final key of the given length
     * @return a string of lowercase 'a' or 'z' of the given length
     */
    private static String createKey(int size, boolean makeFinalKey){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < size; i++){
            if(makeFinalKey)
                result.append("z");
            else
                result.append("a");
        }
        return result.toString();
    }

    /**
     * Advances the given key by one position.  count is base 26(a-z)
     * @param key the key to iterate
     * @return string of the iterated result
     */
    private static String iterateKey(String key){
        for (int i = key.length() - 1; i >= 0; i--){
            int value = key.charAt(i);

            //check for end of iteration
            if(value == MAX_ALLOWED_ASCII_VALUE){
                value = MINIMUM_ALLOWED_ASCII_VALUE;
               char[] newKey = key.toCharArray();
               newKey[i] = (char)value;
               key = new String(newKey);
            }
            else{
                value++;
                char[] newKey = key.toCharArray();
                newKey[i] = (char)value;
                key = new String(newKey);
                break;
            }


        }

        return key;
    }

    /**
     * Decrypts an encrypted character back to its plaintext form
     * @param cipherChar The encoded character
     * @param keyChar The corresponding key character it was encrypted against
     * @return the plaintext result
     */
    private static char decryptChar(char cipherChar, char keyChar) {
        int keyASCII = keyChar;
        int cipherASCII = cipherChar;
        int keyOffset = keyASCII - MINIMUM_ALLOWED_ASCII_VALUE;
        int cipherOffset = cipherASCII - MINIMUM_ALLOWED_ASCII_VALUE;
        int decCharOffset = (cipherOffset - keyOffset) % MAX_UNIQUE_CHARACTERS;
        int plainCharASCII = (decCharOffset >= 0? MINIMUM_ALLOWED_ASCII_VALUE + decCharOffset: decCharOffset + MAX_ALLOWED_ASCII_VALUE + 1);

        return (char)plainCharASCII;
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

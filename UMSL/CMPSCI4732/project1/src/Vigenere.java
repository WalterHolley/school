public class Vigenere {
    private static final int MINIMUM_ALLOWED_ASCII_VALUE = (int)'a';
    private static final int MAX_ALLOWED_ASCII_VALUE = (int)'z';
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
}

/*CSC 142 - Week 5 Practice Excercise
 * Walter Holley III
 * 
 * Takes in a word or phrase, prints it in reverse
 * order, and determines if the word/phrase is a
 * palindrome.
 * 
 */


public class ReverseWord {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testCases();
	

	}
	
	public static void reverseWord(String word){
		String reversed = "";
		System.out.println("Word entered: " + word);
		
		for(int i = 1; i <= word.length(); i++){
			reversed += word.toCharArray()[word.length() - i];
		}
		
		System.out.println("Reversed Word: " + reversed);
		
		//Check for palindrome
		//clear whitespace and reduce to lower-case
		word = word.toLowerCase().replace(",","").replace("'","").replace(" ","");
		reversed = reversed.toLowerCase().replace(",","").replace("'","").replace(" ","");
		
		if(word.equals(reversed)){
			System.out.println("This is a palindrome");
		}
		else{
			System.out.println("This is not a palindrome");
		}
		
	}
	
	
	//Runs a set of test cases for the program
	private static void testCases(){
		doTestCase("cat", "tac", false);
		System.out.println();
		doTestCase("Dad", "daD", true);
		System.out.println();
		doTestCase("Madam, I'm Adam", "madA m'I ,madaM", true);
		System.out.println();
		doTestCase("Ultralight", "thgilartlU", false);
		System.out.println();
		doTestCase("Cool Runnings", "sgninnuR looC", false);
		System.out.println();
	}
	
	//Executes an individual test case
	private static void doTestCase(String word, String expectedResult, boolean isPalindrome){
		System.out.println("TEST CASE: " + word);
		System.out.println("EXPECTED RESULTS:");
		System.out.println("Reversed word: " + expectedResult);
		System.out.println("Palindrome:" + isPalindrome);
		System.out.println("ACTUAL RESULTS:");
		reverseWord(word);
		
	}

}

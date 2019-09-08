package project1;

import java.util.Arrays;

public class Anagram {
	
	/**
	 * Determines if one phrase is the anagram of another.
	 * This process ignores whitespace and letter case.
	 * @param sourcePhrase The phrase to be checked for an anagram
	 * @param anagramCandidate The anagram to be looked for.
	 * @return returns true if the anagramCandidate is found in the sourcePhrase
	 */
	public static boolean isAnagram(String sourcePhrase, String anagramCandidate) {
		boolean isAnagram = false;
		
		//remove all white space
		sourcePhrase = sourcePhrase.replace(" ", "");
		anagramCandidate = anagramCandidate.replace(" ", "");
			
		if(sourcePhrase.length() == anagramCandidate.length()) {
			char[] leftPhrase = sourcePhrase.toLowerCase().toCharArray();
			char[] rightPhrase = anagramCandidate.toLowerCase().toCharArray();
			Arrays.sort(leftPhrase);
			Arrays.sort(rightPhrase);
				
			if(Arrays.equals(leftPhrase, rightPhrase)){
				isAnagram = true;
			}
		}
		
		return isAnagram;
	}
}

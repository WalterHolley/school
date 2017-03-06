import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

/**
 * 
 * @author Walter Holley III
 * <p>
 * CSC143 - Assignment 3: Sentiment Analysis
 * <p>
 * BookAnalyzer Class - Performs the actual analysis of a provided book file and determines if the book is a comedy, tragedy, or boring.
 *
 */
public class BookAnalyzer {
	private static final String[] DEFAULT_NEGATIVE_WORDS = {"bad", "awful", "hate", "terrible", "miserable",
		"sad", "dislike", "atrocious", "depressed", "cry"};
	private static final String[] DEFAULT_POSITIVE_WORDS = {"good", "nice", "love", "excellent", "great",
		"awesome", "wonderful", "fantastic", "fabulous", "like", "happy"};
	private Set<String> _negWords;
	private Set<String> _posWords;
	private ArrayList<String> _bookWords;
	
	/**
	 * Default constructor
	 * @param posWordFile file object containing positive words
	 * @param negWordFile file object containing negative words
	 * @param bookFile
	 * @throws FileNotFoundException if the bookFile object cannot be found
	 */
	public BookAnalyzer(File posWordFile, File negWordFile, File bookFile)throws FileNotFoundException{
		try{
			_posWords = readFileToHash(posWordFile);
		}
		catch(FileNotFoundException ex){
			System.out.println(posWordFile.getName() + " not found.  Using default positive "
					+ "word list.");
			_posWords = this.readArrayToHash(DEFAULT_POSITIVE_WORDS);
		}
		
		try{
			_negWords = readFileToHash(negWordFile);
		}
		catch(FileNotFoundException ex){
			System.out.println(negWordFile.getName() + " not found.  Using default negative "
					+ "word list.");
			_negWords = this.readArrayToHash(DEFAULT_NEGATIVE_WORDS);
		}
		
		processBookWords(bookFile);
	}
	
	/**
	 * Gets the number of words found in the book
	 * @return Integer of the number of words in the book
	 */
	public int getBookWordCount(){
		return _bookWords.size();
	}
	
	/**
	 * Finds the most commonly recurring positive word in the book
	 * @return String of the most common positive word
	 */
	public String getCommonPositiveWord(){
		return getMostRecurringElement(_posWords);
	}
	
	/**
	 * Finds the most commonly recurring negative word in the book
	 * @return String of the most common negative word
	 */
	public String getCommonNegativeWord(){
		return getMostRecurringElement(_negWords);
	}
	
	/**
	 * Determines the total number of positive word occurrences
	 * based on positive word file provided at construction.
	 * Otherwise a default collection of positive words will be used.
	 * @return Integer of total positive words found.
	 */
	public int getTotalPositiveWordOccurrences(){
		return findWordOccurrences(_posWords, _bookWords);
	}
	
	/**
	 * Determines the total number of negative word occurrences
	 * based on positive word file provided at construction.
	 * Otherwise a default collection of negative words will be used.
	 * @return Integer of total negative words found.
	 */
	public int getTotalNegativeWordOccurrences(){
		return findWordOccurrences(_negWords, _bookWords);
	}
	
	/**
	 * gets the percentage of positive words used in the book
	 * @return float of percentage.  Formatted properly
	 * <p>
	 * example: (regular float: 0.982, returned float: 98.2)
	 */
	public float getPositiveWordPercentage(){
		float percentage = (float)this.getTotalPositiveWordOccurrences() / _bookWords.size() * 100;
		return  percentage;
	}
	
	/**
	 * gets the percentage of negative words used in the book
	 * @return float of percentage.  Formatted properly
	 * <p>
	 * example: (regular float: 0.982, returned float: 98.2)
	 */
	public float getNegativeWordPercentage(){
		float percentage = (float)this.getTotalNegativeWordOccurrences() / _bookWords.size() * 100;
		return  percentage;
	}
	
	/**
	 * Determines the general sentiment of the book.
	 * @return String indicating if the book's sentiment is a that of a comedy, tragedy, or just boring
	 */
	public String getBookSentiment(){
		String sentiment = "Boring";
		
		if(this.getTotalPositiveWordOccurrences() > this.getTotalNegativeWordOccurrences()){
			sentiment = "Comedy";
		}
		else if(this.getTotalPositiveWordOccurrences() < this.getTotalNegativeWordOccurrences()){
			sentiment = "Tragedy";
		}	
		return sentiment;
	}
	
	/**
	 * determines how many times a word occurred in the book
	 * @param word the word to look for
	 * @return Integer of total occurrences
	 */
	public int getWordOccurrence(String word){
		return  findWordOccurrences(word, _bookWords);
	}

	
	/**
	 * Out of a given set, returns the element that occurs the most in the book
	 * @param listOfElements Set of strings to compare
	 * @return String representation of the element that occurs the most.  Otherwise
	 * null if none of the elements were found.
	 */
	private String getMostRecurringElement(Set<String> listOfElements){
		String mostCommonWord = null;
		int occurrences = 0;

		for(String word : listOfElements){
			int timesFound = findWordOccurrences(word, _bookWords);
			if(timesFound > occurrences){
				mostCommonWord = word;
				occurrences = timesFound;
			}
		}	
		return mostCommonWord;
	}
	
	/**
	 * From a set of elements, determines the total number of times
	 * all elements occur in the book
	 * @param wordOccurrences Set of words to look for
	 * @param wordList List of words to search
	 * @return Integer of total occurrences
	 */
	private int findWordOccurrences(Set<String> wordOccurrences, ArrayList<String> wordList){
		int occurrences = 0;
		
		for( String word: wordOccurrences){
			occurrences += findWordOccurrences(word, wordList);
		}
		
		return occurrences;
	}
	
	/**
	 * Searches for the occurrences of a word in a List, and returns the number of occurrences
	 * @param word the word to search for
	 * @param wordList the List object to be searched
	 * @return Integer representing the number of instances found
	 */
	private int findWordOccurrences(String word, List<String> wordList){
		int occurrences = 0;
		if(wordList.size() > 0){
			int index = Collections.binarySearch(wordList, word);
			
			if(index > -1){
				occurrences++;
				if(index < wordList.size() - 1){
					int rightArraySize = wordList.size() - index - 1;
					
					if(rightArraySize > 0){
						occurrences += findWordOccurrences(word, wordList.subList(index + 1, index + rightArraySize + 1));
					}
					
					if(index > 0){
						occurrences += findWordOccurrences(word, wordList.subList(0, index));
					}
					
				}
			}
		}
		return occurrences;
	}
	
	/**
	 * Converts a String array into a HashSet of type String
	 * @param array the String array to convert
	 * @return HashSet<String> of the given array
	 */
	private HashSet<String> readArrayToHash(String[] array){
		return new HashSet<String>(Arrays.asList(array));
	}
	
	/**
	 * Writes all the lines in a file to a
	 * Set
	 * @param file File Object to be read
	 * @return Set<String> of File Object
	 * @throws FileNotFoundException if the given file cannot be found
	 */
	private Set<String> readFileToHash(File file) throws FileNotFoundException{
		HashSet<String> wordHash = new HashSet<String>();
		Scanner fileReader = new Scanner(file);
		
		while(fileReader.hasNextLine()){
			String fileLine = fileReader.nextLine().toLowerCase().trim();
			wordHash.add(fileLine);
		}	
		fileReader.close();
		return wordHash;
	}
	
	/**
	 * Parses the bookFile passed from the constructor of the class
	 * into an ArrayList.
	 * @param bookFile the book's File Object.
	 * @throws FileNotFoundException Will be thrown if the File referenced by bookFile 
	 * cannot be found.
	 */
	private void processBookWords(File bookFile) throws FileNotFoundException{
		Scanner fileReader;
		
		fileReader = new Scanner(bookFile);
		_bookWords = new ArrayList<String>();
			
		while(fileReader.hasNextLine()){
			String[] bookLine = fileReader.nextLine().toLowerCase().split("\\s+");
			for(int i = 0; i < bookLine.length; i++){
				if(bookLine[i].equals("")){
					continue;
				}
				_bookWords.add(bookLine[i]);
			}
		}
		Collections.sort(_bookWords);
		fileReader.close();
	}
}


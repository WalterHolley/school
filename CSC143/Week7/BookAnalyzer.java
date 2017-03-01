import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;


public class BookAnalyzer {
	private static final String[] DEFAULT_NEGATIVE_WORDS ={"bad", "awful", "hate", "terrible", "miserable",
		"sad", "dislike", "atrocious", "depressed", "cry"};
	private static final String[] DEFAULT_POSITIVE_WORDS = {"good", "nice", "love", "excellent", "great",
		"awesome", "wonderful", "fantastic", "fabulous", "like"};
	private Set<String> _negWords;
	private Set<String> _posWords;
	private ArrayList<String> _bookWords;

	
	public BookAnalyzer(File posWordFile, File negWordFile, File bookFile)throws FileNotFoundException{
		try{
			_posWords = readFileToHash(posWordFile);
		}
		catch(FileNotFoundException ex){
			System.out.println(posWordFile.getName() + "not found.  Using defualt positive "
					+ "word list.");
			_posWords = this.readArrayToHash(DEFAULT_POSITIVE_WORDS);
		}
		
		try{
			_negWords = readFileToHash(negWordFile);
		}
		catch(FileNotFoundException ex){
			System.out.println(posWordFile.getName() + "not found.  Using defualt negative "
					+ "word list.");
			_negWords = this.readFileToHash(bookFile);
		}
		
		processBookWords(bookFile);
	}
	
	/**
	 * Writes all the unique words in a file to a
	 * HashSet
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	private Set<String> readFileToHash(File file) throws FileNotFoundException{
		HashSet<String> wordHash = new HashSet<String>();
		Scanner fileReader = new Scanner(file);
		
		while(fileReader.hasNextLine()){
			String[] fileLine = fileReader.nextLine().toLowerCase().split("\\s+");
			for(String word : fileLine){
				if(!wordHash.contains(word)){
					wordHash.add(word);
				}
			}
		}	
		fileReader.close();
		return wordHash;
	}
	
	private void processBookWords(File bookFile) throws FileNotFoundException{
		_bookWords = new ArrayList<String>();
		Scanner fileReader;
		
		fileReader = new Scanner(bookFile);
		_bookWords = new ArrayList<String>();
			
		while(fileReader.hasNextLine()){
			String[] bookLine = fileReader.nextLine().split("\\s+");
			for(int i = 0; i < bookLine.length; i++){
				_bookWords.add(bookLine[i]);
			}
		}
		Collections.sort(_bookWords);
		fileReader.close();
	}
	
	public int getBookWordCount(){
		return _bookWords.size();
	}
	
	private int getWordOccurrenceCount(String word){
		int occurrences = 0;
		word = word.toLowerCase();
		
		if(_bookWords.contains(word)){
			for(String bookWord : _bookWords){
				if(bookWord.toLowerCase().equals(word)){
					occurrences++;
				}
			}
		}
		
		return occurrences;
	}
	
	private void doPositiveWordAnalysis(){
		for(int i = 0; i < _posWords.size(); i++){
			
		}
	}
	public String getCommonPositiveWord(){
		return getMostRecurringElement(_posWords);
	}
	
	public String getCommonNegativeWord(){
		return getMostRecurringElement(_negWords);
	}
	
	public int getTotalPositiveWordOccurrences(){
		return findWordOccurrences(_posWords, (String[])_bookWords.toArray());
	}
	
	public float getPositiveWordPercentage(){
		return this.getTotalPositiveWordOccurrences() / this.getBookWordCount();
	}
	
	public int getWordOccurrence(String word){
		return  findWordOccurrences(word, (String[])_bookWords.toArray());
	}
	
	
	
	private String getMostRecurringElement(Set<String> listOfElements){
		String mostCommonWord = null;
		int occurrences = 0;
		
		for(String word : listOfElements){
			int timesFound = findWordOccurrences(word,(String[])_bookWords.toArray());
			if(timesFound > occurrences){
				mostCommonWord = word;
				occurrences = timesFound;
			}
		}
		
		return mostCommonWord;
	}
	
	
	private int findWordOccurrences(Set<String> wordOccurrences, String[] wordList){
		int occurrences = 0;
		
		for(String word: wordOccurrences){
			occurrences += findWordOccurrences(word, wordList);
		}
		
		return occurrences;
	}
	
	private int findWordOccurrences(String word, String[] wordArray){
		int occurrences = 0;
		if(wordArray.length > 0){
			int index = Arrays.binarySearch(wordArray, word);
			
			if(index > -1){
				occurrences++;
				if(index < wordArray.length - 1){
					int newArraySize = wordArray.length - index - 1;
					occurrences = findWordOccurrences(word, Arrays.copyOfRange(wordArray, index, newArraySize));
				}
			}
		}
		return occurrences;
	}
	
	private HashSet<String> readArrayToHash(String[] array){
		return new HashSet<String>(Arrays.asList(array));
	}
}


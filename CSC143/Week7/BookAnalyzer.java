import java.awt.print.Book;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;


public class BookAnalyzer {
	private Set<String> negWords;
	private Set<String> posWords;
	private ArrayList<String> bookWords;
	private String[] posList = {"good", "nice", "love", "excellent", "great",
			"awesome", "wonderful", "fantastic", "fabulous", "like"};
	private String [] negList = {"bad", "awful", "hate", "terrible", "miserable",
			"sad", "dislike", "atrocious", "depressed", "cry"};

	
	public BookAnalyzer(File posWordFile, File negWordFile, File bookFile){
		try{
			negWords = readFileToHash(negWordFile);
			posWords = readFileToHash(posWordFile);
		}
		catch(FileNotFoundException ex){
			//throw new FileNotFoundException();
		}
	}
	
	private void processWordList(File posWordFile, File negWordFile){
		
	}
	
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
	
	private void processBookWords(File bookFile) throws FileNotFoundException, IOException{
		bookWords = new ArrayList<String>();
		Scanner fileReader;
		
		try{
			fileReader = new Scanner(bookFile);
			bookWords = new ArrayList<String>();
			
			while(fileReader.hasNextLine()){
				String[] bookLine = fileReader.nextLine().split("\\s+");
				for(int i = 0; i < bookLine.length; i++){
					bookWords.add(bookLine[i]);
				}
			}
		}
		catch(FileNotFoundException ex){
			
		}
		catch(IOException ex){
			
		}
		
		
		
	}
	
	public int getBookWordCount(){
		return bookWords.size();
	}
	
	private int getWordOccurrenceCount(String word){
		int occurrences = 0;
		word = word.toLowerCase();
		
		if(bookWords.contains(word)){
			for(String bookWord : bookWords){
				if(bookWord.toLowerCase().equals(word)){
					occurrences++;
				}
			}
		}
		
		return occurrences;
	}
}


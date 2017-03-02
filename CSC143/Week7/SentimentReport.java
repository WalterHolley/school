import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * 
 * @author Walter Holley III
 *
 */
public class SentimentReport {
	private static final boolean TEST_MODE = true;
	private static final String POSITIVE_WORD_FILE = "posWords.txt";
	private static final String NEGATIVE_WORD_FILE = "negWords.txt";
	private static final String FILE_NOT_FOUND_MSG = "The file name you entered cannot be found";
	private static final String INVALID_INPUT_MSG = "Input is invalid";
	
	public static void main(String[] args) {
		boolean runProgram = true;
		ArrayList<File> bookList;
		while(runProgram){
			if(!TEST_MODE){
				bookList = getInput();
			}
			else{
				bookList = new ArrayList<File>();
				bookList.add(new File("OliverTwist.txt"));
				bookList.add(new File("Macbeth.txt"));
				bookList.add(new File("MuchAdoAboutNothing.txt"));
			}
			
			for(File bookFile: bookList){
				try{
					float executionTime = System.nanoTime();
					getReport(bookFile);
					executionTime = System.nanoTime() - executionTime;
					System.out.printf("Execution Time: %s\n\n", executionTime);
				}
				catch(FileNotFoundException ex){
					System.out.printf("The file %s could not be found.\n\n", bookFile.getName());
				}
				
			}
			
			if(TEST_MODE){
				runProgram = false;
			}
			
		}
		
	}
	
	private static void intro(){
		
	}
	
	private static void getReport(File bookFile)throws FileNotFoundException{
		File posFile = new File(POSITIVE_WORD_FILE);
		File negFile = new File(NEGATIVE_WORD_FILE);
		BookAnalyzer book = new BookAnalyzer(posFile, negFile, bookFile);
		
		System.out.println("Analyzing " + bookFile.getName() + "...");
		int totalWords = book.getBookWordCount();
		int totalPosWords = book.getTotalPositiveWordOccurrences();
		float posWordPercentage = book.getPositiveWordPercentage();
		String commonPosWord = book.getCommonPositiveWord();
		//int commonPosWord = book.getWordOccurrence("happy");
		
		System.out.println("Analysis Complete");
		System.out.println("Book Name: " + bookFile.getName());
		System.out.println("Total Book Words: " + totalWords);
		System.out.println("Total Positive Words: " + totalPosWords);
		System.out.println("Positive Word Percentage: " + posWordPercentage);
		System.out.println("Most Common Positive Word: " + commonPosWord);
	}
	
	private static ArrayList<File> getInput(){
		ArrayList<File>bookList = new ArrayList<File>();
		boolean inputBooks = true;
		boolean isValidInput = false;
		Scanner console = new Scanner(System.in);
		
		while(inputBooks){
			System.out.println("Enter book file(ex. \"file.txt\", \"directory\\file.txt\"):");
			String userInput = console.nextLine();
			File bookFile;
			try{
				bookFile = new File(userInput);
				if(!bookFile.exists()){
					System.out.println(FILE_NOT_FOUND_MSG);
				}
				else{
					bookList.add(bookFile);
					System.out.println("File has been added to report");
				}
			}
			catch(NullPointerException ex){
				System.out.println("Something is wrong with the file name you entered.  Please try again");
			}
			
			while(!isValidInput){
				System.out.println("Add another book? y/n");
				String input = console.nextLine().toLowerCase();
				
				if(!input.equals("y") || !input.equals("n")){
					System.out.println(INVALID_INPUT_MSG);
				}
				else if(input.equals("n")){
					isValidInput = true;
					inputBooks = false;
				}
				else{
					isValidInput = true;
				}
			}	
		}
		console.close();
		return bookList;
	}
}

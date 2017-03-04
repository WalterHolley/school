import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * 
 * @author Walter Holley III
 *CSC 143 - Assignment 3
 *<p>
 *Report client for Sentiment Analysis.  Accepts book files
 *from the user and performs a sentiment analysis of the given
 *files.  Presents findings upon completion.
 */
public class SentimentReport {
	private static final boolean TEST_MODE = true;
	private static final String POSITIVE_WORD_FILE = "posWords.txt";
	private static final String NEGATIVE_WORD_FILE = "negWords.txt";
	private static final String TEST_BOOK_ONE = "OliverTwist.txt";
	private static final String TEST_BOOK_TWO = "Macbeth.txt";
	private static final String TEST_BOOK_THREE = "MuchAdoAboutNothing.txt";
	private static final String FILE_NOT_FOUND_MSG = "The file name you entered cannot be found";
	private static final String INVALID_INPUT_MSG = "Input is invalid";
	
	public static void main(String[] args) {
		boolean runProgram = true;
		boolean checkMoreBooks = true;
		ArrayList<File> bookList;
		Scanner console = new Scanner(System.in);
		String checkBooksQuestion = "";
		
		intro();
		while(runProgram){
			if(!TEST_MODE){
				bookList = getInput(console);
			}
			else{
				bookList = new ArrayList<File>();
				bookList.add(new File(TEST_BOOK_ONE));
				bookList.add(new File(TEST_BOOK_TWO));
				bookList.add(new File(TEST_BOOK_THREE));
			}
			
			System.out.println("GENERATING BOOK REPORT(S)\n");
			for(File bookFile: bookList){
				try{
					getReport(bookFile);
				}
				catch(FileNotFoundException ex){
					System.out.printf("The file %s could not be found.\n\n", bookFile.getName());
				}		
			}
			
			if(TEST_MODE){
				checkBooksQuestion = "Would you like to re-run the test? y/n: ";
			}
			else{
				checkBooksQuestion = "Would you like to analyze more books? y/n: ";
			}
			
			checkMoreBooks = yesOrNoQuestion(checkBooksQuestion, console);
			if(checkMoreBooks){
				runProgram = true;
			}
			else{
				runProgram = false;
			}
		}
		System.out.println("Ending Program.  Have a nice day!");
	}
	
	/**
	 * Writes an introduction and instructions for the application.
	 */
	private static void intro(){
		System.out.println("BOOK ANALYZER\n");
		
		if(TEST_MODE){
			System.out.println("TEST MODE ACTIVE\n");
		}
		else{
			System.out.println("INSTRUCTIONS");
			System.out.println("Add books the analyzer by entering the name or path" +
					"\nfor a book you want to analyize.  You can enter as many books as" +
					"\nyou wish.  When done, the application will provide a sentiment summary" +
					"\nof all the books you selected.");
		}
	}
	
	/**
	 * Generates and prints a sentiment report for the provided book file
	 * @param bookFile
	 * @throws FileNotFoundException
	 */
	private static void getReport(File bookFile)throws FileNotFoundException{
		File posFile = new File(POSITIVE_WORD_FILE);
		File negFile = new File(NEGATIVE_WORD_FILE);
		
		System.out.println("Analyzing " + bookFile.getName() + "...");
		long executionTime = System.nanoTime();
		BookAnalyzer book = new BookAnalyzer(posFile, negFile, bookFile);
		int totalWords = book.getBookWordCount();
		int totalPosWords = book.getTotalPositiveWordOccurrences();
		int totalNegWords = book.getTotalNegativeWordOccurrences();
		float posWordPercentage = book.getPositiveWordPercentage();
		float negWordPercentage = book.getNegativeWordPercentage();
		String commonPosWord = book.getCommonPositiveWord();
		String commonNegWord = book.getCommonNegativeWord();
		String bookSentiment = book.getBookSentiment();
		executionTime = (System.nanoTime() - executionTime);
		executionTime = executionTime / 1000000;
		
		System.out.println("Analysis Complete");
		System.out.println("Book File: " + bookFile.getName());
		System.out.println("Total Book Words: " + totalWords);
		System.out.println("Total Positive Words: " + totalPosWords);
		System.out.println("Total Negative Words: " + totalNegWords);
		System.out.printf("Positive Word Percentage: %.2f%s\n", posWordPercentage, "%");
		System.out.printf("Negative Word Percentage: %.2f%s\n", negWordPercentage, "%");
		System.out.println("Most Common Positive Word: " + commonPosWord);
		System.out.println("Most Common Negative Word: " + commonNegWord);
		System.out.println("General Book Sentiment: " + bookSentiment);
		System.out.printf("Execution Time: %s milliseconds\n\n", executionTime);
	}
	
	/**
	 * Ask the user for the location of the book(s) they wish to
	 * analyze.
	 * @return
	 */
	private static ArrayList<File> getInput(Scanner console){
		ArrayList<File>bookList = new ArrayList<File>();
		boolean inputBooks = true;
		
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
			inputBooks = yesOrNoQuestion("Add another book? y/n: ", console);
		}
		return bookList;
	}
	
	/**
	 * Presents a question to a user and looks for a 'y' or 'n'
	 * response.
	 * @param question the question to be asked
	 * @param console Scanner object gathering user input
	 * @return true if the answer is yes, otherwise false
	 */
	private static boolean yesOrNoQuestion(String question, Scanner console){
		boolean isYes = true;
		boolean isValidInput = false;
		
		while(!isValidInput){
			System.out.println(question);
			String input = console.nextLine().toLowerCase().trim();
			
			if(!input.equals("y") && !input.equals("n")){
				System.out.println(INVALID_INPUT_MSG);
			}
			else if(input.equals("n")){
				isValidInput = true;
				isYes = false;
			}
			else{
				isValidInput = true;
			}
		}
		return isYes;
	}
}

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
	private static final String FILE_NOT_FOUND_MSG = "The file name you entered cannot be found";
	private static final String INVALID_INPUT_MSG = "Input is invalid";
	private static ArrayList<File> bookList;
	
	public static void main(String[] args) {
		boolean runProgram = true;
		
		while(runProgram){
			getInput();
		}
		
	}
	
	private static void intro(){
		
	}
	
	private static void getReport(File bookFile){
		
	}
	
	private static void getInput(){
		bookList = new ArrayList<File>();
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
	}
}

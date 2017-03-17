import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * CSC 143 - Assignment 4: Book Catalog
 * @author Walter Holley III
 * Client for Book Catalog Assignment
 */
public class BookCatalogClient {
	private static final String BOOK_CATALOG_FILE = "bookList.txt";
	private static final String INVALID_INPUT_MSG = "The input entered is invalid";
	private static BookCatalog BOOK_CATALOG = null;

	public static void main(String[] args) {
		boolean runProgram = true;
		try{
			processCatalogFile();
		}
		catch(FileNotFoundException ex){
			System.out.println("The Book Catalog file could not be found.");
		}
		
		while(runProgram){
			
		}
		

	}
	
	private static void searchBooksMenu(Scanner console){
		boolean isValidInput = false;
		System.out.println("***SEARCH MENU***");
		System.out.println("1. Search By Author Last Name");
		System.out.println("2. Search By Author First Name");
		System.out.println("3. Search By ISBN#");
	
	}
	
	private static List<Book> searchForBook(Scanner console, int method){
		List<Book> results;
		System.out.println("Enter Search Criteria");
		String input = console.nextLine();
		
		for(int i = 1; i <= BOOK_CATALOG.size(); i++){
			switch(method){
			case 1:
				BOOK_CATALOG.
			}
		}
		
		return results;
	}
	
	private static void addBook(Scanner console){
		boolean isValidInput = false;
		String title = "";
		String firstName = "";
		String lastName = "";
		String isbn = "";
		int bookYear = 0;
		float bookPrice = 0;
		
		while(!isValidInput){
			try{
				title = getAddBookStringInput(console, "Book Title:", false, false, false);
				firstName = getAddBookStringInput(console, "Author First Name:", false, false, false);
				lastName = getAddBookStringInput(console, "Author Last Name:", false, false, false);
				bookYear = Integer.parseInt(getAddBookStringInput(console, "Year Published:", true, false, false));
				bookPrice  = Float.parseFloat(getAddBookStringInput(console, "Book Price:", true, true, false));
				isbn = getAddBookStringInput(console, "ISBN#:", false, false, false);
			}
			catch(Exception ex){
				System.out.println(INVALID_INPUT_MSG);
			}
		}
		
		Book book = new Book(title, isbn, bookPrice);
		book.setAuthorFirstName(firstName);
		book.setAuthorLastName(lastName);
		book.setBookYear(bookYear);
		
		System.out.println(book.toString());
	
		
	}
	
	private static int getMenuSelection(Scanner console, int lowerBound, int upperBound){
		int selection = 0;
		boolean isValidInput = false;
		
		while(!isValidInput){
			System.out.println("Selection:");
			String input = console.nextLine();
			try{
				selection = Integer.parseInt(input);
			}
			catch(NumberFormatException ex){
				System.out.println(INVALID_INPUT_MSG);
				continue;
			}
			
			if(selection < lowerBound || selection > upperBound){
				System.out.printf("Please make a selection from %s to %s\n", lowerBound, upperBound);
			}
			else{
				isValidInput = true;
			}
		}
		
		return selection;
	}
	private static boolean yesOrNoQuestion(Scanner console, String question){
		boolean isYes = false;
		boolean isValidInput = false;
		String input;
		
		while(!isValidInput){
			System.out.println(question);
			input = console.nextLine().toLowerCase().trim();
			
			if(input.equals("y") || input.equals("n")){
				isValidInput = true;
				if(input.equals("n")){
					isYes = false;
				}
			}
			else{
				System.out.println(INVALID_INPUT_MSG);
			}
		}
		return isYes;
	}
	
	private static String getAddBookStringInput(Scanner console, String question, boolean isNumber, boolean isFloat, boolean isISBN){
		boolean isValidInput = false;
		String input = null;
		while(!isValidInput){
			System.out.println(question);
			input = console.nextLine();
			
			if(isNumber){
				if(isFloat){
					if(Float.parseFloat(input) > 0.00f){
						isValidInput = true;
					}
				}
				else{
					if(Integer.parseInt(input) > 2013){
						isValidInput = true;
					}
				}
			}
			else if(isISBN){
				isValidInput = isValidISBN(input);
			}
			else if((int)input.charAt(0) < 48 || (int)input.charAt(0) > 	57){
				isValidInput = true;
			}
			else{
				System.out.println(INVALID_INPUT_MSG);
			}
		}
		
		return input;
		
	}
	
	private static boolean isValidISBN(String isbn){
		boolean isValid = false;
		int dashDigit = (int)'-';
		if(isbn.length() > 9 && isbn.length() < 14){
			//get checksum value
			int checkSum = (int)isbn.toCharArray()[isbn.length() - 1];
			
			//calculate validation checksum
			if(checkSum != dashDigit){
				int dashCount = 0;
				int digitTotal = 0;
				int digitCount = 0;
				for(int i = 0; i < isbn.length(); i++){
					int value = (int)isbn.charAt(i);
					if(value == dashDigit){
						if(i == 0){
							break;
						}
						else if((int)isbn.charAt(i - 1) == dashDigit){
							break;
						}
						dashCount++;
						if(dashCount > 3){
							break;
						}
					}
					else{
						digitCount++;
						if(digitCount < 10){
							digitTotal += value * digitCount;
						}
						else{
							break;
						}	
					}
				}
				
				if(digitCount == 9){
					int determinedChecksum = digitTotal % 11;
					if(determinedChecksum == checkSum){
						isValid = true;
					}
				}
			}
		}	
		return isValid;
	}
	
	
	private static void processCatalogFile()throws FileNotFoundException{
		Scanner fileReader = new Scanner(new File(BOOK_CATALOG_FILE));
		BOOK_CATALOG = new BookCatalog();
		
		while(fileReader.hasNextLine()){
			String[] bookEntry = fileReader.nextLine().split("\\t");
			Book book;
			try{
				book = new Book(bookEntry[3], bookEntry[0], Float.parseFloat(bookEntry[5]));
				book.setBookYear(Integer.parseInt(bookEntry[4]));
			}
			catch(NumberFormatException ex){
				continue;
			}
			
			book.setAuthorFirstName(bookEntry[2]);
			book.setAuthorLastName(bookEntry[1]);
			BOOK_CATALOG.add(book);
			
		}
		
		fileReader.close();
	}

}

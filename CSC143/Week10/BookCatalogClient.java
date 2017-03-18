import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
		boolean runProgram = false;
		Scanner console = new Scanner(System.in);
		int selection = 0;
		
		try{
			processCatalogFile();
			runProgram = true;
		}
		catch(FileNotFoundException ex){
			System.out.println("The Book Catalog file could not be found.");
		}
		
		while(runProgram){
			selection = showMainMenu(console);
			switch(selection){
				case 1:
					showCatalog(console);
					break;
				case 2:
					addBook(console);
					break;
				case 3:
					doBookSearch(console);
					break;
				case 4:
					runProgram = false;
					break;
				case 5:
					runProgram = false;
					break;
			}
		}
		
		console.close();
		System.out.println("Exiting Book Catalog.  Enjoy your day!");
		

	}
	
	private static int showMainMenu(Scanner console){
		int selection = 0;
		
		System.out.println("***MAIN MENU***");
		System.out.println("1. View Catalog");
		System.out.println("2. Add A Book");
		System.out.println("3. Find A Book");
		System.out.println("4. Remove A Book From Catalog");
		System.out.println("5. Exit Program");
		selection = getMenuSelection(console, 1, 5);
		
		return selection;
	}
	
	private static void showCatalog(Scanner console){
		printCatalog();
		System.out.println("Press ENTER to continue");
		console.nextLine();
	}
	
	/**
	 * Prints Book Catalog to the console
	 */
	private static void printCatalog(){
		BookCatalogNode catalog = BOOK_CATALOG.getCatalog();
		int count = 1;
		//table format 
		System.out.printf("%-5s\t%s\t\t%-25s\t%-60s\t%s\t%s\n", "#", "ISBN","AUTHOR", "TITLE", "PUBLISHED", "PRICE");
		
		while(catalog != null){
			Book book = catalog.getBook();
			System.out.printf("%-5s\t%s\t%-25s\t%-60s\t%s\t\t$%.2f\n", count, book.getISBN(), book.getAuthorFullName(), book.getBookName(),
					book.getBookYear(), book.getPrice());
			catalog = catalog.getNext();
			count++;
		}
		
		
	}
	
	/**
	 * Handles the Book Search process.  Responsible for 
	 * search menu and printing search results
	 * @param console Scanner object for the application
	 */
	private static void doBookSearch(Scanner console){
		int selection = 0;
		
		while(selection != 4){
			System.out.println("***SEARCH MENU***");
			System.out.println("1. Search By Author Last Name");
			System.out.println("2. Search By Author First Name");
			System.out.println("3. Search By ISBN#");
			System.out.println("4. Return to Main Menu");
			selection = getMenuSelection(console, 1, 4);
			
			if(selection != 4){
				List<Book> result = searchForBook(console, selection);
				
				System.out.printf("%s Result(s) found\n\n", result.size());
				for(int i = 0; i < result.size(); i++){
					System.out.println(result.get(i).toString());
					System.out.println();
				}
				
				if(!yesOrNoQuestion(console, "Perform another search y/n?: ")){
					selection = 4;
				}
			}
		}
	}
	
	/**
	 * Acquires search results based on 
	 * the method selected by the user
	 * @param console Scanner object for user input
	 * @param method number indicating the search method
	 * <p>
	 * 1 = Author Last Name; 2 = Author First name; 3 = ISBN
	 * @return List of books discovered in catalog that match the user's criteria
	 */
	private static List<Book> searchForBook(Scanner console, int method){
		List<Book> results = new ArrayList<Book>();
		System.out.println("Enter Search Criteria");
		String input = console.nextLine();
		
		switch(method){
			case 1:
				results = BOOK_CATALOG.searchByAuthorLastName(input);
				break;
			case 2:
				results = BOOK_CATALOG.searchByAuthorFirstName(input);
				break;
			case 3:
				results = BOOK_CATALOG.searchByISBN(input);
				break;
		}
		
		return results;
	}
	
	/**
	 * Adds a book to the catalog
	 * @param console Scanner object for user input
	 */
	private static void addBook(Scanner console){
		boolean isValidInput = false;
		String title = "";
		String firstName = "";
		String lastName = "";
		String isbn = "";
		int bookYear = 0;
		float bookPrice = 0;
		
		while(!isValidInput){
			System.out.println("You can stop this process at any point by typing 'quit'");
			try{
				title = getAddBookStringInput(console, "Book Title:", false, false, false);
				firstName = getAddBookStringInput(console, "Author First Name:", false, false, false);
				lastName = getAddBookStringInput(console, "Author Last Name:", false, false, false);
				bookYear = Integer.parseInt(getAddBookStringInput(console, "Year Published:", true, false, false));
				bookPrice  = Float.parseFloat(getAddBookStringInput(console, "Book Price:", true, true, false));
				isbn = getAddBookStringInput(console, "ISBN#:", false, false, true);
			}
			catch(NumberFormatException ex){
				System.out.println(INVALID_INPUT_MSG);
				if(!yesOrNoQuestion(console, "Try adding book again? y/n:")){
					break;
				}
			}
			catch(IOException ex){
				System.out.println("Book Entry Interrupted.");
			}
		}
		
		if(isValidInput){
			Book book = new Book(title, isbn, bookPrice);
			book.setAuthorFirstName(firstName);
			book.setAuthorLastName(lastName);
			book.setBookYear(bookYear);
			BOOK_CATALOG.add(book);
			System.out.println("Book Successfully Added");
		}
	
	
		
	}
	
	private static void removeBook(Scanner console){
		int upperBound = BOOK_CATALOG.size();
		
		if(upperBound <= 0){
			System.out.println("The catalog is currently empty");
		}
		else{
			printCatalog();
			System.out.println("Enter the number(#) of the book to remove");
			int selection = getMenuSelection(console, 1, upperBound);
		}
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
	
	/**
	 * Presents a question to the user that requires a
	 * yes/no answer
	 * @param console Scanner object for user input
	 * @param question Text of the question to ask
	 * @return true if answered yes, otherwise no
	 */
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
	
	/**
	 * Validates and collects the information necessary for creating adding
	 * a book to the catalog
	 * @param console Scanner object for user input
	 * @param question Text of the question to be asked
	 * @param isNumber set to true if you wish to validate a numeric value(book year)
	 * @param isFloat set to true if you wish to validate a book price.  isNumber must also be true
	 * @param isISBN set to true if the expected input is an ISBN number
	 * @return String representation of validated result
	 */
	private static String getAddBookStringInput(Scanner console, String question, boolean isNumber, boolean isFloat, boolean isISBN)throws NumberFormatException, IOException{
		boolean isValidInput = false;
		String input = null;
		while(!isValidInput){
			System.out.println(question);
			input = console.nextLine();
			if(input.equals("q")){
				throw new IOException();
			}
			if(isNumber){
				if(isFloat){
					if(Float.parseFloat(input) > 0.00f){
						isValidInput = true;
					}
				}
				else{
					if(Integer.parseInt(input) <= 2013 && Integer.parseInt(input) > 0){
						isValidInput = true;
					}
				}
			}
			else if(isISBN){
				isValidInput = isValidISBN(input.toUpperCase());
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
	
	/**
	 * Determines if the given ISBN is valid
	 * @param isbn the ISBN to validate
	 * @return true if the number is valid, otherwise false
	 */
	private static boolean isValidISBN(String isbn){
		boolean isValid = false;
		int dashDigit = (int)'-';
		
		if(isbn.length() > 9 && isbn.length() < 14){
			Integer checkSum = null;
			int dashCount = 0;
			int digitTotal = 0;
			int digitCount = 0;
			for(int i = 0; i < isbn.length(); i++){
				int value = 0;
				char isbnChar = isbn.toCharArray()[i];
				//get checksum value at the end of the isbn string
				if(i == isbn.length() -1){
					if((int)isbnChar == 88){
						checkSum = 10;
					}
					else if((int)isbnChar > 47 && (int)isbnChar < 58){
						checkSum = Integer.parseInt(String.valueOf(isbn.charAt(i)));
					}
					else{
						break;
					}
				}
				else{
					if((int)isbnChar > 47 && (int)isbnChar < 58){
						value = Integer.parseInt(String.valueOf(isbn.charAt(i)));
					}
					else if((int)isbnChar == dashDigit){
						value = dashDigit;
					}
					else{
						break;
					}
				}
				
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
			if(digitCount == 10){
				int determinedChecksum = digitTotal % 11;
				if(determinedChecksum == checkSum){
					isValid = true;
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

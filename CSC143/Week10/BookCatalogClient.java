import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * CSC 143 - Assignment 4: Book Catalog
 * @author Walter Holley III
 * Client for Book Catalog Assignment
 */
public class BookCatalogClient {
	private static final String BOOK_CATALOG_FILE = "bookList.txt";
	private static BookCatalog BOOK_CATALOG = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			processCatalogFile();
			System.out.println(BOOK_CATALOG.size());
		}
		catch(FileNotFoundException ex){
			System.out.println("The Book Catalog file could not be found.");
		}
		

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

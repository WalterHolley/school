import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Scanner;
import java.io.*;

public class WeeklyProblemNineTen {
	private static final String BOOK_A = "BookA.txt";
	private static final String BOOK_B = "BookB.txt";
	private static HashSet<String> BookA_hash;
	private static HashSet<String> BookB_hash;
	private static TreeSet<String> BookA_tree;
	private static TreeSet<String> BookB_tree;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Prepares hash and tree objects.
	 * Reads book files into a has and tree object
	 * for each book
	 */
	private static void init()
	throws FileNotFoundException{
		Scanner bookA = new Scanner(new File(BOOK_A));
		Scanner bookB = new Scanner(new File(BOOK_B));
		BookA_hash = new HashSet<String>();
		BookB_hash = new HashSet<String>();
		BookA_tree = new TreeSet<String>();
		BookB_tree = new TreeSet<String>();
		
		while(bookA.hasNextLine()){
			String[] bookLine = bookA.nextLine().split("\\s+");
			for(int i = 0; i < bookLine.length; i++){
				BookA_hash.add(bookLine[i]);
				BookA_tree.add(bookLine[i]);
			}
			
		}
		
	}
	

}

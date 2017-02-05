/**
 * @author Walter Holley III
 * CSC 143 - Weekly Problem 9 and 10
 * From the instructions:
 * 1. Choose 2 books (BookA and BookB) by the same author (use www.gutenberg.org (Links to an external site.)).
 * 2. Store the words in each of the books in a TreeSet and in a HashSet (make sure to close and open the files,
 *    if you are loading the words into the above data structures individually). After this, 
 *    you should have a total of 4 sets: BookA_hash, BookA_tree, BookB_hash, BookB_tree.
 * 3. Iterate through the words in BookA_hash and search for these words in both BookB_tree and in BookB_hash.
 * 4. Iterate through the words in Book A_tree and search for these words in both BookB_tree and BookB_hash.
 * 5. Iterate through the words in BookB_hash and search for these words in both BookA_tree and in BookA_hash.
 * 6. Iterate through the words in Book B_tree and search for these words in both BookA_tree and BookA_hash.
 */

import java.util.TreeSet;
import java.util.HashSet;
import java.util.Set;
import java.io.*;

public class WeeklyProblemNineTen {
	private static final String BOOK_A = "BookA.txt";
	private static final String BOOK_B = "BookB.txt";
	
	public static void main(String[] args) {
		boolean booksParsed = false;
		HashSet<String> BookA_hash = new HashSet<String>();
		HashSet<String> BookB_hash = new HashSet<String>();
		TreeSet<String> BookA_tree = new TreeSet<String>();
		TreeSet<String> BookB_tree = new TreeSet<String>();
		
		try{
			System.out.println("Parsing books into HashSets");
			booksParsed = parseBooksIntoSets(BookA_hash, BookB_hash);
			
			System.out.println("Parsing books into TreeSets");
			booksParsed = parseBooksIntoSets(BookA_tree, BookB_tree);
		}
		catch(FileNotFoundException ex){
			System.out.println("A book file was not found");
		}
		catch(IOException ex){
			System.out.println("An error occurred while reading the book files");
		}
		
		if(booksParsed){
			System.out.printf("==%s HashSet Time Execution==\n", BOOK_A);
			System.out.println("Executed against Book B HashSet and Book B TreeSet");
			wordSearch(BookA_hash, BookB_hash, BookB_tree);
			System.out.printf("==%s TreeSet Time Execution==\n", BOOK_A);
			System.out.println("Executed against Book B HashSet and Book B TreeSet");
			wordSearch(BookA_tree, BookB_hash, BookB_tree);
			System.out.printf("==%s HashSet Time Execution==\n", BOOK_B);
			System.out.println("Executed against Book A HashSet and Book A TreeSet");
			wordSearch(BookB_hash, BookA_hash, BookA_tree);
			System.out.printf("==%s TreeSet Time Execution==\n", BOOK_B);
			System.out.println("Executed against Book A HashSet and Book A TreeSet");
			wordSearch(BookB_tree, BookA_hash, BookA_tree);
		}
		
		System.out.println("Exiting Program");
		
	}
	
	/**
	 * Prepares hash and tree objects.
	 * Reads book files into generic set objects
	 * for each book
	 * @throws FileNotFoundException, IOException 
	 */
	public static boolean parseBooksIntoSets(Set<String> set1, Set<String> set2) throws FileNotFoundException, IOException{
		float processingTime = 0;
		boolean booksParsed = false;
		BufferedReader bookFile = null;
		
		bookFile = new BufferedReader(new FileReader(BOOK_A));
		processingTime = System.nanoTime();
		while(bookFile.ready()){
			String[] bookLine = bookFile.readLine().split("\\s+");
			for(int i = 0; i < bookLine.length; i++){
					set1.add(bookLine[i].toLowerCase());
			}			
		}
		
		bookFile.close();
		
		bookFile = new BufferedReader(new FileReader(BOOK_B));
		while(bookFile.ready()){
			String[] bookLine = bookFile.readLine().split("\\s+");
			for(int i = 0; i < bookLine.length; i++){
					set2.add(bookLine[i].toLowerCase());
			}
		}
		
		processingTime = (System.nanoTime() - processingTime) / 1000000000L;
		
		bookFile.close();
		System.out.printf("Total words from %s: %s\n", BOOK_A, set1.size());
		System.out.printf("Total words from %s: %s\n", BOOK_B, set2.size());
		System.out.printf("Total Setup Time: %.5f seconds\n\n", processingTime);
		booksParsed = true;
		return booksParsed;
	}
	
	/**
	 * Looks for words shared between a source set, and two
	 * other sets(hash and tree) that contain a different set of words
	 * @param source Set with results that will drive the search
	 * @param compareHash hash that will be compared to source object. same content as compareTree.
	 * @param compareTree tree that will be compared to source object.  same content as CompareHash
	 */
	private static void wordSearch(Set<String> source, HashSet<String> compareHash, TreeSet<String> compareTree){
		int hashOccurrences = 0;
		int treeOccurrences = 0;
		float hashTime = 0L;
		float treeTime = 0L;
		hashTime = System.nanoTime();
		for(String word: source){
			for(String hashWord: compareHash){
				if(word.toLowerCase().equals(hashWord.toLowerCase())){
					hashOccurrences++;
					break;
				}
			}
		}
		hashTime = (System.nanoTime() - hashTime) / 1000000000L;
		
		treeTime = System.nanoTime();
		for(String word: source){
			
			for(String treeWord: compareTree){
				if(word.toLowerCase().equals(treeWord.toLowerCase())){
					treeOccurrences++;
					break;
				}
			}
		}
		treeTime = (System.nanoTime() - treeTime) / 1000000000L;
		
		if(hashOccurrences == treeOccurrences){
			System.out.println("RESULTS");
			System.out.printf("Total shared words: %s\n", hashOccurrences);
			System.out.printf("Time expended for iterative Hash Search: %.2f seconds\n", hashTime);
			System.out.printf("Time expended for iterative Tree Search: %.2f seconds\n\n", treeTime);
		}
		else{
			System.out.println("There is a problem with the word search.  Both Books do not have the same number of similar words");
		}
		
	}
}

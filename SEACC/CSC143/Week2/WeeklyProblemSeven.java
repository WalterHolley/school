import java.util.ArrayList;
/**
 * 
 * @author Walter Holley III
 * CSC 143 - Weekly problem 7
 * From the instructions:
 * Write a method cleanCorruptData that accepts an ArrayList of integers and removes any adjacent pair of
 * integers in the list if the left element of the pair is smaller than the right element of the pair. Every pair's
 * left element is an even-numbered index in the list, and every pair's right element is an odd index in the
 * list. 
 *
 */

public class WeeklyProblemSeven {

	public static void main(String[] args) {
		System.out.print("Weekly Problem 7 - ArrayLists\nRunning Tests\n\n");
		oddListTest();
		evenListTest();
		emptyListTest();
		System.out.println("Execution Completed. Exiting Program.");
	}
	
	/**
	 * Removes corrupt number pairs from the list. 
	 * If list has an odd size, it removes the tail element
	 * Removes number pairs if left right element is greater than left element
	 * @param corruptList the list to fix
	 * @return fixed list
	 */
	public static ArrayList<Integer> cleanCorruptData(ArrayList<Integer> corruptList){
		
		for(int i = corruptList.size() - 1; i >= 0; i-= 2){
			if(corruptList.size() % 2 != 0){
				corruptList.remove(corruptList.size() - 1);
				i--;
			}			
			if(i - 1 >= 0){
				if(corruptList.get(i - 1) < corruptList.get(i)){
					corruptList.remove(i);
					corruptList.remove(i - 1);
				}
			}
		}
		return corruptList;
	}
	
	/**
	 * Executes a test with an odd sized list
	 */
	private static void oddListTest(){
		ArrayList<Integer> badList = new ArrayList<Integer>();
		badList.add(3);
		badList.add(7);
		badList.add(5);
		badList.add(5);
		badList.add(8);
		badList.add(5);
		badList.add(6);
		badList.add(3);
		badList.add(4);
		System.out.println("Test with odd sized list");
		System.out.printf("List Size: %s\nElements: %s\n", badList.size(), badList);
		
		badList = cleanCorruptData(badList);
		System.out.printf("Results: %s\n\n", badList);	
	}
	
	/**
	 * Executes a test with ane xens ized list
	 */
	private static void evenListTest(){
		ArrayList<Integer> badList = new ArrayList<Integer>();
		badList.add(3);
		badList.add(7);
		badList.add(5);
		badList.add(5);
		badList.add(8);
		badList.add(5);
		badList.add(6);
		badList.add(3);
		badList.add(4);
		badList.add(7);
		System.out.println("Test with even sized list");
		System.out.printf("List Size: %s\nElements: %s\n", badList.size(), badList);
		
		badList = cleanCorruptData(badList);
		System.out.printf("Results: %s\n\n", badList);	
	}
	
	/**
	 * Executes a test with an empty list
	 */
	private static void emptyListTest(){
		ArrayList<Integer> badList = new ArrayList<Integer>();
		System.out.println("Test with empty list");
		System.out.printf("List Size: %s\nElements: %s\n", badList.size(), badList);
		
		badList = cleanCorruptData(badList);
		System.out.printf("Results: %s\n\n", badList);
	}

}

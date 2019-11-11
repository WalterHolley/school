package differences;

import java.util.Arrays;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] array1 = new int[] {1,2,3,4,5,6,7};
		int[] array2 = new int[] {1,2,2,3,4,4,4,5,6};
		
		System.out.println("Array differences");
		System.out.println("Array 1");
		System.out.println(Arrays.toString(array1));
		System.out.println("Array 2");
		System.out.println(Arrays.toString(array2));
		System.out.println("Comparison Result");
		System.out.println(difference(array1, array2).toString());

	}
	
	/**
	 * Determines the differences between two arrays
	 * and writes a description of those differences to the console
	 * @param array1 the initial array
	 * @param array2 the array that will be compared to the initial array
	 */
	private static HashMap<Integer, Integer> difference(int[] array1, int[] array2) {
		HashMap<Integer, Integer> diffMap = new HashMap<Integer, Integer>();
		int maxIndex = 0;
		
		if(array1.length >= array2.length)
			maxIndex = array1.length - 1;
		else
			maxIndex = array2.length - 1;
		
		for(int i = 0; i <= maxIndex; i++) {
			
			if(array1.length - 1 >= i) {
				if(diffMap.containsKey(array1[i])) {
					diffMap.put(array1[i], diffMap.get(array1[i]) - 1);
				}
				else
					diffMap.put(array1[i], -1);
			}
			
			if(array2.length - 1 >= i) {
				if(diffMap.containsKey(array2[i])) {
					diffMap.put(array2[i], diffMap.get(array2[i]) + 1);
				}
				else
					diffMap.put(array2[i], 1);
			}
		}
		
		return diffMap;
		
	}

}

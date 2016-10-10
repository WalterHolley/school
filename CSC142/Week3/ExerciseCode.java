/*
 * CSC 142 - Week 3 Practice Exercises
 * Walter Holley III
 */
public class ExerciseCode {
	
	//Prints a sequence of numbers up to the given
	//Maximum number
	public static void printNumbers(int maxNumber){
		
		for(int i = 0; i < maxNumber; i++){
			System.out.print("[" + (i + 1) + "] ");
		}
	}
	
	//prints the powers of 2 up to the 
	//maximum power given
	public static void printPowersof2(int maxPower){
		
		for(int i = 0; i <= maxPower; i++){
			System.out.print((int)Math.pow(2, i) + " ");
		}
	}
}

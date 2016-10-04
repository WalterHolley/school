/*
 * Assignment #1 Exercise Question #11: TwoRockets
 * Walter Holley III
 * CSC142
 * Draws two rockets to the console.
 * demonstrates use of functions.
 */
public class TwoRockets {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		drawRockets();
	}
	
	//Draws two rockets to the console
	private static void drawRockets(){
		drawCone();
		drawBoxEnd();
		drawEmptyBox();
		drawBoxEnd();
		drawUSABox();
		drawBoxEnd();
		drawCone();
	}
	
	//Draws a box on the console
	private static void drawEmptyBox(){	
		System.out.println("|      | |      |");
		System.out.println("|      | |      |");
	}
	
	//Draws a box with 'United States' inside of it
	private static void drawUSABox(){
		System.out.println("|United| |United|");
		System.out.println("|States| |States|");
	}
	
	//Draws the nose of the rocket
	private static void drawCone(){
		System.out.println("   /\\       /\\   ");
		System.out.println("  /  \\     /  \\  ");
		System.out.println(" /    \\   /    \\ ");
	}
	
	//Draws the top/bottom of a box
	private static void drawBoxEnd(){
		System.out.println("+------+ +------+");
	}

}

//***********REFLECTIONS***********//
/* This was a fairly straightforward program as well.  It was really just an expansion on the previous assignment, except we had
 * to make use of functions in order to reduce the amount of code we write.  Didn't have to look up anything or this one.  Time
 * to complete was maybe 10 or 20 minutes.  I believe this assignment really tries to convey the importance or re-using code.
 * The tasks at hand would have been pretty tedious if you only did all of this from the main() function.  The best part of
 * this assignment was seeing the completed result.  Crude, but fun to look at!.
 */

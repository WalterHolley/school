/*
 * CSC 142
 * Week 2 Practice Exercise - Faces
 * Walter Holley III
 */
public class Face {
	
	// this main method draws one or more faces out of 5 separate subunits
	public static void main(String[] args) {
		hair1();
		eyes1();
		nose1();
		mouth1();
		chinNeck1();
		
		System.out.println();
		System.out.println();
		
		hair2();
		eyes2();
		nose2();
		mouth2();
		chinNeck2();
		
		System.out.println();
		System.out.println();
		
		// you can see that with just 2 options for each segment, 
		// you have multiple mix-and-match options
		
		hair1();
		eyes2();
		nose1();
		mouth2();
		chinNeck1();
		
		System.out.println();
		System.out.println();
		
		hair1();
		eyes2();
		nose2();
		mouth1();
		chinNeck2();
		
		System.out.println();
		System.out.println();
		
		//Custom face
		hair_wh();
		eyes_wh();
		nose_wh();
		mouth1();
		chinNeck1();
	}
	// Hair segments have dimensions 2 rows x 13 columns
	// Must join eyes segments which have structure: 
	// blank - | -  9 units - | - blank
	public static void hair1(){
		System.out.println("   _______");
		System.out.println("  /       \\");
		System.out.println(" /         \\");
		
	}
	
	public static void hair2(){
		System.out.println("  @@@@@@@@@");
		System.out.println(" @@@@@@@@@@@");
		
	}
	
	//Custom hair segment( a fancy hat
	public static void hair_wh(){
		System.out.println(" @@@@@@@ ");
		System.out.println(" @@@@@@@@");
		System.out.println(" @@@@@@@@@");
		System.out.println(" @@@@@@@@@@");
		System.out.println(" @@@@@@@@@@@");
		System.out.println(" @@@@@@@@@@@");
	}
	
	// Eye segments have dimensions 2 rows x 13 columns
	// Must join hair segments which have structure:
	// blank - | -  9 units - | - blank
	// Must join nose segments which have structure: 
	// blank - | -  9 units - | - blank
	public static void eyes1(){
		System.out.println(" | /\\   /\\ |");
		System.out.println("(| o    o  |)");
		
		
	}
	
	//Custom eyes portion
	public static void eyes_wh(){
		System.out.println(" |_________|");
		System.out.println("({|||||||||})");
		
	}
	
	public static void eyes2(){
		System.out.println(" |  \\   \\  |");
		System.out.println("(| o    o  |)");
	}
	
	// Nose segments have dimensions 3 rows x 13 columns
	// Must join eyes segments which have structure: 
	// blank - | -  9 units - | - blank
	// Must join mouth segments which have structure: 
	// blank - | -  9 units - | - blank
	public static void nose1(){
		System.out.println(" |   |     |");
		System.out.println(" |   |/    |");
		System.out.println(" |         |");
	}
	
	public static void nose2(){
		System.out.println("(|         |)");
		System.out.println(" |   |/    |");
		System.out.println(" |  wwww   |");
	}
	
	//Custom nose portion 
	public static void nose_wh(){
		System.out.println("(|   | |   |)");
		System.out.println(" |   /_\\   |");
		System.out.println(" |         |");
	}
	
		
	
	// Mouth segments have dimensions 3 rows x 13 columns
	// and nose segments which have structure:
	// blank - | -  9 units - | - blank
	// Must join chin/neck segments which have structure: 
	// blank - blank - \ - 7 units - / - blank - blank
	public static void mouth1(){
		System.out.println(" |         |");
		System.out.println(" | \\_____/ |");
		System.out.println(" \\  \\___/  /");
	}
	
	public static void mouth2(){
		System.out.println(" |         |");
		System.out.println(" | /\\____  |");
		System.out.println(" \\         /");
	}
	
	// Chin =/- neck segments have dimensions 3 rows x 13 columns
	// Must join mouth segments which have structure: 
	// blank - \ - 9 units - / - blank 
	public static void chinNeck1(){
		System.out.println("  \\_______/");
		System.out.println("     /  \\");	
	}
	
	public static void chinNeck2(){
		System.out.println("  \\@@@@@@@/");
		System.out.println("    @@@@@");
		System.out.println("     @@");
	}
	

}

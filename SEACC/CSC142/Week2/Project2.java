/*
 * Assignment #1 Project #2
 * Walter Holley III
 * CSC142
 * From the description:
 * Write a program that prints out similar letters to three people of your choosing. Each letter
 * should have at least one paragraph in common with each of the other two, but none of the letters
 * should be identical to each other.
 */
public class Project2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//print letters
		letterToDad();
		letterToBrother();
		letterToNate();
		
	}
	
	//letter for my dad
	private static void letterToDad(){
		System.out.println("Hey Dad!");
		commonGreeting();
		System.out.println();
		System.out.println("Hope things are going well back home.  Josh tells me you're enjoying retirement.");
		System.out.println("Are you following the playoffs at all?  Everyone out here is livid that their team");
		System.out.println("didn't make it.  I've just been gloating wearing the hat for the Cardinals.  The groans are glorious.");
		System.out.println("Are you still in that bowling league?  I'm surpised I haven't heard about you going pro by now!");
		System.out.println();
		System.out.println("Like I said, I've got a break coming up soon which should give me some time off.  I'll be\ncoming to visit soon.");
		System.out.println();
		familySignature();
		letterSpacing();
	}
	
	//letter for my brother
	private static void letterToBrother(){
		System.out.println("Hey Loser :P");
		commonGreeting();
		System.out.println();
		System.out.println("How's the old man been holding up?  He still moving around and not eating all that junk?");
		System.out.println("It was great seeing you out here when you visited.  A couple friends still ask about you!");
		System.out.println("Good jokes go far out here man, and you've got them in spades.");
		System.out.println("Speaking of spades, I've got your number when I get back in town.  You're goin' down this time!");
		System.out.println();
		familySignature();
		letterSpacing();
	}
	
	//letter to my buddy Nate
	private static void letterToNate(){
		System.out.println("Yo,");
		commonGreeting();
		System.out.println();
		System.out.println("On to more serious matters.  Remember that 'thing' we worked on a few months back? Well, someone's interested.");
		System.out.println("When I say someone, I mean someone that actually matters.  This could be big man, so I need you to come out here ASAP.");
		System.out.println("Have to talk to you in person, not over the phone.  I know you'll be out here in a couple days, but");
		System.out.println("I'm so excited I just had to send a letter. See ya in a few.  You're gonna dig it out here.");
		System.out.println();
		commonSignature();
		System.out.println("Walt");
		letterSpacing();
	}
	
	//standard greeting for each letter
	private static void commonGreeting(){
		System.out.println("It has certainly been awhile!  I hope this letter finds you well.");
		System.out.println("Things have certainly been busy up here with the project and all that, but");
		System.out.println("there's light at the end of the tunnel, and well deserved break is around the corner.");
		System.out.println("I can't wait to get back home for awhile to see you and everyone else.");
	}
	
	//Common signature header used for all letters
	private static void commonSignature(){
		System.out.println("All the best,");
	}
	
	//Full signature for family letters
	private static void familySignature(){
		commonSignature();
		System.out.println("Dave");
	}
	
	//Marks the end of a letter
	private static void letterSpacing(){
		System.out.println();
		System.out.println("*********************************END OF LETTER***************************");
		System.out.println();
		
	}
	
}

//***********REFLECTIONS***********//
/* I'd say this project was fairly straightforward.  It took the lessons from the "TwoRockets" assignment regarding
 * function re-use, and continues the enforce the importance of code re-use.  This assignment took me about 30 minutes.
 * The absolute majority of that was coming up with the letters.  The code was fairly easy, and no outside help or research was needed.
 * The best part of this assignment was just seeing the end result on the console.  Everything looked as it should.
 * 
 * 
*/

package project4;
/*
 * PROJECT 4
 * Create a black jack game using
 * ian inheritance model for your cards and deck.
 */

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	private static Scanner consoleReader;
	private static final String INVALID_SELECTION_MESSAGE = "Invalid Selection";
	private static final String INT_ERROR_MESSAGE = "Value must be a positive integer of at least 20, and at most the number of chips the player has.";
	private static final String NSF_ERROR_MESSAGE = "You do not have enough chips for this bet.";
	private static final int INT_STARTING_CHIPS = 500;
	

	public static void main(String[] args) {
		boolean runProgram = true;
		
		consoleReader = new Scanner(System.in);
		
		while(runProgram) {
			System.out.println("Black Jack");
			System.out.println("***Main Menu***");
			System.out.println("1 >>> Start New Game");
			System.out.println("2 >>> Exit Program");
			
			try {
				int selection = consoleReader.nextInt();
				
				switch(selection){
				case 1:
					playBlackJack();
					break;
				case 2:
					runProgram = false;
					break;
				default:
					throw new InputMismatchException();
				}
				
				
			}
			catch(InputMismatchException ex) {
				System.out.println(INVALID_SELECTION_MESSAGE);
				consoleReader.nextLine();
			}
		}
		
		consoleReader.close();
			
			
	}
	
	private static void playBlackJack() {
		//init black jack
		BlackJack game = new BlackJack(INT_STARTING_CHIPS);
		boolean keepPlaying = true;
		boolean keepDealing = true;
		
		//game loop start
		do {
			//ask for bet
			takeBet(game);
			
			
			//deal loop start
			do {
				//deal cards
				game.dealCards();
				
				//Print card values
				showHands(game);
				
							
				//check for end of game
				if(!game.isGameOver()) {
					
					showScores(game);
					
					if(!game.playerStands()) {
						//Check rules for bets
						checkRules(game);
						
						//check for hit, or stand.
						hitPlayer(game);
						
					}
				}
				else
					keepDealing = false;
				
				
				
			}
			while(keepDealing);
		
			//Announce winner of round
			announceWinner(game);
			
			//print end of game statistics.
			showGameStats(game);
		
		    //loop if player wants to keep playing
			keepPlaying = playAgain();
			keepDealing = true;
		}
		while(keepPlaying);
		
		//game loop end
		
		
		
	}
	
	private static boolean playAgain() {
		return askYesNoQuestion("Would you like to play again?");
	}
	
	/**
	 * prints a base set of game statistics to the console
	 * # games won, games played, and total winnings
	 * @param game blackjack game object
	 */
	private static void showGameStats(BlackJack game) {
		System.out.println();
		System.out.println("Hands played: " + game.getHandsPlayed());
		System.out.println("Hands won: " + game.getHandsWon());
		System.out.println("Total winnings: " + game.getTotalWinnings());
	}
	
	/**
	 * Prints the game's winner to the console.
	 * Or print 'in progress' if the game is still going.
	 * @param game Blackjack game object
	 */
	private static void announceWinner(BlackJack game) {
		if(game.isGameOver()) {
			System.out.println();
			if(game.isPlayerWinner())
				System.out.println("PLAYER WINS!");
			else
				System.out.println("DEALER WINS!");
			showScores(game);
		}
		else
			System.out.println("GAME IN PROGRESS");
	}
	
	/**
	 * Prints dealer and player hands to the console
	 * @param game game object
	 */
	private static void showHands(BlackJack game) {
		System.out.print("Dealer Hand:  ");
		game.showDealerHand();
		System.out.println();
		System.out.print("Your Hand:  ");
		game.showPlayerHand();
	}
	
	/**
	 * prints dealer and player scores to the console
	 */
	private static void showScores(BlackJack game) {
		System.out.print("Dealer Score: ");
		//print deal score
		System.out.println(game.getDealerScore());
		System.out.print("Player Score: ");
		System.out.println(game.getPlayerScore());
	}
	
	/**
	 * Takes a bet from the player
	 * @param game the blackjack game object
	 */
	private static void takeBet(BlackJack game) {
		boolean getBet = true;
		
		do {
			boolean result = true;
			System.out.println("Current Chips: " + String.valueOf(game.getPlayerChips()));
			System.out.println("Please place your bet, minimum bet is 20 chips.");
			try {
				result = game.startGame(consoleReader.nextInt());
				
				if(!result) {
					System.out.println(NSF_ERROR_MESSAGE);
				}
				else
					getBet = false;
				
			}
			catch(InputMismatchException ex) {
				System.out.println(INT_ERROR_MESSAGE);
					consoleReader.nextLine();
			}			
			
		}while(getBet);
		
	}
	
	
	private static void checkRules(BlackJack game) {
		if(game.canSplitPair()) {
			if(askYesNoQuestion("Would you like to split pairs?"))
				game.splitPair();
		}
		if(game.canDoubleDown()) {
			if(askYesNoQuestion("Would you like to double down?"))
				game.doubleDown();
		}
		if(game.canInsuranceBet()) {
			if(askYesNoQuestion("Would you like to make an insurance bet?"))
				game.doInsuranceBet(0);
		}
		
	}
	
	/**
	 * asks the player if they will take another card, or
	 * stop receiving cards
	 * @param game blackjack game object
	 * @return true if the player takes a hit, false if they choose to stand
	 */
	private static void hitPlayer(BlackJack game) {
		boolean askQuestion = true;
		
		do {
			System.out.println("Do you want a hit or wish to stand?");
			System.out.println("1>>>Hit! Give me another card.");
			System.out.println("2>>>Stand. I like the hand I have.");
			
			try {
				int decision = consoleReader.nextInt();
				consoleReader.nextLine();
				
				switch(decision) {
				case 1:
					System.out.println("Player Hits");
					askQuestion = false;
					break;
				case 2:
					game.standPlayer();
					System.out.println("Player Stands");
					askQuestion = false;
					break;
					default:
						throw new InputMismatchException();
				}
			}
			catch(InputMismatchException ex) {
				System.out.println("Decision must be 1 or 2.");
				consoleReader.nextLine();
			}
			
		}while(askQuestion);

	}
	
	/**
	 * Asks a question. 
	 * @param question Text of the question
	 * @result true if yes, otherwise no
	 */
	private static boolean askYesNoQuestion(String question) {
		boolean result = false;
		boolean askQuestion = true;
		String answer = null;
		
		do {
			System.out.println(question);
			System.out.println("y/n:");
			
			try {
				answer = consoleReader.nextLine();
				switch(answer.toLowerCase()) {
				case "y":
					result = true;
					askQuestion = false;
					break;
				case "n":
					askQuestion = false;
					break;
				default:
					throw new InputMismatchException();				
				}
			}
			catch(InputMismatchException ex) {
				System.out.println("answer must be a 'y' or 'n' character");
				consoleReader.nextLine();
			}
			
			
		}
		while(askQuestion);
			
		return result;
	}
		

}
	


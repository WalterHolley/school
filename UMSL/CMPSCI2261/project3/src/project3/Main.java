package project3;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	private static final int GAME_GRID_WIDTH = 7;
	private static final int GAME_GRID_HEIGHT = 6;
	private static final String INVALID_SELECTION_MESSAGE = "Invalid Selection";
	private static Scanner consoleReader;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean runProgram = true;
		consoleReader = new Scanner(System.in);
		
		do {
			System.out.println("Connect Four!");
			System.out.println("***Main Menu***");
			System.out.println("1 >>> Start New Game");
			System.out.println("2 >>> Exit Program");
			
			try {
				int selection = consoleReader.nextInt();
				
				switch(selection) {
				case 1:
					runGame();
					break;
				case 2:
					runProgram = false;
					break;
				default:
					System.out.println(INVALID_SELECTION_MESSAGE);
					
						
				}
			}
			catch(InputMismatchException ex) {
				System.out.println(INVALID_SELECTION_MESSAGE);
				consoleReader.nextLine();
			}
		}
		while(runProgram);

	}
	
	/**
	 * Runs the connect 4 game.
	 */
	private static void runGame() {
		Connect4Game game = new Connect4Game(GAME_GRID_WIDTH, GAME_GRID_HEIGHT);
		
		System.out.println("Welcome to Connect 4!");
		System.out.println(game.getPlayerTurn() + " won the coin toss, and will go first.");
		game.printGrid();
		
		while(!game.isGameOver()) {
			System.out.println();
			doPlayerTurn(game);
			if(game.isGameOver()) {
				if(!game.isDrawGame()) {
					System.out.println("The game is over.  " + game.getPlayerTurn() + " is the winner!");
				}
				else
					System.out.println("The game ends in a draw!");
				
				System.out.println();
				
			}
			
		}
	}
	
	/**
	 * Performs the turn of a player.  The function exits when 
	 * the turn is complete.
	 * @param game the connect for game instance
	 */
	private static void doPlayerTurn(Connect4Game game) {
		boolean isValidTurn = false;
		
		System.out.println("Player Turn: " + game.getPlayerTurn());
		while(!isValidTurn) {
			try {
				System.out.println(String.format("Drop a %s disk into column(1 - %s): ", game.getPlayerTurn().toLowerCase(),
						GAME_GRID_WIDTH));
				
				int selection = consoleReader.nextInt();
				
				if(selection < 1 || selection > GAME_GRID_WIDTH) {
					throw new InputMismatchException();
				}
				else {
					if(!game.addPiece(selection)) {
						System.out.println("There's no more space in this column.  Try again.");
					}
					else {
						isValidTurn = true;
					}
				}
			}
			catch(InputMismatchException ex) {
				System.out.println(INVALID_SELECTION_MESSAGE);
				consoleReader.nextLine();
			}
		}
		
		
	}

}

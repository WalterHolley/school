package project3;


public class Connect4Game {
	private char[][] gameGrid = null;
	private boolean redPlayerTurn = true;
	private boolean gameCompleted = false;
	private boolean isGridFull = false;
	private boolean isDrawGame = false;
	private int gridWidth;
	private int gridHeight;
	
	public Connect4Game(int gridWidth, int gridHeight) {
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		
		gameGrid = new char[gridWidth][gridHeight];
		
		if(Math.random() > 0.50)
			redPlayerTurn = false;
		
	}
	
	/**
	 * Adds a piece to the game board, and cycles the player turn.
	 * When a piece is added, the game will update it's game completed
	 * flag, and prints the game grid to the console
	 * @param column integer of column to place piece in
	 * @return true if move was successful, otherwise false
	 */
	public boolean addPiece(int column) {
		int index = column - 1;
		boolean result = false;
		char piece = Character.MIN_VALUE;
		
		for(int i = 0; i < gameGrid[0].length; i++) {
			if((int)gameGrid[index][i] == 0) {
				
				if(!redPlayerTurn) {
					piece = 'Y';
				}
				else {
					piece = 'R';
				}
				
				gameGrid[index][i] = piece;
				printGrid();
				result = true;
				if(!checkForWinner(i, index, piece)) {
					if(isGridFull) {
						isDrawGame = true;
						gameCompleted = true;
					}
					else if(!redPlayerTurn) {
						redPlayerTurn = true;
					}
					else
						redPlayerTurn = false;
				}
				else
					gameCompleted = true;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Checks if the game is over
	 * @return true if the game is over, otherwise false
	 */
	public boolean isGameOver() {
		return gameCompleted;
	}
	
	/**
	 * Indicates if the game grid is full,
	 * no moves can be made, and no player
	 * has a winning combination
	 * @return true if the game is a draw, otherwise false.
	 */
	public boolean isDrawGame() {
		return isDrawGame;
	}
	
	/**
	 * Prints the game grid
	 */
	public void printGrid() {
		boolean gridIsFull = true;
		
		for(int r = gridHeight - 1; r >= 0; r--) {
			System.out.print("|");
			for(int c = 0; c < gridWidth; c++) {
				if((int)gameGrid[c][r] == 0) {
					System.out.print(" ");
					gridIsFull = false;
				}
				else {
					System.out.print(gameGrid[c][r]);
				}
				
				System.out.print("|");
			}
			System.out.println();
		}
		System.out.println("===============");
		
		isGridFull = gridIsFull;
	}
	
	/**
	 * Indicates which player currently has a turn
	 * @return String indicating which color player has the current turn.
	 */
	public String getPlayerTurn() {
		String result = "Red";
		
		if(!redPlayerTurn)
			result = "Yellow";
		
		return result;
	}
	
	/**
	 * Updates the gameCompleted flag if a winner is found
	 * @return true if the game has been won
	 */
	private boolean checkForWinner(int rowIndex, int columnIndex, char piece) {
		boolean result = false;
		if(checkHorizontal(rowIndex, columnIndex, piece) ||
				checkVertical(rowIndex, columnIndex, piece) ||
				checkDiagonal(rowIndex, columnIndex, piece))
			result = true;
		
		return result;
	}
	
	/**
	 * helper function to the other checkDiagonal signature
	 * @param checkUp if true, searches up the game grid, otherwise down
	 * @param checkLeft if true, checks the left of the game grid, otherwise right
	 * @param rowIndex the row index where the search begins
	 * @param colIndex the column index where the search begins
	 * @param piece the piece to search for
	 * @return number of consecutive piece found in a given direction
	 */
	private int checkDiagonal(boolean checkUp, boolean checkLeft, int rowIndex, int colIndex, char piece) {
		int continuousFound = 0;
		if(colIndex >= 0 && colIndex <= gridWidth - 1 && rowIndex >= 0 && rowIndex <= gridHeight - 1) {
			
			if(isPiece(rowIndex, colIndex, piece)) {
				continuousFound++;
				
			}
			if(checkLeft)
				colIndex--;
			else
				colIndex++;
			if(checkUp)
				rowIndex++;
			else
				rowIndex--;
			
			continuousFound += checkDiagonal(checkUp, checkLeft, rowIndex, colIndex, piece);
					
		}
			
		return continuousFound;
	}
	/**
	 * Checks the diagonal directions on the game grid for a winning combination
	 * @param rowIndex row index to begin search
	 * @param columnIndex column index to begin search
	 * @param piece the piece to search for
	 * @return true if a diagonal combination is found, otherwise false
	 */
	private boolean checkDiagonal(int rowIndex, int columnIndex, char piece) {
		boolean result = false;
		int continuousFound = 0;
		int totalFound = 0;
		
		
		//check given location
		if(isPiece(rowIndex, columnIndex, piece))
			continuousFound++;
	
		//check upper right
		totalFound += checkDiagonal(true, false, rowIndex + 1, columnIndex + 1, piece);
		//check lower left
		totalFound += checkDiagonal(false, true, rowIndex - 1, columnIndex - 1, piece);
			
		if(totalFound + continuousFound < 4) {
			totalFound = 0;
			//check upper left
			totalFound += checkDiagonal(true, true, rowIndex + 1, columnIndex - 1, piece);
			//check lower right
			totalFound += checkDiagonal(false, false, rowIndex - 1, columnIndex + 1, piece);
		}
		
		if(totalFound + continuousFound >= 4)
			result = true;

		return result;
	}
	
	/**
	 * Checks the vertical of the game grid for a winning combination
	 * @param rowIndex the row index of the game grid to begin search
	 * @param columnIndex the column index of the game grid to begin search
	 * @param piece the character of the piece to look for
	 * @return true if a winning vertical pattern pattern is found, otherwise false
	 */
	private boolean checkVertical(int rowIndex, int columnIndex, char piece) {
		boolean result = false;
		int continuousFound = 0;
		int upperBound = gridHeight - 1;
		int lowerBound = 0;
		int offset = 1;
		boolean doneUp = false;
		boolean doneDown = false;
		
		if(isPiece(rowIndex, columnIndex, piece)) {
			continuousFound++;
			
			do {
				//check up
				if(!doneUp) {
					if(rowIndex + offset <= upperBound) {
						if(isPiece(rowIndex + offset, columnIndex, piece))
							continuousFound++;
						else
							doneUp = true;
					}
					else
						doneUp = true;
				}
				//check down
				if(!doneDown) {
					if(rowIndex - offset >= lowerBound) {
						if(isPiece(rowIndex - offset, columnIndex, piece))
							continuousFound++;
						else
							doneDown = true;
					}
					else
						doneDown = true;
				}
				
				if(continuousFound >= 4) {
					result = true;
					break;
				}
					
				offset++;
			}
			while(!doneUp || !doneDown);			
		}
						
		

		return result;	
	}
	
	/**
	 * Checks the horizontal of the game grid for a winning combination
	 * @param rowIndex the row index of the game grid to begin search
	 * @param columnIndex the column index of the game grid to being search
	 * @param piece the character of the piece to look for.
	 * @return true if a winning horizontal pattern is found, otherwise false.
	 */
	private boolean checkHorizontal(int rowIndex, int columnIndex, char piece) {
		boolean result = false;
		int continuousFound = 0;
		int upperBound = gridWidth - 1;
		int lowerBound = 0;
		boolean doneLeft = false;
		boolean doneRight = false;
		int offset = 1;
		
		if(isPiece(rowIndex, columnIndex, piece)) {
			continuousFound++;
			
			do {
				//check left
				if(!doneLeft) {
					if(columnIndex - offset >= lowerBound) {
						if(isPiece(rowIndex, columnIndex - offset, piece))
							continuousFound++;
						else
							doneLeft = true;
					}
					else
						doneLeft = true;
				}
				//check right
				if(!doneRight) {
					if(columnIndex + offset <= upperBound) {
						if(isPiece(rowIndex, columnIndex + offset, piece))
							continuousFound++;
						else
							doneRight = true;
					}
					else
						doneRight = true;
				}
				
				if(continuousFound >= 4) {
					result = true;
					break;
				}
					
				offset++;
			}
			while(!doneLeft || !doneRight);			
		}
				
		
		
		
		return result;
	}
	
	/**
	 * Check to see if a given piece is located at a given location in the
	 * game's connect 4 grid
	 * @param rowIndex zero based index of the row for the piece
	 * @param ColumnIndex zero based column index for the piece
	 * @param piece character of the piece to search for.
	 * @return true if the parameters find the piece, otherwise false.
	 */
	private boolean isPiece(int rowIndex, int columnIndex, char piece) {
		boolean result = false;
		
		if((int)gameGrid[columnIndex][rowIndex] != 0) {
			if(gameGrid[columnIndex][rowIndex] == piece)
				result = true;
		}
		
		return result;
	}

	
}

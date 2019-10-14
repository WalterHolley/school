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
	 * Checks the diagonal directions on the game grid for a winning combination
	 * @param rowIndex row index to begin search
	 * @param columnIndex column index to begin search
	 * @param piece the piece to search for
	 * @return true if a diagonal combination is found, otherwise false
	 */
	private boolean checkDiagonal(int rowIndex, int columnIndex, char piece) {
		boolean result = false;
		int continuousFound = 0;
		int verticalUpperBound = rowIndex + 3 > gridHeight - 1 ? gridHeight - 1 : rowIndex + 3;
		int verticalLowerBound = rowIndex - 3 < 0 ? 0 : rowIndex - 3;
		int horizontalUpperBound = columnIndex + 3 > gridWidth - 1 ? gridWidth - 1 : columnIndex + 3;
		int horizontalLowerBound = columnIndex - 3 < 0 ? 0 : columnIndex - 3;
		int currentRow = rowIndex;
		int currentCol = columnIndex;
		
		//check given location
		if(isPiece(rowIndex, columnIndex, piece))
			continuousFound++;
		
		//check upper right
		
		while(true) {
			currentRow++;
			currentCol++;
			if(currentRow <= verticalUpperBound && currentCol <= horizontalUpperBound) {
				if(isPiece(currentRow, currentCol, piece))
					continuousFound++;
				else
					break;
			}
			else
				break;
		}
		
		currentRow = rowIndex;
		currentCol = columnIndex;
		//check lower left
		while(true) {
			currentRow--;
			currentCol--;
			if(currentRow >= verticalLowerBound && currentCol >= horizontalLowerBound) {
				if(isPiece(currentRow, currentCol, piece))
					continuousFound++;
				else
					break;
			}
			else
				break;
			
		}
		
		//check for win
		if(continuousFound < 4) {
			continuousFound = 0;
			currentRow = rowIndex;
			currentCol = columnIndex;
			
			if(isPiece(rowIndex, columnIndex, piece))
				continuousFound++;
			
			//check upper left
			while(true) {
				currentRow++;
				currentCol--;
				if(currentRow <= verticalUpperBound && currentCol >= horizontalLowerBound) {
					if(isPiece(currentRow, currentCol, piece))
						continuousFound++;
					else
						break;
				}
				else
					break;
			}
			
			currentRow = rowIndex;
			currentCol = columnIndex;
			
			//check lower right
			while(true) {
				currentRow--;
				currentCol++;
				if(currentRow >= verticalLowerBound && currentCol <= horizontalUpperBound) {
					if(isPiece(currentRow, currentCol, piece))
						continuousFound++;
					else
						break;
				}
				else
					break;
			}
			
			//check for win
			if(continuousFound >= 4)
				result = true;
			
		}
		else
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
		int upperBound = rowIndex + 3 > gridHeight - 1 ? gridHeight - 1 : rowIndex + 3;
		int lowerBound = rowIndex - 3 < 0 ? 0 : rowIndex - 3;
		
		//check up
		for(int i = rowIndex; i <= upperBound; i++) {
			if(isPiece(i, columnIndex, piece))
				continuousFound++;
			else
				break;
		}
		
		//check down, do not recount the piece given to the function
		if(continuousFound < 4) {
			for(int i = rowIndex - 1; i >= lowerBound; i--) {
				if(isPiece(i, columnIndex, piece))
					continuousFound++;
				else
					break;
			}
		}
		
		if(continuousFound >= 4) {
			result = true;
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
		int upperBound = columnIndex + 3 > gridWidth - 1 ? gridWidth - 1 : columnIndex + 3;
		int lowerBound = columnIndex - 3 < 0 ? 0 : columnIndex - 3;
		
		//check left
		for(int i = columnIndex; i >= lowerBound; i--) {
			if(isPiece(rowIndex, i, piece))
				continuousFound++;
			else
				break;
		}
		
		//check right, do not recount the piece given to the function
		if(continuousFound < 4) {
			for(int i = columnIndex + 1; i <= upperBound; i++) {
				if(isPiece(rowIndex, i, piece))
					continuousFound++;
				else
					break;
			}			
		}
		
		if(continuousFound >= 4)
			result = true;
		
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

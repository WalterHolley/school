package project4;

import java.util.ArrayList;
import java.util.HashMap;


public class BlackJack extends Deck {
	private final int DEALER_LIMIT = 17;
	private final int MINIMUM_BET = 20;
	private final int SCORE_LIMIT = 21;
	
	private boolean isRoundOver = false;
	private boolean isPlayerStands = false;
	private boolean isDealerStands = false;
	private boolean canInsuranceBet = false;
	private boolean isFirstDeal = false;
	private boolean canSplitPair = false;
	private boolean insuranceBetWon = false;
	private boolean isPayoutComplete = false;
	private boolean canDoubleDown = false;
	private boolean isPlayerWinner = false;
	
	private int roundsPlayed = 0;
	private int roundsWon = 0;
	private int totalWinnings = 0;
	private int playerChips = 0;
	private int dealerScore = 0;
	private int playerScore = 0;
	private int currentBet = 0;
	
	private ArrayList<String> playerHand;
	private ArrayList<String> playerSplitHand;
	private ArrayList<String> dealerHand;
	private ArrayList<Deck> dealerDeck;
	private HashMap<String, Integer> scoreHash;
	
	public BlackJack(int chipAmount) {
		playerChips = chipAmount;
		buildDeck();
		
		//build score table
		scoreHash = new HashMap<String, Integer>();
		scoreHash.put("2", 2);
		scoreHash.put("3", 3);
		scoreHash.put("4", 4);
		scoreHash.put("5", 5);
		scoreHash.put("6", 6);
		scoreHash.put("7", 7);
		scoreHash.put("8", 8);
		scoreHash.put("9", 9);
		scoreHash.put("10", 10);
		scoreHash.put("J", 10);
		scoreHash.put("Q", 10);
		scoreHash.put("K", 10);
		
		
	}
	
	/**
	 * begin new round of Black Jack
	 */
	public boolean startGame(int bet) {
		boolean result = false;
		if(bet >= MINIMUM_BET) {
			result = makeBet(bet);
		}
		
		
		if(result) {
			playerHand = new ArrayList<String>();
			dealerHand = new ArrayList<String>();
			playerSplitHand = null;
			
			resetGameFlags();
			shuffleDeck();
		}
				
		return result;
		
	}
	
	/**
	 * splits the hand of the player if possible
	 */
	public void splitPair() {
		if(this.isFirstDeal) {
			if(this.canSplitPair) {
				
			}
		}
	
	}
	
	/**
	 * indicates if the player's hand has ended
	 * @return true if the hand has ended, otherwise false.
	 */
	public boolean playerStands() {
		return isPlayerStands;
	}
	
	/**
	 * Indicates if the player can split a 
	 * pair
	 * @return true if split pair is available
	 * otherwise false
	 */
	public boolean canSplitPair() {
		if(this.isFirstDeal && checkForPairs()) {
			this.canSplitPair = true;
		}
		else
			canSplitPair = false;
		
		return canSplitPair;
	}
	
	/**
	 * 'hits' the player with the next
	 * card, or stands the player. Dealing them
	 * no more cards.
	 * @param doHit true to hit, false to indicate
	 * the player stands
	 */
	public void hitPlayer(boolean doHit) {
		
		if(!doHit)
			isPlayerStands = true;
	}
	
	/**
	 * Ends the round for the player.
	 * player can no longer receive cards
	 */
	public void standPlayer() {
		isPlayerStands = true;
	}
	
	/**
	 * Indicates if the game is over
	 * @return true if the game is over, otherwise false
	 */
	public boolean isGameOver() {
		return isRoundOver;
	}
	
	/**
	 * Indicates if the player won the game.
	 * Result cannot be trusted until the game is over
	 * @return true if the player won, otherwise false;
	 */
	public boolean isPlayerWinner() {
		return isPlayerWinner;
	}
	
	/**
	 * Determines if the first deal has happened or not
	 * @return true if first deal has not happened, otherwise false
	 */
	public boolean isFirstDeal() {
		return isFirstDeal;
	}
	
	/**
	 * Indicates if the double down rule can be applied
	 * @return
	 */
	public boolean canDoubleDown() {
		boolean result = false;
		
		if(this.isFirstDeal) {
			//player score must be from 9 to 11
		}
		
		return result;
	}
	
	/**
	 * Indicates if an insurance bet can be made
	 * @return true if yes, otherwise false
	 */
	public boolean canInsuranceBet() {
		return canInsuranceBet;
	}
	
	/**
	 * deals cards to the player and dealer
	 */
	public void dealCards() {
		
		if(!isRoundOver) {
			if(!isPlayerStands)
				playerHand.add(getCardFromDeck());
			
			if(!isDealerStands)
				dealerHand.add(getCardFromDeck());
			
			//deal an additional card to each player
			if(isFirstDeal) {
				playerHand.add(getCardFromDeck());
				dealerHand.add(getCardFromDeck());		
			}
		}
		
		updateBetFlags();
		updateScores();
		
	}
	
	/**
	 * get number of hands played
	 * @return int of hands played
	 */
	public int getHandsPlayed() {
		return roundsPlayed;
	}
	
	public int getTotalWinnings() {
		return totalWinnings;
	}
	
	/**
	 * gets hands won by the player
	 * @return int of total hands
	 * won by the player
	 */
	public int getHandsWon() {
		return roundsWon;
	}
	
	/**
	 * Get score of the dealer
	 * @return int of the dealer score
	 */
	public int getDealerScore() {
		return dealerScore;
	}
	
	/**
	 * Gets score of the player
	 * @return int of player score
	 */
	public int getPlayerScore() {
		return playerScore;
	}
	
	/**
	 * Prints the hand of the player to the console
	 */
	public void showPlayerHand() {
		for(int i = 0; i < playerHand.size(); i++) {
			System.out.print(playerHand.get(i) + " ");
			
		}
		System.out.println();
	}
	
	/**
	 * Prints the hand of the dealer to the console
	 */
	public void showDealerHand() {
		for(int i = 0; i < dealerHand.size(); i++) {
			
			if(!isRoundOver) {
				if(i == dealerHand.size() - 1)
					System.out.print("*");
				else
					System.out.print(dealerHand.get(i) + " ");
			}
			else{
				if(i < dealerHand.size() - 1)
					System.out.print(dealerHand.get(i) + " ");
			}
				
				
		}
		System.out.println();
	}
	
	/**
	 * Double the original bet.
	 * Will only succeed if the first two cards dealt total
	 * from 9-11, and the players chip balance can cover the bet.
	 * 
	 * @return true for a successful bet, otherwise false.
	 */
	public boolean doubleDown() {
		boolean result = false;
		
		return result;
	}
	
	/**
	 * 
	 * @param amount number of chips to bet
	 * @return true if insurance bet is won, otherwise false.
	 */
	public boolean doInsuranceBet(int amount) {
		boolean result = false;
		
		return result;
	}
	
	
	/**
	 * 
	 * @return integer representing the number of chips held by the
	 * player
	 */
	public int getPlayerChips() {
		return this.playerChips;
	}
	
	/**
	 * 
	 * Builds the initial blackjack deck which contains 6 different decks of cards.
	 */
	private void buildDeck() {
		dealerDeck = new ArrayList<Deck>();
		dealerDeck.add(new Deck());
		dealerDeck.add(new Deck());
		dealerDeck.add(new Deck());
		dealerDeck.add(new Deck());
		dealerDeck.add(new Deck());
		dealerDeck.add(new Deck());
	}
	
	/**
	 * Retrieve card from deck
	 * @return String of the card face
	 */
	private String getCardFromDeck() {
		String result = null;
		int deckToPull = (int)Math.random()* (dealerDeck.size() - 1);
		
		//remove deck if cards are too low
		if(dealerDeck.get(deckToPull).size() == 0) {
			dealerDeck.remove(deckToPull);
			result = getCardFromDeck();
		}
		else
			result = dealerDeck.get(deckToPull).drawCard();
		
		return result;
	}
	
	/**
	 * Takes a bet from the player for the round
	 * @param chips number of chips to bet
	 * @return true if the bet was accepted, otherwise false.
	 */
	private boolean makeBet(int chips) {
		boolean result = false;
		
		if(chips <= playerChips) {
			currentBet = chips;
			playerChips -= chips;
			result = true;
		}
		
		return result;
		
	}
	
	/**
	 * Shuffles the deck. For this case,
	 * it means adding up to 6 decks back to the
	 * blackjack deck if 2 or fewer decks remain.
	 */
	private void shuffleDeck() {
		if(dealerDeck.size() <= 2) {
			for(int i = dealerDeck.size(); i <= 6; i++)
				dealerDeck.add(new Deck());
		}
	}
	
	/**
	 * checks for pairs in the players hand
	 * during the first deal
	 * @return true if pair is found, otherwise false
	 */
	private boolean checkForPairs() {
		boolean result = false;
		
		if(isFirstDeal) {
			if(playerHand.size() == 2) {
				if(playerHand.get(0) == playerHand.get(1)) {
					result = true;
				}
			}
		}
		
		return result;
	}
	private void updateBetFlags()
	{
		if(this.isFirstDeal) {
			if(playerScore >= 9 && playerScore <= 11)
				canDoubleDown = true;
			
			if(checkForPairs()) {
				
				
			}
			
			isFirstDeal = false;
		}
		else {
			canDoubleDown = false;
			canSplitPair = false;
			canInsuranceBet = false;
		}
		
	}
	

	/**
	 * Calculates scores and verifies winning/losing
	 * conditions for the game
	 */
	private void updateScores() {
		int playerAceCount = 0;
		int dealerAceCount = 0;
		int maxIndex = 0;
		playerScore = 0;
		dealerScore = 0;
		
		if(playerHand.size() > dealerHand.size())
			maxIndex = playerHand.size();
		else
			maxIndex = dealerHand.size();
		
		for(int i = 0; i < maxIndex; i++) {
			if(i < playerHand.size()) {
				if(playerHand.get(i) == "A")
					playerAceCount++;
				else {
					playerScore += scoreHash.get(playerHand.get(i));
					
				}
					
				
			}
			
			if(i < dealerHand.size() - 1) {
				if(dealerHand.get(i) == "A")
					dealerAceCount++;
				else
					dealerScore += scoreHash.get(dealerHand.get(i));
			}
		}
		
		playerScore = optimizeScore(true, playerAceCount);
		dealerScore = optimizeScore(false, dealerAceCount);
		
		//check for automatic conditions that would end card deals
		if(dealerScore >= DEALER_LIMIT)
			isDealerStands = true;
		if(playerScore >= SCORE_LIMIT) {
			isRoundOver = true;
			isPlayerStands = true;
		}
		
		if(isDealerStands && isPlayerStands){
			isRoundOver = true;
		}
		
		if(dealerScore >= SCORE_LIMIT) {
			isRoundOver = true;
			isDealerStands = true;
		}
		
		//assign winner
		if(isRoundOver) {
			int dealerDifference = SCORE_LIMIT - dealerScore;
			int playerDifference = SCORE_LIMIT - playerScore;
			if(dealerDifference < 0 && playerDifference >= 0)
			{
				isPlayerWinner = true;
			}
			else if(playerDifference >= 0 && dealerDifference >= 0 && dealerDifference >= playerDifference)
				isPlayerWinner = true;
			
			processRound();
			
		}
		
			
		
			
		
	}
	
	/**
	 * calculates the statistics for the round,
	 * and performs the payout to the winner
	 */
	private void processRound() {
		if(!isPayoutComplete) {
			roundsPlayed++;
			if(!isPlayerWinner) {
				totalWinnings -= currentBet;
			}
			else {
				totalWinnings += currentBet;
				roundsWon++;
				playerChips += currentBet * 2;
			}
			
		}
	
	}
	
	/**
	 * restore initial game flag settings
	 */
	private void resetGameFlags() {
		isRoundOver = false;
		isPlayerStands = false;
		isDealerStands = false;
		canInsuranceBet = false;
		isFirstDeal = true;
		canSplitPair = false;
		insuranceBetWon = false;
		isPayoutComplete = false;
		canDoubleDown = false;
		isPlayerWinner = false;
	}
	
	/**
	 * Updates the current score to its best possible score based on
	 * a given ace count
	 * @param isPlayerScore set true to optimize the player score,
	 * otherwise the dealer's score will be optimized
	 * @param aceCount the number of aces in the player's hand
	 */
	private int optimizeScore(boolean isPlayerScore, int aceCount) {
		int score = 0;
		
		if(isPlayerScore)
			score = playerScore;
		else
			score = dealerScore;
		
		if(aceCount > 0) {
			if(score + 11 > SCORE_LIMIT) {
				score += 1 * aceCount;
			}
			else {
				score += 11 + (1 * (aceCount - 1));
			}
		}
		
		return score;
	}
	
}

package project4;

public class CardType extends Card{
	private String suitName;
	
	public CardType(String suitName) {
		this.suitName = suitName;
		this.init();
	}
	
	/**
	 * 
	 * @return suit of the card
	 */
	public String getSuit() {
		return this.suitName;
	}
	
	/**
	 * number of cards of this type remaining
	 * @return int representing the number of
	 * cards remaining of the card type
	 */
	public int count() {
		return this.faceCount();
	}
	
	public String getCardFace() {
		String cardFace = null;
		
		cardFace = getValue();
		return cardFace;
	}

}

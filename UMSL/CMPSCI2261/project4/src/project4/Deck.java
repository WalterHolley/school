package project4;

import java.util.ArrayList;

public class Deck {
	private ArrayList<CardType> cards;
	private int cardCount = 0;
	
	protected Deck() {
		init();
	}
	
	/**
	 * Creates a deck with a set of standard playing cards
	 */
	private void init() {
		cards = new ArrayList<CardType>();
		
		cards.add(new CardType("Diamond"));
		cards.add(new CardType("Club"));
		cards.add(new CardType("Heart"));
		cards.add(new CardType("Spade"));
		
		cards.forEach((c) -> cardCount += c.count() );
		
	}
	
	
	/**
	 * Retrieves a random card from the deck
	 * @return
	 */
	protected String drawCard() {
		
		return getCard();
	}
	
	protected int size() {
		return cardCount;
	}
	
	/**
	 * Selects a card from a random suit
	 * @return
	 */
	private String getCard() {
		int index =(int)Math.random() * (cards.size() - 1);
		String result = null;
		
		if(cardCount != 0) {
			
			if(cards.get(index).count() == 0) {
				cards.remove(index);
				result = getCard();
			}
			else {
				result = cards.get(index).getCardFace();
				cardCount--;
			}
			
		}
			
		return result;
	}
}

package project4;

import java.util.ArrayList;

public class Card {
	
private ArrayList<String> cardValues = new ArrayList<String>(); 

	/**
	 * retrieves a random face value for the card,
	 * and removes the returned value from the possible
	 * faces to be drawn
	 * @return
	 */
	protected String getValue() {
		String result = null;
		int index =  (int)(Math.random() * (cardValues.size() - 1));
		
		if(cardValues.size() > 0) {
			result = cardValues.remove(index);
		}
		
		return result;
		
	}
	
	protected int faceCount() {
		return cardValues.size();
	}
	
	/**
	 * Initializes the card collection
	 */
	public void init() {
		cardValues.add("2");
		cardValues.add("3");
		cardValues.add("4");
		cardValues.add("5");
		cardValues.add("6");
		cardValues.add("7");
		cardValues.add("8");
		cardValues.add("9");
		cardValues.add("10");
		cardValues.add("A");
		cardValues.add("J");
		cardValues.add("Q");
		cardValues.add("K");
	}
	
}

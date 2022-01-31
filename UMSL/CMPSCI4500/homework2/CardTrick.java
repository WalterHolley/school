import java.util.LinkedList;

//Run this application using the Java setting at onlinegdb.com
/**
     * CMP SCI 4500
     * HOMEWORK 2 - Card Trick Assignment.
     * February, 2022
     * Performs a card trick for the user.  Re-arranges
     * a series of cards face up, and reveals the card chosen by the user.
     * @author Walter Holley III
     
 */
public class CardTrick {
    
    private List<String> cardCollection = new List<String>();
    private LinkedList<String> cardsInUse;

    public static void main(){
        init();
        menu();
    }

    /**
     * Initializes elements needed for the application
     */
    private static void init(){

    }

    /**
     * Primary menu loop.  This is where
     * the user experience starts
     */
    private static void menu(){

        while(mainMenu()){
            //ask user to choose a card

            //shuffle card back into deck
            placeCard("cardFace", 0)
            //show new card pile face-up
            //get card arrays to show
            dealCards();

            //ask user to choose pile(first time: select 1,2, or 3)
            //cut the cards
            cutGroup(0);

            //show new card pile face-up
            //get card arrays to show
            dealCards();

            //ask user to choose pile(second time: select 1,2, or 3)
             //cut the cards
             cutGroup(0);

            //show new card pile face-up
            //get card arrays to show
            dealCards();

            //ask user to choose pile(third time: select 1,2, or 3)
             //cut the cards
             cutGroup(0);
            
            //show new card pile face-up
             //get card arrays to show
            dealCards();

            //reveal chosen card
        }
    }

    /**
     * Shows the main menu of the application.
     * User can choose to perform the card trick or exit
     * @return false if user chooses to exit
     */
    private static boolean mainMenu(){
        
        return false; 
    }

    /**
     * Deals the cards. Deals by placing each card 
     * into one of three different piles. creating
     * 3 rows of 7 cards
     * @return two dim string array representing card piles
     */
    private static string[][] dealCards(){

    }

    /**
     * Places a grouping if 7 cards in the middle of the deck
     * @param group the group number to place in the middle
     * @throws IllegalArgumentException when group parameter is not 1, 2, or 3
     */
    private static void cutGroup(int group) throws IllegalArgumentException{

    }

    /**
     * 
     * @param cardKey
     * @param position
     * @throws IllegalArgumentException when cardKey doesn't exist, or position is 
     * less than 1 or greater than 20
     */
    private static void placeCard(String cardKey, int position) throws IllegalArgumentException{


    }

    /**
     * 
     */
    private static void initCardList(){

    }

}

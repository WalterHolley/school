package supplemental;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class HarisCode {

        //Represents a full deck of cards
        private static List<String> cardCollection;

        //The list of cards to use for the trick
        private static List<String> cardsInUse;
    
        //Scanner for accepting input from console
        private static Scanner inputObject;
   /* public static void main(String[] args){
        initCardList();

        menu();
    }*/

    /**
     * Initializes elements needed for the application
     */
    private static void init(){
        initCardList();
    }

    private static void printPile(String[][] pile)
    {
        for(int i= 0 ; i< 3; i++)
        {
            System.out.print("Pile " + (i+1) + " : ") ;
            for(int j= 0 ; j< 7; j++)
            {
                if(j == 6)
                {
                    System.out.println(pile[i][j]);
                }
                else
                {
                    System.out.print(pile[i][j] + "   ");
                }
            }
        }
    }
    //**USER EXPERIENCE**//

    /**
     * Primary menu loop.  This is where
     * the user experience starts
     */
    private static void menu(){
        inputObject = new Scanner(System.in) ;
        while(mainMenu()){

            // Printing cards
            System.out.println("Cards List : " + cardsInUse) ;
            //ask user to choose a card
            System.out.print("Select One Card : ") ;
            String cardName = inputObject.next() ;

            //shuffle card back into deck
            placeCard(cardName, 3);
            //show new card pile face-up
            //get card arrays to show
            printPile(dealCards());

            //ask user to choose pile(first time: select 1,2, or 3)
            System.out.print("Choose pile : ") ;
            int pileN = inputObject.nextInt() ;
            //cut the cards
            cutGroup(pileN);

            //show new card pile face-up
            //get card arrays to show
            printPile(dealCards());

            //ask user to choose pile(second time: select 1,2, or 3)
            //cut the cards
            System.out.print("Choose pile : ") ;
            pileN = inputObject.nextInt() ;
            cutGroup(pileN);

            //show new card pile face-up
            //get card arrays to show
            printPile(dealCards());

            //ask user to choose pile(third time: select 1,2, or 3)
            //cut the cards
            System.out.print("Choose pile : ") ;
            pileN = inputObject.nextInt() ;
            cutGroup(pileN);

            //show new card pile face-up
            //get card arrays to show
            printPile(dealCards());

            //reveal chosen card
            System.out.println("Your Chosen Card : " + cardName) ;
        }
    }

    /**
     * Shows the main menu of the application.
     * User can choose to perform the card trick or exit
     * @return false if user chooses to exit
     */
    private static boolean mainMenu(){
        inputObject = new Scanner(System.in) ;
        System.out.print("You Want to Perform Card Trick or want to exit.. Choose 1 to play and 0 for exit: ");
        int choice = inputObject.nextInt() ;
        if(choice == 1)
        {
            init();
            return true ;
        }
        else
        {
            return false;
        }
    }


//***CARD OPERATIONS**//

    /**
     * Deals the cards. Deals by placing each card
     * into one of three different piles. creating
     * 3 rows of 7 cards
     * @return two dim string array representing card piles
     */
    private static String[][] dealCards(){
        String[][] pile = new String [3][7];
        Random rng = new Random();
        for(int i= 0 ; i< 3; i++)
        {
            for(int j= 0 ; j< 7; j++)
            {
                pile[i][j] = cardsInUse.get(rng.nextInt(21));
            }
        }
        return pile;

    }

    /**
     * Places a grouping if 7 cards in the middle of the deck
     * @param group the group number to place in the middle
     * @throws IllegalArgumentException when group parameter is not 1, 2, or 3
     */
    private static void cutGroup(int group) throws IllegalArgumentException{

        if( group < 1 || group > 3)
            throw new IllegalArgumentException("Invalid Group Selection");
        else{
            int startIndex = 7 * (group - 1);
            Random rng = new Random();
            List<String> topGroup = new ArrayList<String>();
            List<String> usedCards = new ArrayList<String>();
            usedCards.addAll(cardsInUse) ;
            cardsInUse.removeAll(usedCards);
            //Get group to place in middle
            List<String>middleGroup = usedCards.subList(startIndex, startIndex + 6);
            cardsInUse.removeAll(middleGroup);

            //decide which pile to place on top
            if(rng.nextInt(1) > 0)
                startIndex = 0;
            else
                startIndex = 7;
            topGroup = usedCards.subList(startIndex, startIndex + 6);
            cardsInUse.removeAll(topGroup);

            //place piles
            cardsInUse.addAll(usedCards) ;
            cardsInUse.addAll(middleGroup);
            cardsInUse.addAll(topGroup);
        }
    }

    /**
     *
     * @param cardKey The card you wish to insert
     * @param position the integer position to insert the card into
     * @throws IllegalArgumentException when cardKey doesn't exist, or position is
     * less than 1 or greater than 20
     */
    private static void placeCard(String cardKey, int position) throws IllegalArgumentException{

        if(!cardsInUse.contains(cardKey))
            throw new IllegalArgumentException("Card not found");
        else if(position < 1 || position > 20)
            throw new IllegalArgumentException("Invalid Card Position");
        else{
            cardsInUse.remove(cardsInUse.indexOf(cardKey));
            cardsInUse.add(position, cardKey);
        }

    }

    /**
     * Initializes the deck, and randomly selects 21 cards for use in the trick.
     */
    private static void initCardList(){
        cardCollection = new ArrayList<String>();
        cardsInUse = new ArrayList<String>();
        String[] hearts = {"1H","2H","3H", "4H", "5H", "6H", "7H", "8H", "9H", "10H","JH","QH", "KH", "AH"};
        String[] diamonds = {"1D","2D","3D", "4D", "5D", "6D", "7D", "8D", "9D", "10D","JD","QD", "KD", "AD"};
        String[] clubs = {"1C","2C","3C", "4C", "5C", "6C", "7C", "8C", "9C", "10C","JC","QC", "KC", "AC"};
        String[] spades = {"1S","2S","3S", "4S", "5S", "6S", "7S", "8S", "9S", "10S","JS","QS", "KS", "AS"};

        cardCollection.addAll(Arrays.asList(hearts));
        cardCollection.addAll(Arrays.asList(diamonds));
        cardCollection.addAll(Arrays.asList(clubs));
        cardCollection.addAll(Arrays.asList(spades));
        //Randomly select cards from deck. add to usage pile, remove card from deck
        while(cardsInUse.size() < 21){
            Random rng = new Random();

            //get random value between zero and deck size - 1
            int randVal = rng.nextInt(cardCollection.size() - 1);
            cardsInUse.add(cardCollection.get(randVal));
            cardCollection.remove(randVal);
        }


    }

}

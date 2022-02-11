import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

//Run this application using the Java setting at onlinegdb.com
/**
     * CMP SCI 4500
     * HOMEWORK 2 - Card Trick Assignment.
     * February, 2022
     * Performs a card trick for the user.  Re-arranges
     * a series of cards face up, and reveals the card chosen by the user.
     * @author Walter Holley III, Haris Bihorac
     
 */
public class CardTrick {
    
    //Represents a full deck of cards
    private static List<String> cardCollection;

    //The list of cards to use for the trick
    private static List<String> cardsInUse;

    //Scanner for accepting input from console
    private static Scanner inputObject;
    private static String userCard;





    public static void main(String[] args){
        menu();
    }

    /**
     * Initializes elements needed for the application
     */
    private static void init(){
        userCard = null;
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

    //*****USER EXPERIENCE*****//  
    /**
     * Primary menu loop.  This is where
     * the user experience starts
     */
    private static void menu(){

        inputObject = new Scanner(System.in) ;
        boolean running  = false;
        running = mainMenu();
        while(running){

            // Printing cards
            System.out.println("Cards List : " + cardsInUse) ;
            //ask user to choose a card
            selectCard();

            // ask user to shuffle card back into deck
            placeCardInDeck();
            //show new card pile face-up
            //get card arrays to show
            printPile(dealCards());

            //ask user to choose pile(first time: select 1,2, or 3)
            int pileN = chooseCardPile();
            //cut the cards
            cutGroup(pileN);

            //show new card pile face-up
            //get card arrays to show
            printPile(dealCards());

            //ask user to choose pile(second time: select 1,2, or 3)
            //cut the cards
            pileN = chooseCardPile();
            cutGroup(pileN);

            //show new card pile face-up
            //get card arrays to show
            printPile(dealCards());

            //ask user to choose pile(third time: select 1,2, or 3)
            //cut the cards
            pileN = chooseCardPile();
            cutGroup(pileN);

            //show new card pile face-up
            //get card arrays to show
            printPile(dealCards());

            //reveal chosen card
            System.out.println("Your Chosen Card : " + userCard.toUpperCase()) ;

            //Ask to play again
            running = playAgain();
        }
    }

    /**
     * Asks the user if they would like to perform the trick again
     * @return True if the user wishes to perform the trick again.
     */
    private static boolean playAgain(){
        boolean result = false;
        boolean asking = true;

        while(asking){
            try{

                System.out.print("Would you like to play again(y/n)? ");
                String response = inputObject.next();

                if( response.toLowerCase().equals("y")){
                    asking = false;
                    result = true;
                }
                else if(response.toLowerCase().equals("n")){
                    asking = false;
                }
                else{
                    throw new InputMismatchException();
                }

            }
            catch(InputMismatchException ex){
                System.out.println("Error:  Input can only be Y, y, n, or N.  Please try again");
            }
           

        }
        return result;
    }

    /**
     * Lets the user chose which card pile their selected
     * card is located in.  
     * @return integer indicating which pile the card is stored in.
     */
    private static int chooseCardPile(){
        int pileN = 0;
        boolean selecting = true;

        while(selecting){
            try{
                System.out.print("Choose pile : ");
                pileN = inputObject.nextInt();
    
                if(pileN < 1 || pileN > 3)
                    throw new InputMismatchException();
                else
                    selecting = false;
    
    
            }
            catch(InputMismatchException ex){
                System.out.println("Number must be from 1 to 3(inclusive).  Try again");
            }
            catch(Exception ex){
                System.out.println("Number must be from 1 to 3(inclusive).  Try again");
            }
        }
        
        return pileN;
    }
    /**
     * Asks the user to place the card they've selected
     * back into the deck.
     */
    private static void placeCardInDeck(){
        boolean selecting = true;
        System.out.println("Place the card back in the deck");
        
        while(selecting){
            System.out.println("Enter a number between 1 and 21(inclusive): ");

            try{
                int spaceNumber = inputObject.nextInt();
                
                if(spaceNumber < 1 || spaceNumber > 21){
                    throw new Exception("Invalid Selection. Please Try Again");
                }
                else{
                    spaceNumber--;
                    placeCard(userCard, spaceNumber);
                    selecting = false;
                }
            }
            catch(InputMismatchException ex){
                System.out.println("ERROR: Invalid Selection.  Please Try Again");
                inputObject.nextLine();
            }
            catch(Exception ex){
                System.out.println("ERROR: " + ex.getMessage());
                
            }
            

        }
    }

    /**
     * Prompts a user to select a card.
     */
    private static void selectCard(){

        boolean selecting = true;

        while(selecting){
            System.out.print("Select One Card(ex. 10C, 9H): ") ;

            try{
                String cardName = inputObject.next() ;
                
                if(!cardsInUse.contains(cardName.toUpperCase())){
                    throw new Exception("Card Not Found. Please Try Again");
                }
                else{
                    userCard = cardName;
                    selecting = false;
                }
            }
            catch(Exception ex){
                System.out.println("ERROR: " + ex.getMessage());
            }
            
        }
    }

    /**
     * Shows the main menu of the application.
     * User can choose to perform the card trick or exit
     * @return false if user chooses to exit
     */
    private static boolean mainMenu(){
        
        //inputObject = new Scanner(System.in) ;
        int choice = 0;

        System.out.print("You Want to Perform Card Trick or want to exit.. Choose 1 to play or any key to exit: ");
        try{
            choice = inputObject.nextInt();
        }
        catch(Exception ex){
            //perform no action
        }
        
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




//******CARD OPERATIONS*****//

    /**
     * Deals the cards. Deals by placing each card 
     * into one of three different piles. creating
     * 3 rows of 7 cards
     * @return two dim string array representing card piles
     */
    private static String[][] dealCards(){

        String[][] cardGroups = new String[3][7];
        int column = 0;

        for(int i = 0; i < cardsInUse.size(); i++){
            cardGroups[0][column] = cardsInUse.get(i);
            cardGroups[1][column] = cardsInUse.get(i + 1);
            cardGroups[2][column] = cardsInUse.get(i + 2);
            i += 2;
            column++;
        }
        return cardGroups;

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
            List<String> bottomGroup = new ArrayList<String>();
            List<String> newCardList = new ArrayList<String>();
            int leftRight = rng.nextInt(1);

            //Get group to place in middle
            List<String>middleGroup = cardsInUse.subList(startIndex, startIndex + 7);

            //decide which piles to place on top and bottom.
            // chose the right pile for top if 1
            //otherwise left
            if(leftRight == 1)
                if(startIndex == 7){
                    topGroup = cardsInUse.subList(startIndex + 7, startIndex + 14);
                    bottomGroup = cardsInUse.subList(startIndex - 7, startIndex);
                }                  
                else if(startIndex == 0){
                    topGroup = cardsInUse.subList(startIndex + 7, startIndex + 14);
                    bottomGroup = cardsInUse.subList(startIndex + 14 , startIndex + 21);
                }    
                else{
                    topGroup = cardsInUse.subList(startIndex + 7, startIndex + 14);
                    bottomGroup = cardsInUse.subList(startIndex + 14 , startIndex + 21);
                }                  
            else{
                if(startIndex == 7){
                    topGroup = cardsInUse.subList(startIndex - 7, startIndex);
                    bottomGroup = cardsInUse.subList(startIndex + 7, startIndex + 14);
                }
                else if(startIndex == 0){
                    topGroup = cardsInUse.subList(startIndex + 14, startIndex + 21);
                    bottomGroup = cardsInUse.subList(startIndex + 7, startIndex + 14);
                }
                else{
                    topGroup = cardsInUse.subList(startIndex - 7, startIndex);
                    bottomGroup = cardsInUse.subList(startIndex - 14, startIndex - 7);
                }
            }

            //place piles
            newCardList.addAll(bottomGroup);
            newCardList.addAll(middleGroup);
            newCardList.addAll(topGroup);

            
            cardsInUse = newCardList;
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
        
        if(!cardsInUse.contains(cardKey.toUpperCase()))
            throw new IllegalArgumentException("Card not found");
        else if(position < 0 || position > 20)
            throw new IllegalArgumentException("Invalid Card Position");
        else{
            cardsInUse.remove(cardsInUse.indexOf(cardKey.toUpperCase()));
            cardsInUse.add(position, cardKey.toUpperCase());
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

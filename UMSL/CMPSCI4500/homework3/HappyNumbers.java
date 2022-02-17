//Run this application using the Java setting at onlinegdb.com
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * CMP SCI 4500
 * Homework 3 - Happy Prime Numbers 
 * Asks the user for a number range,
 * and displays happy primes found within
 * that range.  Then displays the results.
 * @author Walter Holley III
 * @implNote Definition happy numbers provided by wikipedia https://en.wikipedia.org/wiki/Happy_number
 * @implNote Definition of prime numbers provided by wikipedia https://en.wikipedia.org/wiki/Prime_number
 */
public class HappyNumbers{

    private static Scanner scanner;
    public static void main(String[] args){
        run();
    }

    /**
     * Primary execution of program
     */
    private static void run(){

        boolean asking  = true;
        int lowerBound = 0;
        int upperBound = 0;

        while(asking){
            System.out.println("Enter a lowerbound");
            lowerBound = askForNumber(1);
            System.out.println("Enter an upperbound");
            upperBound = askForNumber(lowerBound);
        }
    }

    /**
     * Displays an explanation of Happy
     * Prime numbers used in this application
     */
    private static void showExplanation(){
        System.out.println("Happy numbers are numbers that can reach 1 when replaced by the sum of the square of each digit.");
        System.out.println("Prime numbers are numbers greater than one that are not products of two smaller numbers.");
        System.out.println("10-Happy Prime numbers are natural, base 10 numbers that meet the conditions for happy and prime numbers.");
        System.out.println("Lets find the 10-Happy Prime Numbers between a range of numbers you select");

    }

    /**
     * Asks for a number from the user
     * will ask until a number from
     * one to one million is received
     * @return the number received
     */
    private static int askForNumber(int lowerBound){

        int result = 0;
        boolean asking = true;
        if(scanner == null){
            scanner = new Scanner(System.in);
        }

        while(asking){
            try{
                System.out.print("Select a number from " + loweBound + " to 1,000,000(inclusive): ");
                result = scanner.nextInt();
                if(result < 1 || result > 1000000)
                    throw new InputMismatchException();
                asking  = false;
            }
            catch(InputMismatchException ex){
                System.out.println("ERROR: The value you've entered is invalid.  Please try again.");
            }
            
        }

        return result;
    }

    private static boolean isPrime(int num){
        boolean result = false;

        return result;
    }

    private static boolean isHappyNumber(int num){
        boolean result = false;

        return result;
    }

    private static int[] getHappyPrimes(int loweBound, int upperBoud){
        List<Integer> happyPrimes = new ArrayList<Integer>();

        return happyPrimes.toArray();
    }
}
//Run this application using the Java setting at onlinegdb.com
//No external files are required for this application
//primeList is a global list for maintaining prime numbers
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
    private static List<Integer> primeList;
    public static void main(String[] args){
        showExplanation();
        run();

        if(scanner != null)
            scanner.close();
        
        System.out.println("End of program.");
    }

    /**
     * Primary execution of program
     */
    private static void run(){

        
        int lowerBound = 0;
        int upperBound = 0;

        //get lower and upperbound
        System.out.println("Enter a lowerbound");
        lowerBound = askForNumber(1);
        System.out.println("Enter an upperbound");
        upperBound = askForNumber(lowerBound);

        //init prime numbers
        initPrimes(upperBound);

        //get happy primes
        int[] results = getHappyPrimes(lowerBound);
        
        //display results
        switch(results.length){

            case 0:
                System.out.println("No happy prime numbers found.");
                break;
            case 1:
                printHappyPrimes(results, lowerBound, upperBound);
                break;
            case 2:
                printHappyPrimes(results, lowerBound, upperBound);
                System.out.printf("The number of digits between the two happy primes is %d.\n", results[1] - results[0] - 1);
                break;              
            default:
                printHappyPrimes(results, lowerBound, upperBound);
                printMultiResults(results);
                break;
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

    private static void printHappyPrimes(int[] happyPrimes, int lowerBound, int upperBound){
        int count = 0;

        if(happyPrimes.length == 1)
            System.out.printf("There is %d happy prime between %d and %d(inclusive).  It is:\n", happyPrimes.length, lowerBound, upperBound);
        else
            System.out.printf("There are %d happy primes between %d and %d(inclusive).  They are:\n", happyPrimes.length, lowerBound, upperBound);
        
        for(int i = 0; i < happyPrimes.length; i++){
            System.out.print(happyPrimes[i]);
            if(i < happyPrimes.length - 1)
                System.out.print(",");
            
            if(count == 10 || i == happyPrimes.length - 1){
                System.out.println();
                count = 0;
            }
            else
                count++;
                
        }
    }

    /**
     * Prints and analysis of multiple happy
     * prime results
     * @param happyPrimes array of happy primes found
     */
    private static void printMultiResults(int[] happyPrimes){
        int totalGaps = happyPrimes.length - 1;
        List<Integer> gapList = new ArrayList<Integer>(totalGaps);
        List<Integer> resultList = Arrays.stream(happyPrimes).boxed().collect(Collectors.toList());
        int sum = 0;
        int median = 0;
        int average = 0;
        int min = 0;
        int max = 0;
        resultList.sort(Comparator.naturalOrder());

        //create gap list, determine sum, min, max of gaps
        for(int i = 0; i < totalGaps; i++){
            gapList.add(i, resultList.get(i+1) - resultList.get(i) - 1);
            sum += gapList.get(i);
            if(gapList.get(i) > max)
                max = gapList.get(i);
            if(min == 0 || min > gapList.get(i))
                min = gapList.get(i);
    
        }
        

        //average gap
        average = sum / gapList.size();

        //print gap information
        System.out.println("Here are the gaps");
        for(int i = 0; i < gapList.size(); i++){
            System.out.printf("%d - %d: %d\n",resultList.get(i), resultList.get(i + 1), gapList.get(i));
        }

        gapList.sort(Comparator.naturalOrder());

        //median gap
        if(gapList.size() % 2 == 0){
            int size = gapList.size() / 2;
            median = (gapList.get(size) + gapList.get(size - 1)) / 2;
        }
        else
            median = gapList.get(((totalGaps + 1) / 2) - 1);
        
        //print gap statistics
        System.out.println("Gap Statistics");
        System.out.println("Average Gap: " + average);
        System.out.println("Smallest Gap: " + min);
        System.out.println("Largest Gap: " + max);
        System.out.println("Median Gap: " + median);
            
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
                System.out.print("Select a number from " + lowerBound + " to 1,000,001(inclusive): ");
                result = scanner.nextInt();
                if(result < lowerBound || result > 1000001)
                    throw new InputMismatchException();
                asking  = false;
            }
            catch(InputMismatchException ex){
                System.out.println("ERROR: The value you've entered is invalid.  Please try again.");
                scanner.nextLine();
            }
            
        }

        return result;
    }

    /**
     * initializes the prime number list
     * @param upperBound find primes up to this number
     * (inclusive)
     */
    private static void initPrimes(int upperBound){

        if(primeList == null){
            primeList = new ArrayList<Integer>();
            primeList.add(2);
        }
        if(upperBound > 2){
            for(int i = 3; i <= upperBound; i++){
                isPrime(i);
            }
        }
    }

    /**
     * Determines if the given integer is a prime number
     * @param num the number to check
     * @return True if prime number
     */
    private static boolean isPrime(int num){
        boolean result = false;
        if(num > 1){

            if(primeList.contains(num)){
                result = true;
            }
            else{
                int limit = (int)Math.sqrt(num);
                for(int i = 0; i < primeList.size(); i++){
                    if(primeList.get(i) > limit){
                        result = true;
                        primeList.add(num);
                        break;
                    }
                        
                    if(num % primeList.get(i) == 0)
                        break;
                }
            }
        }

        return result;
    }

    /**
     * Determines if the given number is a happy number
     * @param num the value to analyze
     * @return True if given number is a happy number
     */
    private static boolean isHappyNumber(int num){
        boolean result = false;
        char[] intArray = Integer.toString(num).toCharArray();
        int val = 0;
        
        //for base 10, we can iterate a maximum of 8 times before the cycle repeats
        for(int i = 1; i <= 8; i++){
            
            for(int c = 0; c < intArray.length; c++){
                int parsedVal = Integer.parseInt(String.valueOf(intArray[c]));
                val += Math.pow(parsedVal,2);
            }
            if(val == 1){
                result = true;
                break;
            }
            else
                intArray = Integer.toString(val).toCharArray();
                
            val = 0;
        }

        return result;
    }

    

    /**
     * Searches for happy prime numbers within
     * a given range. Inclusively.
     * @param loweBound the lower number of the range. included in search
     * @param upperBoud the highest number of the range.  included.
     * @return array of happy primes found within range.
     */
    private static int[] getHappyPrimes(int lowerBound){
        List<Integer> happyPrimes = new ArrayList<Integer>();

        for(int i = 0; i < primeList.size(); i++){
            if(primeList.get(i) < lowerBound)
                continue;
            if(isHappyNumber(primeList.get(i)))
                happyPrimes.add(primeList.get(i));
        }

        return happyPrimes.stream().mapToInt(i->i).toArray();
    }
}
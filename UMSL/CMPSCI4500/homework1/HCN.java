import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HCN {
    /**
     * CMP SCI 4500
     * HOMEWORK 1 - HCN Assignment.
     * January, 2022
     * Takes an integer from a user, and provides the greatest High Composite
     * Number(HCN) closest to that integer
     * 
     * @author Walter Holley III
     * @implNote HCN list provided courtesy of github
     *           https://gist.github.com/dario2994/fb4713f252ca86c1254d#file-list-txt
     */
    // Max integer limit for application
    private static final int NUM_LIMIT = 10000000;
    private static ArrayList<Integer> primeList = new ArrayList<>();
    private static HashMap<Integer, Integer> HCN_LIST;
    private static final String SELECTION_ERROR = "You've made an invalid selection.  Try again.";

    public static void main(String[] args) {
        init();
        menu();
    }

    /**
     * Setup application prior to execution
     */
    private static void init() {
        HCN_LIST = getStaticHCNList();
        System.out.println(HCN_LIST.size());
    }

    /**
     * Creates the main menu for the program.
     * Allows the user to get the HCN value, or exit the application
     */
    private static void menu() {
        int selection = 0;

        while (selection != 2) {
            System.out.println("Please make a selection:");
            System.out.println("1>>>Find HCN for number");
            System.out.println("2>>>Exit");

            try {
                selection = Integer.parseInt(System.console().readLine());
            } catch (NumberFormatException ex) {
                selection = 3;
            }

            switch (selection) {
                case 1:
                    getNumber();
                    break;
                case 2:
                    break;
                default:
                    System.out.println(SELECTION_ERROR);
            }
        }

    }

    /**
     * Retrieves a number within the specified number limit
     * for evaluation
     */
    private static void getNumber() {
        int numRetrieved = 0;

        while (numRetrieved < 1 || numRetrieved > NUM_LIMIT) {
            System.out.println("Enter a number between 1 and " + NUM_LIMIT + ".");
            try {
                numRetrieved = Integer.parseInt(System.console().readLine());

                if (numRetrieved < 1 || numRetrieved > NUM_LIMIT) {
                    throw new NumberFormatException("Invalid Selection");
                } else {
                    // perform HCN Process
                    int result = findHCN(numRetrieved);

                    if (numRetrieved == result)
                        System.out.println(numRetrieved + " is a Highly composite number, and has a total of "
                                + HCN_LIST.get(numRetrieved).toString() + " divisors.");
                    else
                        System.out.println(numRetrieved + " is not a Highly composite number.  The closest HCN is "
                                + result + ", which has " + HCN_LIST.get(result).toString() + " divisors.");
                }

            } catch (NumberFormatException ex) {
                System.out.println(SELECTION_ERROR);
            }

        }
    }

    /**
     * Determines if the given number is a prime number
     * 
     * @param number The number to check for prime
     * @return booelan true if prime, otherwise false
     */
    private static boolean isPrime(int number) {
        boolean result = false;

        if (number > 1) {
            if (number == 2)
                result = true;
            else {
                for (int i = 2; i < number; i++) {

                    if (number % i == 0)
                        break;
                    else if (i == number - 1)
                        result = true;

                }
            }

        }
        return result;
    }

    /**
     * Returns the closest number that is an HCN
     * 
     * @param number the number to begin searching with
     * @return int of HCN
     */
    private static int findHCN(int number) {
        int result = number;
        System.out.println(HCN_LIST.size());
        if (!HCN_LIST.containsKey(number)) {
            Integer[] keyArray = Arrays.copyOf(HCN_LIST.keySet().toArray(), HCN_LIST.size(), Integer[].class);

            int size = keyArray.length;

            result = searchArray(keyArray, 0, size - 1, number);

        }

        return result;
    }

    /**
     * Search for a number as close to the target as possible
     * 
     * @param searchArray an ordered array of integers
     * @param lowerIndex  the lowerbound index of the search
     * @param upperIndex  the upperbound index of the search
     * @param target      the number the seach wants to get closest to
     * @return closest integer to the target
     */
    private static int searchArray(Integer[] values, int lowerIndex, int upperIndex, int target) {
        int totalRecords = upperIndex - lowerIndex;
        int result = target;
        if (totalRecords > 1) {
            // get middle entry
            int middle = upperIndex - (totalRecords / 2);
            int candidate = values[middle];
            if (candidate > target)
                result = searchArray(values, middle, upperIndex, target);
            else
                result = searchArray(values, lowerIndex, middle, target);
        } else
            result = values[lowerIndex + 1];

        return result;
    }

    /**
     * Generates a hashmap containing Highly Composite Numbers
     * 
     * @param numLimit the max value to evaluate(this value will be evaluated as
     *                 well)
     * @return Hashmap of HCNs. Key is the HCN, value is the divisor
     */
    private static HashMap<Integer, Integer> getHCNList(int numLimit) {
        HashMap<Integer, Integer> hcnList = new HashMap<Integer, Integer>();

        for (int i = 1; i <= numLimit; i++) {

            // check if value is a prime number
            if (isPrime(i))
                primeList.add(i);

            // find factors of value
        }

        return hcnList;
    }

    /**
     * Returns a list of HCNs ranging from 1 to 10 million.
     * 
     * @return Hashmap of HCNs. key is number, value is number of divisors
     */
    private static HashMap<Integer, Integer> getStaticHCNList() {
        HashMap<Integer, Integer> hcnCollection = new HashMap<Integer, Integer>();

        hcnCollection.put(1, 1);
        hcnCollection.put(2, 2);
        hcnCollection.put(4, 3);
        hcnCollection.put(6, 4);
        hcnCollection.put(12, 6);
        hcnCollection.put(24, 8);
        hcnCollection.put(36, 9);
        hcnCollection.put(48, 10);
        hcnCollection.put(60, 12);
        hcnCollection.put(120, 16);
        hcnCollection.put(180, 18);
        hcnCollection.put(240, 20);
        hcnCollection.put(360, 24);
        hcnCollection.put(720, 30);
        hcnCollection.put(840, 32);
        hcnCollection.put(1260, 36);
        hcnCollection.put(1680, 40);
        hcnCollection.put(2520, 48);
        hcnCollection.put(5040, 60);
        hcnCollection.put(7560, 64);
        hcnCollection.put(10080, 72);
        hcnCollection.put(15120, 80);
        hcnCollection.put(20160, 84);
        hcnCollection.put(25200, 90);
        hcnCollection.put(27720, 96);
        hcnCollection.put(45360, 100);
        hcnCollection.put(50400, 108);
        hcnCollection.put(55440, 120);
        hcnCollection.put(83160, 128);
        hcnCollection.put(110880, 144);
        hcnCollection.put(166320, 160);
        hcnCollection.put(221760, 168);
        hcnCollection.put(277200, 180);
        hcnCollection.put(332640, 192);
        hcnCollection.put(498960, 200);
        hcnCollection.put(554400, 216);
        hcnCollection.put(665280, 224);
        hcnCollection.put(720720, 240);
        hcnCollection.put(1081080, 256);
        hcnCollection.put(1441440, 288);
        hcnCollection.put(2162160, 320);
        hcnCollection.put(2882880, 336);
        hcnCollection.put(3603600, 360);
        hcnCollection.put(4324320, 384);
        hcnCollection.put(6486480, 400);
        hcnCollection.put(7207200, 432);
        hcnCollection.put(8648640, 448);

        return hcnCollection;
    }

}

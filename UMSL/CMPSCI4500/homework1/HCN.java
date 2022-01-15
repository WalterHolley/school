public class HCN {
    /**
     * CMP SCI 4500
     * HOMEWORK 1 - HCN Assignment.
     * January, 2022
     * Takes an integer from a user, and provides the greatest High Composite
     * Number(HCN) closest to that integer
     * 
     * @author Walter Holley III
     * 
     */
    // Max integer limit for application
    private static final int NUM_LIMIT = 10000000;

    private static final String SELECTION_ERROR = "You've made an invalid selection.  Try again.";

    public static void main(String[] args) {
        menu();
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
                }

            } catch (NumberFormatException ex) {
                System.out.println(SELECTION_ERROR);
            }

        }
    }

}

public class HCN {

    private static final int NUM_LIMIT = 10000000;

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
                    System.out.println("You've made an invalid selection.  Try again.");
            }
        }

    }

    private static void getNumber() {
        boolean numRetrieved = false;

        while (!numRetrieved) {
            System.out.println();

        }
    }

}

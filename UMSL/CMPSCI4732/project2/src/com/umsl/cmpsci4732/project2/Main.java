package com.umsl.cmpsci4732.project2;


import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final String INVALID_SELECTION_MESSAGE = "Invalid selection";
    private static Settings settings;
    private static Scanner consoleReader;

    public static void main(String[] args) {
        init();
        boolean appRunning = true;

        while (appRunning) {
            System.out.println("*** PROJECT 2 ***");
            System.out.println("Please enter the number of your selection:");
            System.out.println("********************");
            System.out.println("1 >>>> View Settings");
            System.out.println("2 >>>> Change Settings");
            System.out.println("3 >>>> Exit Program");

            try{
                int selection = consoleReader.nextInt();
                consoleReader.nextLine();

                switch(selection) {
                    case 1:
                        //read settings
                        viewSettings();
                        break;
                    case 2:
                        //update settings
                        doDecrypt();
                        break;
                    case 3:
                        //end program
                        appRunning = false;
                        break;
                    default:
                        System.out.println(INVALID_SELECTION_MESSAGE);

                }
            }
            catch (InputMismatchException e) {
                System.out.println(INVALID_SELECTION_MESSAGE);
                consoleReader.nextLine();
            }

        }



        System.out.println("Exiting Program");
    }

    /**
     * Initializes the application.
     * Creates files and object neccessary
     * for program to funtion
     * @return true if initialization completed
     * successfully.  otherwise false.
     */
    private static void init(){
        settings = new Settings();
        consoleReader = new Scanner(System.in);

    }

    /**
     * Prints the contents of the configuration file to
     * the screen
     */
    private static void viewSettings(){
        System.out.println("Dog's Name: " + settings.getDogName());
        System.out.println(("Age: " + settings.getAge()));
        System.out.println("Weight: " + settings.getWeight() + " lbs");
        System.out.println("Press any key to continue");
        consoleReader.next();
        consoleReader.nextLine();
    }
    //UX for updating settings

}

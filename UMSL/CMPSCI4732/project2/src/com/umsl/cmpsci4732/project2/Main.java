package com.umsl.cmpsci4732.project2;


import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final String INVALID_SELECTION_MESSAGE = "Invalid selection";
    private static final String TRY_AGAIN_MESSAGE = "Would you like to try again? y/n: ";
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

                switch(selection) {
                    case 1:
                        //read settings
                        viewSettings();
                        break;
                    case 2:
                        //update settings
                        changeSettings();
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
        System.out.println("Press ENTER key to continue");
        try{
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * UX for updating configuration settings
     */
    private static void changeSettings(){
        boolean editing = true;

        while(editing){

            System.out.println("*** UPDATE SETTINGS ***");
            System.out.println("Please enter the number of your selection:");
            System.out.println("********************");
            System.out.println("1 >>>> Change dog's name");
            System.out.println("2 >>>> Change dog's age");
            System.out.println("3 >>>> Change dog's weight");
            System.out.println("4 >>>> Back(commits changes)");

            try{
                int selection = consoleReader.nextInt();

                switch(selection) {
                    case 1:
                        //update dog name
                        String newName = askForText("Enter the dog's new name");
                        if(newName != null && !newName.isEmpty())
                            settings.setDogName(newName);
                        break;
                    case 2:
                        //update dog age
                        int newAge = askForInt();
                        if(newAge > 0)
                            settings.setAge(newAge);
                        break;
                    case 3:
                        //update dog weight
                        float newWeight = askForFloat();
                        if(newWeight > 0)
                            settings.setWeight(newWeight);
                        break;
                    case 4:
                        if(settings.saveConfig())
                            System.out.println("Settings saved");
                        editing = false;
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

    }


    /**
     * Asks for a number from the user.  Must be positive, and greater
     * than zero
     * @return The number given by the user.  Returns the minimum value integer if
     * nothing was selected
     */
    private static int askForInt(){
        int input = Integer.MIN_VALUE;
        boolean tryAgain = false;


        do {
            //get input from user
            System.out.println("Update the dog's age.  Use whole numbers greater than zero: ");
            try{
                input = consoleReader.nextInt();

                if(input <= 0)
                    throw new IllegalArgumentException("Number less than or equal to zero");
            }
            catch (Exception ex){
                System.out.println("The input should be a whole number.  No special or alpha characters. No Spaces.  Greater than zero.");
                consoleReader.nextLine();
                tryAgain = yesOrNoResponse(TRY_AGAIN_MESSAGE);
            }
            consoleReader.nextLine();


        }while(tryAgain);

        return input;
    }

    /**
     * Asks for a float from the user
     * @return float value given by the user
     */
    private static float askForFloat(){
        float input = -1;
        boolean tryAgain = false;

        do {
            //get input from user
            System.out.println("Update the dog's weight.  Enter a positive number, greater than zero: ");
            try{
                input = consoleReader.nextFloat();

                if(input <= 0)
                    throw new IllegalArgumentException("Number less than or equal to zero");
                consoleReader.nextLine();
            }
            catch (Exception ex){
                System.out.println("The input should be a number.  No special or alpha characters. No Spaces.  Greater than zero.");
                consoleReader.nextLine();
                tryAgain = yesOrNoResponse(TRY_AGAIN_MESSAGE);
            }



        }while(tryAgain);

        return input;
    }

    /**
     * Asks for text response from the user
     */
    private static String askForText(String questionText){
        String input;

            //get input from user
            System.out.println(questionText);

            input = consoleReader.nextLine();

            //Strip leading and trailing whitespaces from entries
            input = input.trim();


        return input;
    }

    /**
     * Presents a question to the user that requires a
     * yes/no answer
     * @param question Text of the question to ask
     * @return true if answered yes, otherwise false.
     */
    private static boolean yesOrNoResponse(String question){
        boolean isYes = false;
        boolean isValidInput = false;
        String input;

        while(!isValidInput){
            System.out.println(question);
            input = consoleReader.nextLine().toLowerCase().trim();

            if(input.equals("y") || input.equals("n")){
                isValidInput = true;
                if(input.equals("y")){
                    isYes = true;
                }
            }
            else{
                System.out.println(INVALID_SELECTION_MESSAGE);
            }
        }
        return isYes;
    }

}

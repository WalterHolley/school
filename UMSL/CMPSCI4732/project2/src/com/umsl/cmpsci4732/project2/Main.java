package com.umsl.cmpsci4732.project2;


public class Main {

    private static Settings settings;

    public static void main(String[] args) {
        init();
        boolean appRunning = true;

        while (appRunning) {


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

    }

    //UX for file reading
    //UX for updating settings

}

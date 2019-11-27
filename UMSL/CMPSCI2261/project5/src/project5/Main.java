package project5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
	private static Scanner consoleReader;
	private static final String INVALID_SELECTION_MESSAGE = "Invalid Selection";
	private static final String ERROR_BLANK_ANSWER = "The entry cannot be blank";
	private static final String ERROR_LESSTHAN_ZERO = "Value must be a number greater than zero";
	private static ArrayList<Animal> animalList;
	private static ArrayList<Plant> plantList;
	private static ArrayList<Fungi> fungiList;
	
	
	public static void main(String[] args) {
		boolean runProgram = true;
		consoleReader = new Scanner(System.in);
		
		do {
			System.out.println("***PROJECT 5***");
			System.out.println("CREATURE INDEX");
			System.out.println("1>>> Add Creature");
			System.out.println("2>>> View Creatures");
			System.out.println("3>>> Sort Creatures");
			System.out.println("4>>> Exit Program");
			
			try {
				int selection = consoleReader.nextInt();
				
				switch(selection) {
				case 1:
					processCreature();
					break;
				case 2:
					doViewCreatures();
					break;		
				case 3:
					doSortCreatures();
					break;
					
				case 4:
					runProgram = false;
					break;
				default:
					throw new InputMismatchException();
					
				}
			}
			catch(InputMismatchException ex) {
				System.out.println(INVALID_SELECTION_MESSAGE);
				consoleReader.nextLine();
			}
			
			
		}while(runProgram);
		
		consoleReader.close();
	}
	
	/**
	 * Begins the creature creation process.
	 */
	private static void processCreature() {
		boolean stillCreating = true;
		
		do {
			System.out.println("Select the creature you wish to create");
			System.out.println("1>>>Animal");
			System.out.println("2>>>Plant");
			System.out.println("3>>>Fungi");
			
			try {
				int selection = consoleReader.nextInt();
				consoleReader.nextLine();
				
				switch(selection) {
				case 1:
				case 2:
				case 3:
					createCreature(selection);
					stillCreating = false;
					break;
				default:
					throw new InputMismatchException();
				}
				
			}
			catch(InputMismatchException ex) {
				System.out.println(INVALID_SELECTION_MESSAGE);
				consoleReader.nextLine();
			}
		}while(stillCreating);
		
	}
	
	/**
	 * Creates the creature selected by the user
	 * @param creatureSelection 1 = Animal, 2 = Plant, 3 = Fungi.  
	 * No other creatures will be saved for any other number.
	 */
	private static void createCreature(int creatureSelection) {
		String genus = getResponseFromUser("What is the genus of this creature?");
		String species = getResponseFromUser("What is the name of this creature?");
		int chromosomes = getIntResponseFromUser("How many chromosomes does this creature have?");
		
		if(creatureSelection == 1) {
			String transport = getResponseFromUser("How does this creature transport itself?");
			String skinType = getResponseFromUser("What kind of skin does this creature have?");
			
			if(animalList == null)
				animalList = new ArrayList<Animal>();
			
			animalList.add(new Animal(genus, species, transport, skinType, chromosomes));
		}
		else if(creatureSelection == 2) {
			int leaves = getIntResponseFromUser("How many leaves does the creature have?");
			String color = getResponseFromUser("What color is this creature?");
			
			if(plantList == null)
				plantList = new ArrayList<Plant>();
			
			plantList.add(new Plant(genus,species,color,chromosomes,leaves));
		}
		else if(creatureSelection == 3) {
			double capSize = getDoubleResponseFromUser("What's the cap size for this creature?");
			double stemHeight = getDoubleResponseFromUser("What's the stem height for this creature?");
			
			if(fungiList == null)
				fungiList = new ArrayList<Fungi>();
			
			fungiList.add(new Fungi(genus, species, stemHeight, capSize, chromosomes));
		}
	}
	
	/**
	 * Processes the user's creature sorting decisions
	 */
	private static void doSortCreatures() {
		boolean optionSelected = false;
		
		do {
			System.out.println("**SORT LISTS**");
			System.out.println("1>>>Animal List");
			System.out.println("2>>>Plant List");
			System.out.println("3>>>Fungi List");
			System.out.println("4>>>All");
			System.out.println("5>>>BACK");
			
			try {
				int selection = getIntResponseFromUser("Select a list to view");
				
				switch(selection) {
				case 1:
				case 2:
				case 3:
				case 4:
					sortList(selection);
					optionSelected = true;
					break;
				case 5:
					optionSelected = true;
					break;
				default:
					throw new InputMismatchException();
					
				
				}
			}
			catch(InputMismatchException ex) {
				System.out.println(INVALID_SELECTION_MESSAGE);
				consoleReader.nextLine();
			}
		}while(!optionSelected);
	}
	
	/**
	 * Processes the user's creature list viewing selection
	 */
	private static void doViewCreatures() {
		boolean optionSelected = false;
		
		do {
			System.out.println("**VIEW LISTS**");
			System.out.println("1>>>Animal List");
			System.out.println("2>>>Plant List");
			System.out.println("3>>>Fungi List");
			System.out.println("4>>>All");
			System.out.println("5>>>BACK");
			
			try {
				int selection = getIntResponseFromUser("Select a list to view");
				
				switch(selection) {
				case 1:
				case 2:
				case 3:
				case 4:
					printList(selection);
					optionSelected = true;
					break;
				case 5:
					optionSelected = true;
					break;
				default:
					throw new InputMismatchException();
					
				
				}
			}
			catch(InputMismatchException ex) {
				System.out.println(INVALID_SELECTION_MESSAGE);
				consoleReader.nextLine();
			}
			
			
		}
		while(!optionSelected);
	}
	
	/**
	 * prints the selected list to the console
	 * @param list 1 = animal list, 2 = plant list, 3 = fungi list
	 * 4 = all lists
	 */
	private static void printList(int list) {
		
		switch(list) {
		case 1:
			printAnimals();
			break;
		case 2:
			printPlants();
			break;
		case 3:
			printFungi();
			break;
		case 4:
			printAnimals();
			printPlants();
			printFungi();
			break;
		default:
			System.out.println(INVALID_SELECTION_MESSAGE);
			
		}
			
	}
	

/**
 * Sorts the selected list
 * @param list 1 = animal list, 2 = plant list, 3 = fungi list
 * 4 = all lists
 */
private static void sortList(int list) {
		
		switch(list) {
		case 1:
			if(animalList != null) {
				Collections.sort(animalList);
				System.out.println("Animals are now sorted by transport method");
			}
			break;
		case 2:
			if(plantList != null) {
				Collections.sort(plantList);
				System.out.println("Plants are now sorted by color");
			}
			break;
		case 3:
			if(fungiList != null) {
				Collections.sort(fungiList);
				System.out.println("Fungi are now sorted by stem height");
			}
			break;
		case 4:
			sortList(1);
			sortList(2);
			sortList(3);
			break;
		default:
			System.out.println(INVALID_SELECTION_MESSAGE);
			
		}
			
	}
	
	/**
	 * prints  animals to console
	 */
	private static void printAnimals() {
		if(animalList != null) {
			for(int i = 0; i < animalList.size(); i++) 
				System.out.println(animalList.get(i).toString());
			
		}
	}
	
	/**
	 * prints plants to console
	 */
	private static void printPlants() {
		if(plantList != null) {
			for(int i = 0; i < plantList.size(); i++)
				System.out.println(plantList.get(i).toString());
		}
	}
	
	/**
	 * prints fungi to console
	 */
	private static void printFungi() {
		if(fungiList != null) {
			for(int i = 0; i < fungiList.size(); i++)
				System.out.println(fungiList.get(i).toString());
		}
	}
	
	
	/**
	 * asks a question from a user and returns a string of their response
	 * @param question the question to ask. 
	 * @return String object of the user's response
	 */
	private static String getResponseFromUser(String question) {
		String answer = "";
		boolean answeringQuestion = true;
		do {
			try {
				System.out.println(question);
				answer = consoleReader.nextLine();
				answer = answer.strip();
				
				if(answer.equals(""))
					throw new NoSuchElementException();
				else
					answeringQuestion = false;
			}
			catch(NoSuchElementException ex) {
				System.out.println(ERROR_BLANK_ANSWER);
				consoleReader.nextLine();
			}			
			
		}while(answeringQuestion);
				
		return answer;
	}
	
	/**
	 * asks a question from a user and returns a string of their response
	 * @param question the question to ask. 
	 * @return String object of the user's response
	 */
	private static int getIntResponseFromUser(String question) {
		Integer answer = 0;
		boolean answeringQuestion = true;
		do {
			try {
				System.out.println(question);
				answer = consoleReader.nextInt();
				consoleReader.nextLine();
				
				if(answer < 0)
					throw new NoSuchElementException();
				else
					answeringQuestion = false;
			}
			catch(InputMismatchException ex) {
				System.out.println(ERROR_LESSTHAN_ZERO);
				consoleReader.nextLine();
			}			
			
		}while(answeringQuestion);
				
		return answer.intValue();
	}
	
	/**
	 * Asks a for a double value response from the user
	 * @param question string of the question
	 * @return double value given by the user
	 */
	private static double getDoubleResponseFromUser(String question) {
		double answer = 0;
		boolean answeringQuestion = true;
		do {
			try {
				System.out.println(question);
				answer = consoleReader.nextDouble();
				consoleReader.nextLine();
				
				if(answer < 0)
					throw new NoSuchElementException();
				else
					answeringQuestion = false;
			}
			catch(InputMismatchException ex) {
				System.out.println(ERROR_LESSTHAN_ZERO);
				consoleReader.nextLine();
			}			
			
		}while(answeringQuestion);
				
		return answer;
	}
	
	


}

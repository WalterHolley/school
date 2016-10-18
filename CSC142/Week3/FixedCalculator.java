//fix2: importing missing dependencies
import java.util.Scanner;



// This program is meant to perform simple mathematical calculations
// However, it has several bugs... (11, more or less)
// Your job is to debug the program so that it works as intended

//fix1: class rename to matched renamed file(issue introduced by rename).
public class FixedCalculator {

	public static void main(String[] args) {
		boolean done = false;
		
		//see fix2
		Scanner console = new Scanner (System.in);
		while (!done){
			//fix10: uncomment displayMenu operation
			displayMenu();
			String selection = getUsersSelection(console);
			done = processSelection(selection, console);
		}
		System.out.println("Thank you for using this program");
	}

	private static boolean processSelection(String selection, Scanner console) {
		boolean done = false;
		//fix12: change selection from 'E' to 'Q'
		if (!selection.equalsIgnoreCase("Q")){
			if (selection.equalsIgnoreCase("U")){
				//fix11: remove result variable.  function does not return value
				caluculateResults(console);
			}
			else if (selection.equalsIgnoreCase("H")){
				//nothing to do here...let the code loop around one more time
				//fix13:  display instructions for application
				displayCalculatorInstructions();
			}
			else {
				System.out.println("Incorrect entry...try again!");
			}
		}
		else {
			done = true;
		}
		return done;
	}

	//NOTE: the method header below is fine -- it contains NO BUG!
	private static void caluculateResults(Scanner console) {
		//change:  moved instructions display to help section
		//displayCalculatorInstructions();
		double operand1 = console.nextDouble();
		char operator = console.next().charAt(0);
		double operand2 = console.nextDouble();
		
		//fix3: type mismatch for result(int can't handle double)
		double result = 0.0;
		boolean isOperatorValid = true;
		if (operator == '+'){
			result = operand1 + operand2;
		}
		else if (operator == '-'){
			//fix4: missing semicolon
			//fix5: incorrect operator
			result = operand1 - operand2;
		}
		else if (operator == '*'){
			result = operand1 * operand2;
		}
		else if (operator == '/'){
			if (operand2 != 0.0){
				//fix6: incorrect operator
				result = operand1 / operand2;
			}
			else {
				result = Double.NaN;
			}
		}
		else if (operator == '^'){
			result = Math.pow(operand1, operand2);
		}
		else {
			isOperatorValid = false;
		}
		if (isOperatorValid){
			//fix7: improper string concatenation
			System.out.println(operand1 +" "+operator+" "+operand2+" = " + result);//would be nice use printf to control the precision of result 
		}
	}

	private static void displayCalculatorInstructions() {
		System.out.println("Enter a mathematical expression to evaluate");
		System.out.println("Valid operations are: +, -, /, *, ^ for power");
		System.out.println("Expression are entered with spaces between the values and operator");
		System.out.println("Here is the valid format:");
		System.out.println("\t<value><space><operator><space><value>");
		System.out.print("Your expression: ");
	}
	

	private static String getUsersSelection(Scanner console) {
		//fix8: accept proper input type(string) from menu
		String selection = console.next();
		return selection;
	}

	private static void displayMenu() {
		System.out.println("Enter one these options:");
		System.out.println("\tH for Help");
		System.out.println("\tU for using calculator");
		System.out.println("\tQ for exiting this program");
		System.out.print("Your selection: ");
	}

}

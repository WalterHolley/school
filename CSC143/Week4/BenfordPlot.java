/**
 * CSC 143 - Assignment 2: Benford's Law
 * @author Walter Holley III
 * Primary entry point for assignment.  default file is set
 * to the name of the file provided for the project; popData.txt.
 * KNOWN ISSUE:
 * When running the application with FILE_NAME set to a file that doesn't exist,
 * the application writes a messag to the java console, but it doesn't inform the
 * user or close the application properly.
 */
import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

public class BenfordPlot extends JFrame {
	private static final String FILE_NAME = "popData.txt";
	
	public BenfordPlot(){
		try{
			init();
		}
		catch(FileNotFoundException ex){
			System.out.printf("%s could not be found.\n", FILE_NAME);
		}
		catch(IOException ex){
			System.out.printf("An error occurred while reading %s.\n", FILE_NAME);
		}
	}
	
	/**
	 * main entry point for program
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				
				BenfordPlot plot = new BenfordPlot();
				plot.setVisible(true);
				
			}
		});

	}
	
	/**
	 * Prepares data for the UI,
	 * and begins the drawing process
	 * @throws IOException if an issue occurs while reading the file.
	 * @throws FileNotFoundException If the specified data file is not found.
	 */
	private void init() throws FileNotFoundException, IOException{
		Benford benfordObject = new Benford(new File(FILE_NAME));
		add(new BenfordDraw(benfordObject));
		setTitle("Assignment 2: Benford Analysis");
		setSize(1024, 768);
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

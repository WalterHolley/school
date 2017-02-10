
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
			
		}
		catch(IOException ex){
			
		}
		
	}
	
	/**
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
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void init() throws FileNotFoundException, IOException{
		add(new BenfordDraw(new Benford(new File(FILE_NAME))));
		setTitle("Assignment 2: Benford Analysis");
		setSize(1024, 768);
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

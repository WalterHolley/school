/**
 * CSC 143 - Assignment 2: Benford's Law
 * @author Walter Holley III
 * Graphics class for BenfordPlot assignment.  Not required,
 * but wanted to make an attempt at implementing graphics for
 * the assignment.
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BenfordDraw extends JPanel implements ActionListener {
	private Timer animationTimer;
	private Benford _plotResults;
	private final int MAX_BAR_LENGTH = 800;
	
	/**
	 * Default Constructor
	 * @param plotResults Benford Object
	 */
	public BenfordDraw(Benford plotResults){
		this._plotResults = plotResults;
		animationTimer = new Timer(1000/60, this);
		animationTimer.setInitialDelay(0);
		animationTimer.start();
	}
	
	
	/**
	 * Draws bars representing the percentage of a
	 * significant number's distribution
	 * @param g Graphics object
	 */
	private void drawPercentageBars(Graphics g){
		double[] benfordResults = _plotResults.benfordPercents();
		for(int i = 0; i < benfordResults.length; i++){
			drawBar(i + 1, benfordResults[i] , g);
		}
		
	}
	
	/**
	 * Draws stationary text to the screen
	 * @param g Graphics object
	 */
	private void drawStaticText(Graphics g){
		drawHeaderText(g);
		drawSectionText(g);
	}
	
	/**
	 * Draws Benford Analysis Title to the Screen
	 * @param g Graphics Object
	 */
	private void drawHeaderText(Graphics g){
		String stringToDraw = "Benford Analysis";
		
		if(_plotResults.isBenfordsLaw()){
			stringToDraw += " - Benford Frequencies";
		}
		else{
			stringToDraw += " - NON-Benford Frequencies";
		}
				
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString(stringToDraw, 200, 24);
	}
	
	/**
	 * Draws the significant number for each row
	 * on the screen
	 * @param g Graphics object
	 */
	private void drawSectionText(Graphics g){
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		for(int i = 1; i <= 9; i++){
			g.drawString(String.valueOf(i), 10, 70 * i);
		}
	}
	
	/**
	 * Draws an individual percentage bar to the screen
	 * @param position the row for the percentage bar
	 * @param percent The percentage to be written on the percentage bar
	 * @param g Graphics object
	 */
	private void drawBar(int position, double percent, Graphics g){
		Graphics2D graphics = (Graphics2D)g;
		double width = (percent / 30.1) * MAX_BAR_LENGTH;
		int textXPosition = (int)width / 2;
		
		//make sure the percentage text doesn't overwrite any static text.
		if(textXPosition < 40){
			textXPosition = 40;
		}
		
		graphics.setColor(Color.YELLOW);
		graphics.fillRect(40, (50 * position) + (20 * (position - 1)), (int)width, 24);
		graphics.setColor(Color.RED);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.drawString(String.format("%.1f%s", percent, "%"), textXPosition, 70 * position);
	}
	
	/**
	 * Draws arrows on the screen indicating where the default Benford
	 * Values for each significant digit lies.
	 * @param g Graphics Object
	 */
	private void drawBenfordPoints(Graphics g){
		double[] benfordPoints = {30.1, 17.6, 12.5, 9.7, 7.9, 6.7, 5.8, 5.1, 4.6};
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.PLAIN, 24));
		for(int i = 0; i < benfordPoints.length; i++){
			double xPosition = benfordPoints[i] / benfordPoints[0] * MAX_BAR_LENGTH + 40;
			g.drawString("^", (int)xPosition, 70 * (i + 1));
		}
		
	}
	
	/**
	 * Override for paintComponent method
	 */
	@Override
	public void paintComponent(Graphics g){
		drawStaticText(g);
		drawPercentageBars(g);
		drawBenfordPoints(g);
	}
	
	
	/**
	 * Override for base actionPerformed method
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

}

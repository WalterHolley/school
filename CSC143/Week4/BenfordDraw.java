/**
 * CSC 143 - Walter Holley III
 * Graphics class for BenfordPlot assignment.  Not required,
 * but wanted to make an attempt at implementing graphics for
 * the assignment
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BenfordDraw extends JPanel implements ActionListener {
	private Timer animationTimer;
	private Benford _plotResults;
	private final int MAX_BAR_LENGTH = 800;
	
	public BenfordDraw(Benford plotResults){
		this._plotResults = plotResults;
		animationTimer = new Timer(1000/60, this);
		animationTimer.setInitialDelay(0);
		animationTimer.start();
	}
	

	private void drawPercentageBars(Graphics g){
		double[] benfordResults = _plotResults.benfordPercents();
		for(int i = 0; i < benfordResults.length; i++){
			drawBar(i + 1, benfordResults[i] , g);
		}
		
	}
	
	private void drawStaticText(Graphics g){
		drawHeaderText(g);
		drawSectionText(g);
	}
	
	private void drawHeaderText(Graphics g){
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString("Benford Analysis", 200, 24);
	}
	
	private void drawSectionText(Graphics g){
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		for(int i = 1; i <= 9; i++){
			g.drawString(String.valueOf(i), 10, 70 * i);
		}
	}
	
	private void drawBar(int position, double percent, Graphics g){
		Graphics2D graphics = (Graphics2D)g;
		double width = (percent / 30.1) * MAX_BAR_LENGTH;
		
		graphics.setColor(Color.YELLOW);
		graphics.fillRect(40, (50 * position) + (20 * (position - 1)), (int)width, 24);
		graphics.setColor(Color.RED);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.drawString(String.format("%.1f%s", percent, "%"), (int)width / 2, 70 * position);
	}
	
	private void drawBenfordPoints(Graphics g){
		double[] benfordPoints = {30.1, 17.6, 12.5, 9.7, 7.9, 6.7, 5.8, 5.1, 4.6};
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.PLAIN, 24));
		for(int i = 0; i < benfordPoints.length; i++){
			double xPosition = benfordPoints[i] / benfordPoints[0] * MAX_BAR_LENGTH - 10;
			g.drawString("^", (int)xPosition, 70 * (i + 1));
		}
		
	}
	
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

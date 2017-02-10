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
		drawBar(1, _plotResults.benfordPercents()[0], g);
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
		graphics.fillRect(30, 50 * position, (int)width, 24);
		graphics.setColor(Color.RED);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.drawString(String.valueOf(percent), (int)width / 2, 70 * position);
	}
	
	@Override
	public void paintComponent(Graphics g){
		drawStaticText(g);
		drawPercentageBars(g);
	}
	
	
	/**
	 * Override for base actionPerformed method
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

}

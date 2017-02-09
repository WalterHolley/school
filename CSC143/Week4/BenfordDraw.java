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
	private double[] _plotResults;
	
	public BenfordDraw(double[] plotResults){
		this._plotResults = plotResults;
		animationTimer = new Timer(1000/60, this);
		animationTimer.setInitialDelay(0);
		animationTimer.start();
	}
	
	public void drawSection(int sectionNUmber, double sectionPercentage){
		
	}
	
	private void drawStaticText(Graphics g){
		drawHeaderText(g);
	}
	
	private void drawHeaderText(Graphics g){
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.drawString("Benford Analysis", 200, 0);
	}
	
	public void drawBar(int x, int y){
		
	}
	
	/**
	 * Override for base actionPerformed method
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}

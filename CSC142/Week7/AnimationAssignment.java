/*CSC 142 - Programming Assignment 3: Animation!
 * Walter Holley III
 *Introduction to programming animation
 *Create some sort of non-trivial animation
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;


import javax.swing.*;


class DrawSurface extends JPanel implements ActionListener{
	Timer animationTimer;
	private Ellipse2D.Double circle = new Ellipse2D.Double();
	private Ellipse2D.Double circle2 = new Ellipse2D.Double();
	private double stringX;
	private double stringY;
	private static final int CIRCLE_SIZE = 20;
	private boolean reverseAnimation = false;
	
	public DrawSurface(){
		//initialize timer and animation position
		circle.x = 0;
		circle.y = 240;
		circle2.x = 0;
		circle2.y = 260;
		stringX = 20;
		stringY = 240;
		animationTimer = new Timer(1000/60, this);
		animationTimer.setInitialDelay(0);
		animationTimer.start();
	}
	
	//draws circle object
	public void drawFace(Graphics g){
		Graphics2D graphics2d = (Graphics2D)g;
		graphics2d.setColor(Color.blue);
		graphics2d.fill(circle);
		
		graphics2d.setColor(Color.green);
		graphics2d.fill(circle2);
		
		graphics2d.draw(circle);
		graphics2d.draw(circle2);
		
		graphics2d.setColor(Color.MAGENTA);
		g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 82));
		g.drawString("D", (int)stringX, (int)stringY);
	}
	
	//moves the circle across the screen
	public void animateFace(double x1, double y1, double x2, double y2){
		if(x1 <= 720){
			x1+= 5;
			y1 = (int)(y1 + (10 * Math.sin(.03 * x1)));
			if(y1 > 240){
				y1 = 240;
			}
		}
		else{
			x1 = 0;
		}
		
		if(x2 <= 720){
			x2+= 5;
			y2 = (int)(y2 + (10 * Math.sin(.03 * x2)));
			if(y2 > 280){
				y2 = 280;
			}
		}
		else{
			x2 = 0;
		}
		
		if(stringX <= 720){
			stringX += 5;
			stringY = (int)(stringY + (10 * Math.sin(.03 * stringX)));
			if(stringY > 280){
				stringY = 280;
			}
		}
		else{
			stringX = 0;
			
		}
		
		setPosition(x1, y1, x2, y2);
	}
	
	//sets the position for the circle
	public void setPosition(double x1, double y1, double x2, double y2){
		circle.setFrame(x1, y1, CIRCLE_SIZE, CIRCLE_SIZE);
		circle2.setFrame(x2, y2, CIRCLE_SIZE, CIRCLE_SIZE);
	}
	
    //Draws circle to the screen
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		animateFace((int)circle.x, (int)circle.y, (int)circle2.x, (int)circle2.y);
		drawFace(g);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		repaint();
	}
}


public class AnimationAssignment extends JFrame {

	public AnimationAssignment(){
		init();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			
			@Override
			public void run(){
				AnimationAssignment assignment = new AnimationAssignment();
				assignment.setVisible(true);
			}
			
		});

	}
	
	//Sets up the window for the Graphics
	private void init(){
		add(new DrawSurface());
		setTitle("Animation Exercise");
		setSize(640, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


}


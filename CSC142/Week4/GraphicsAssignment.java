/*CSC 142 - Walter Holley III
 * Week 4 Practice Exercise - Graphics
 * Draws a triangle using lines, presents a few text
 * captions using the draw functionality within java
 * Draws a few basic shapes colored in
 * 
 * NOTE:  Starting into this assignment, I didn't
 * recognize that the Drawing Panel code was available
 * in the module.  That said, after obtaining the DrawingPanel code,
 * I realized that the Drawing
 * Panel is an abstraction from the actual drawing process
 * in Java itself.  I decided to continue digging into basic graphics
 * in Java.  Below is the result of that research.
 */

import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;

class DrawingSurface extends JPanel{
	
	//draws a triangle using lines
	private void drawTriangle(Graphics g){
		Graphics2D graphics2d = (Graphics2D) g;
		
		graphics2d.drawLine(100, 100, 150, 150);
		graphics2d.drawLine(100, 100, 50, 150);
		graphics2d.drawLine(50, 150, 150, 150);
	}
	
	
	//Created a filled in triangle using the polygon object
	private void drawFilledTriangle(Graphics g){
		int[]x = {250, 300, 200};
		int[]y = {100, 150, 150};
		
		Polygon triangle = new Polygon(x, y, 3);
		g.setColor(Color.ORANGE);
		g.fillPolygon(triangle);
	}
	
	//Draws a circle in the window
	private void drawCircle(Graphics g){
		
		Graphics2D graphics2d = (Graphics2D)g;
		Ellipse2D.Double circle = new Ellipse2D.Double(240, 240, 80, 80);
		graphics2d.setColor(Color.cyan);
		graphics2d.fill(circle);
	}
	
	//Writes text to the screen
	private void drawText(Graphics g){
		g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
	
		g.setColor(Color.black);
		g.drawString("Triangle Made With Lines", 25, 75);
		g.setColor(Color.blue);
		g.drawString("A filled triangle made with a polygon object", 180, 75);
		g.setColor(Color.green);
		g.drawString("A circle drawn with an Ellipses object", 200, 230);
		
	}
	
	//Override base paint function.  Draws graphics to window
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		drawTriangle(g);
		drawFilledTriangle(g);
		drawCircle(g);
		drawText(g);
	}
}

public class GraphicsAssignment extends JFrame {
	
	public GraphicsAssignment(){
		
		init();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		EventQueue.invokeLater(new Runnable(){
			//Override default run and execute graphics class
			@Override
			public void run(){
				GraphicsAssignment ga = new GraphicsAssignment();
				ga.setVisible(true);
			}
		});

	}
	
	//Sets up the window in which the graphics will appear
	private void init(){
		add(new DrawingSurface());
		setTitle("Graphics Exercise");
		setSize(640, 480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}

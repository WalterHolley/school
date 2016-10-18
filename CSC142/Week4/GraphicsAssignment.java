/*CSC 142 - Walter Holley III
 * Week 4 Practice Exercise - Graphics
 * Draws a triangle using lines, presents a few text
 * captions using the draw functionality within java
 * Draws a few basic shapes colored in
 * 
 * NOTE:  Starting into this assignment, I didn't
 * recognize that the Drawing Panel code was available
 * in the module.  That said, I realized that the Drawing
 * Panel is a bit of an abstraction from the actual drawing process
 * in Java itself.  I decided to continue digging into basic graphics
 * in Java.  Below is the result of that research.
 */

import java.awt.*;
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
		
		//change graphics color to orange
		g.setColor(Color.ORANGE);
		
		g.fillPolygon(triangle);
	}
	
	private void drawText(Graphics g){
		g.drawString("Triangle", 50, 50);
	}
	
	//Override base paint function.  Draws graphics to window
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		drawTriangle(g);
		drawFilledTriangle(g);
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

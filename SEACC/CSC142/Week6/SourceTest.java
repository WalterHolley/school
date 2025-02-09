import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.awt.geom.Ellipse2D;


/*
 * This code serves no purpose.  It's
 * just test functions from assignments in the class.
 * 
 */
public class SourceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(8 * (7 + 1) - 4 + 2);
		System.out.println(9 % 6 * 3 % 5 / 2);
		System.out.println(1 + 2 + "3 + 4" + 9 / 2);
		System.out.println(6 * 7 * (3 + 1) % 4);
		
		System.out.println(productOf3(4, 22, -1));
		
		String a = "hello";
		String b = "goodbye";
		String c = "hey";
		
		mystery(b, c, a);
		mystery(a, b, c);
		
		DrawingPanel dp = new DrawingPanel(200, 200);
		drawShapes(dp.getGraphics());
		
		mystery(1);

		mystery(6);

		mystery(19);

		mystery(39);

		mystery(74);
		
		

	}
	
	public static int productOf3(int x, int y, int z){
		return x * y * z;
	}
	
	public static void mystery(String c, String a, String b){
		 b = b + " for now "; // note the spaces!

         c = c + " "+ a; // note the space!

         System.out.print("a:  " + a + " b: " + b + " c: " + c );
	}
	
	public static void drawShapes(Graphics g){
		
		Graphics2D g2d = (Graphics2D)g;
		Ellipse2D.Double circle = new Ellipse2D.Double(80, 80, 40, 40);
		g2d.setColor(Color.CYAN);
		g2d.fill(circle);
		
		Rectangle rect = new Rectangle();
		rect.height = 60;
		rect.width = 60;
		rect.x = 130;
		rect.y = 20;
		g2d.setColor(Color.red);
		g2d.fill(rect);
		
	}
	
	public static void mystery(int x){

	    int y = 1;

	    int z = 0;

	    while (2 * y <= x){

	        y = y * 2;

	        z++;

	    }

	    System.out.println(y + " " + z);

	}
		

}

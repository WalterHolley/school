/*CSC 142 - Week 9 Practice Exercises Self-Check Problem #4
 * Walter Holley III
 *
 *NOTE:
 *The code in the original given state would have thrown an
 *error due to the mis-named variable in the addToXTwice
 *function.  Below is the corrected code.
 * 
 * OUTPUT
 * 14 14
 * 7 9 14 14
 * 18 18
 * 7 9 18 18
 */
class Point{
	public static int x;
	public static int y;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
}

public class ReferenceMystery {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a = 7;
		int b = 9;
		Point p1 = new Point(2,2);
		Point p2 = new Point(2,2);
		addToXTwice(a, p1);
		System.out.println(a + " " + b + " " + p1.x + " " + p2.x);
		addToXTwice(b, p2);
		System.out.println(a + " " + b + " " + p1.x + " " + p2.x);
	}
	
	public static void addToXTwice(int a, Point b){
		a = a + a;
		b.x = a;
		System.out.println(a + " " + b.x);
	}

}

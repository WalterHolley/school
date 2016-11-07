
public class Week6Exercises {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//output:10 20 30 40 50 60 70 80 
		//repeats: 8 times
		while1();
		
		/*output:
		 * count down: 10
		   count down: 9
           count down: 8
           count down: 7
           count down: 6
           count down: 5
           count down: 4
           count down: 3
           count down: 2
           count down: 1
           count down: 0
           count down: -1
           ...
           repeats: infinity
		 */
		//while2();
		
		//no output
		//repeats: zero
		while3();
		
		//no output
		mystery(1);
		
		//4 2
		mystery(6);
		
		//16 4
		mystery(19);
		
		//32 5
		mystery(39);
		
		//64 6
		mystery(74);
	}
	
	
	public static void while1(){
		int x = 10;

		while (x < 90){

		    System.out.print(x + " ");

		    x += 10;

		}
	}
	
	public static void while2(){
		int max = 10;

		while (max <= 10){

		    System.out.println("count down: " + max);

		    max--;

		}
	}
	
	public static void while3(){
		int x = 250;

		while (x % 5 != 0){

		    System.out.println(x);

		}
	}
	
	public static void convertToWhile1(){
		/*original for loop
		 * 
		 * for (int n = 1; n < max; n++){
		   		System.out.println(n);

		   }
		 */
		int n = 1;
		while(n < Integer.MAX_VALUE){
			System.out.println(n);
			n++;
		}
	}
	
	
	public static void convertToWhile2(){
		/* original for loop
		int total = 35;

		for (int number = 1; number <= (total/2); number++){

		    total = total - number;

		    System.out.println(total + " " + number);

		}
		*/
		
		int total = 35;
		int number = 1;
		while(number <= total/2){
			total = total - number;
			System.out.println(total + " " + number);
			number++;
		}
		
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

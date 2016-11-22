
public class Name {
	public static String FirstName;
	public static char MiddleInitial;
	public static String LastName;
	
	public Name(String first, char middle, String last){
		this.FirstName = first;
		this.MiddleInitial = middle;
		this.LastName = last;
	}
	
	
	//Returns the full name given to the object
	//(first, middle Init, last)
	public String getNormalOrder(){
		String result;
		result = this.FirstName + " ";
		
		if(this.MiddleInitial != ' '){
			result += this.MiddleInitial + ".";
		}
		result += this.LastName;
		
		return result;
	}
	
	//returns the name in reverse order
	//(last, first, middle Init)
	public String getReverseOrder(){
		String result;
		result = this.LastName + ", " + this.FirstName;
		
		if(this.MiddleInitial != ' '){
			result += " " + this.MiddleInitial + ".";
		}
		
		return result;
	}
	
	public String toString(){
		return getNormalOrder();
	}
}

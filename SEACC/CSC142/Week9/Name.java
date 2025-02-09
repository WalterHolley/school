/*CSC 142 - Week 9 Practice Exercise
 * Name Class
 * Walter Holley III
 * 
 * Criteria from assignment:
 * 1) public String getNormalOrder(): Returns the person's name in the normal first middle last order: John Q. Public.
 * 2) public String getReverseOrder(): Returns the person's name in reverse order: Public, John Q.
 * 3) Add a toString method for the Name class that returns a String such as 
 *    "John Q. Public" (yes, this is similar to 1) above)
 * 4) Add a constructor to the Name class that accepts a first name,
 *    middle initial, and last name as parameters and initializes the
 *    Name object's state with those value
 * 
 */

//self-check problem #7
//Create class named Name with
//fields for first name, middle initial, and last name
public class Name {
	private String FirstName;
	private char MiddleInitial;
	private String LastName;
	
	//Self-check problem #19  constructor for name object
	public Name(String first, char middle, String last){
		this.FirstName = first;
		this.MiddleInitial = middle;
		this.LastName = last;
	}
	
	//self-check problem #11
	//Returns the full name given to the object
	//(first, middle Initial, last)
	public String getNormalOrder(){
		String result;
		result = this.FirstName + " ";
		
		if(this.MiddleInitial != ' '){
			result += this.MiddleInitial + ". ";
		}
		result += this.LastName;
		
		return result;
	}
	
	//self-check problem #11
	//returns the name in reverse order
	//(last, first, middle Initial)
	public String getReverseOrder(){
		String result;
		result = this.LastName + ", " + this.FirstName;
		
		if(this.MiddleInitial != ' '){
			result += " " + this.MiddleInitial + ".";
		}
		
		return result;
	}
	
	//Self-check problem #15
	//produces a string representation of the Name
	public String toString(){
		return getNormalOrder();
	}
}

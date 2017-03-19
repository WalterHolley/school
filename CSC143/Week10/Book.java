/**
 * CSC 143 Assignment 4: Book Catalog
 * <p>
 * Book Class: Represents a Book inside the Book Catalog
 * @author Walter Holley III
 *
 */
public class Book {
  private String _bookName;
  private String _authorFirstName;
  private String _authorLastName;
  private String _bookCode;
  private int _bookYear;
  private float _bookPrice;
  
  /**
   * Constructor for Class
   * @param bookName name of the book
   * @param ISBN  International Standard Book Number for the book
   * @param price Price of the book
   * @throws NumberFormatException if the ISBN is invalid, or the Price is equal to or less than zero
   */
  public Book(String bookName, String ISBN, float price )throws NumberFormatException{
	 if(!isValidISBN(ISBN)){
		 throw new NumberFormatException("Invalid ISBN");
	 }
	 else if(price < 0.00f){
		 throw new NumberFormatException("Invalid Price");
	 }
	 else{
		 _bookCode = ISBN.toUpperCase();
		 _bookPrice = price;
		 _bookName = bookName;
	 }
	 
  }
  
  /**
   * Sets the first name of the author of the book
   * @param firstName the first name of the author
   */
  public void setAuthorFirstName(String firstName){
	  _authorFirstName = firstName;
  }
  
  /**
   * Sets the last name of the book's author
   * @param lastName the name name of the book's author
   */
  public void setAuthorLastName(String lastName){
	  _authorLastName = lastName;
  }
  
  /**
   * Sets the published year of the book
   * @param year the year the book was published
   */
  public void setBookYear(int year){
	  _bookYear = year;
  }
  
  /**
   * Retrieves the first name of the author
   * @return String representation of the author's first name
   */
  public String getAuthorFirstName(){
	  return _authorFirstName;
  }
  
  /**
   * Retrieves the last name of the author
   * @return String representation of the author's last name
   */
  public String getAuthorLastName(){
	  return _authorLastName;
  }
  
  /**
   * Retrieves the name of the book
   * @return String representation of the book's name
   */
  public String getBookName(){
	  return _bookName;
  }
  
  /**
   * Retrieves the book's published year
   * @return integer of the book's publishing year
   */
  public int getBookYear(){
	  return _bookYear;
  }
  
  /**
   * Retrieves the price of the book
   * @return float representation of the book's price
   */
  public float getPrice(){
	  return _bookPrice;
  }
  
  /**
   * Retrieves the ISBN of the book
   * @return String Representation of the book's ISBN
   */
  public String getISBN(){
	  return _bookCode;
  }
  
  public String getAuthorFullName(){
	  return this._authorFirstName + " " + this._authorLastName;
  }
  
  public String toString(){
	  String bookString = String.format("%s\t%s\t%s\t%s\t%s\t%.2f", this._bookCode, 
			  this.getAuthorLastName(), this.getAuthorFirstName(), this._bookName, this._bookYear, this._bookPrice);
	  
	  return bookString;
  }

  /**
   * Determines if the given ISBN is valid
   * @param isbn the ISBN to validate
   * @return true if the number is valid, otherwise false
   */
   public static boolean isValidISBN(String isbn){
	   boolean isValid = false;
	   int dashDigit = (int)'-';
	
	   if(isbn.length() > 9 && isbn.length() < 14){
		   Integer checkSum = null;
		   int dashCount = 0;
		   int digitTotal = 0;
		   int digitCount = 0;
		   for(int i = 0; i < isbn.length(); i++){
			   int value = 0;
			   char isbnChar = isbn.toCharArray()[i];
			   //get checksum value at the end of the isbn string
			   if(i == isbn.length() -1){
				   if(isbnChar == 'X'){
					   checkSum = 10;
				   }
				   else if((int)isbnChar > 47 && (int)isbnChar < 58){
					   checkSum = Integer.parseInt(String.valueOf(isbn.charAt(i)));
				   }
				   else{
					   break;
				   }
			   }
			   else{
				   if((int)isbnChar > 47 && (int)isbnChar < 58){
					   value = Integer.parseInt(String.valueOf(isbn.charAt(i)));
				   }
				   else if((int)isbnChar == dashDigit){
					   value = dashDigit;
				   }
				   else{
					   break;
				   }
			   }
			   
			   if(value == dashDigit){
				   if(i == 0){
					   break;
				   }
				   else if((int)isbn.charAt(i - 1) == dashDigit){
					   break;
				   }
				   dashCount++;
				   if(dashCount > 3){
					   break;
				   }
			   }
			   else{
				   digitCount++;
				   if(digitCount < 10){
					   digitTotal += value * digitCount;
				   }
				   else{
					   break;
				   }	
			   }
		   }		
		   if(digitCount == 10){
			   int determinedChecksum = digitTotal % 11;
			   if(determinedChecksum == checkSum){
				   isValid = true;
			   }
		   }
	   }
	   return isValid;
   }
}


public class Book {
  private String _bookName;
  private String _authorFirstName;
  private String _authorLastName;
  private String _bookCode;
  private int _bookYear;
  private float _bookPrice;
  
  public Book(String bookName, String ISBN, float price ){
	  //TODO validate ISBN
	  _bookCode = ISBN;
	  _bookPrice = price;
	  _bookName = bookName;
  }
  
  public void setAuthorFirstName(String firstName){
	  _authorFirstName = firstName;
  }
  
  public void setAuthorLastName(String lastName){
	  _authorLastName = lastName;
  }
  
  public void setBookYear(int year){
	  _bookYear = year;
  }
  
  public String getAuthorFirstName(){
	  return _authorFirstName;
  }
  
  public String getAuthorLastName(){
	  return _authorLastName;
  }
  
  public String getBookName(){
	  return _bookName;
  }
  
  public int getBookYear(){
	  return _bookYear;
  }
  
  public float getPrice(){
	  return _bookPrice;
  }
  
  public String getISBN(){
	  return _bookCode;
  }
  
  public String getAuthorFullName(){
	  return this._authorFirstName + " " + this._authorLastName;
  }
  
  public String toString(){
	  String bookString = String.format("TITLE: %s\nAUTHOR: %s\nPUBLISHED: %s\nISBN: %s\nPRICE: %f\n",
			  this._bookName, this.getAuthorFullName(), this._bookYear, this._bookCode, this._bookPrice);
	  
	  return bookString;
  }
}

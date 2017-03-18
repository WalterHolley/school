
public class BookCatalogNode {
	private Book _data;
	private BookCatalogNode _nextNode;
	private BookCatalogNode _previousNode;
	public BookCatalogNode(){
		this(null, null, null);
	}
	public BookCatalogNode(BookCatalogNode prevNode,  Book book, BookCatalogNode nextNode){
		this._data = book;
		this._nextNode = nextNode;
		this._previousNode = prevNode;
	}
	
	public BookCatalogNode(Book book){
		this(null, book, null);
	}
	
	public BookCatalogNode(Book book, BookCatalogNode prevNode){
		this(prevNode, book, null);
	}
	
	
	public BookCatalogNode getNext(){
		return this._nextNode;
	}
	
	public BookCatalogNode getPrevious(){
		return this._previousNode;
	}
	
	public void setNext(BookCatalogNode node){
		this._nextNode = node;
	}
	
	public void setPrevious(BookCatalogNode node){
		this._previousNode = node;
	}
	
	public Book getBook(){
		return this._data;
	}
	
	public void setBook(Book book){
		this._data = book;
	}
}

/**
 * CSC 143 Assignment 4: Book Catalog
 * <p>
 * BookCatalogNode Class: Represents a node within the book catalog.  
 * Manages data and node traversal
 * @author Walter Holley III
 *
 */
public class BookCatalogNode {
	private Book _data;
	private BookCatalogNode _nextNode;
	private BookCatalogNode _previousNode;
	
	/**
	 * Default Constructor
	 */
	public BookCatalogNode(){
		this(null, null, null);
	}
	
	/**
	 * Constructor
	 * @param prevNode The node before this node
	 * @param book the book data to be contained within this node
	 * @param nextNode the next node in the linked list
	 */
	public BookCatalogNode(BookCatalogNode prevNode,  Book book, BookCatalogNode nextNode){
		this._data = book;
		this._nextNode = nextNode;
		this._previousNode = prevNode;
	}
	
	/**
	 * Constructor
	 * @param book the book data for this node
	 */
	public BookCatalogNode(Book book){
		this(null, book, null);
	}
	
	/**
	 * Constructor
	 * @param book the book data for this node
	 * @param prevNode the node in the position before this node
	 */
	public BookCatalogNode(Book book, BookCatalogNode prevNode){
		this(prevNode, book, null);
	}
	
	/**
	 * Retrieves the next node in the list
	 * @return BookCatalogNode object of the next node, or null if there's no object
	 */
	public BookCatalogNode getNext(){
		return this._nextNode;
	}
	
	/**
	 * Retrieves the previous node in the list
	 * @return BookCatalogNode object of the previous node, or null if there's no object
	 */
	public BookCatalogNode getPrevious(){
		return this._previousNode;
	}
	
	/**
	 * Sets the next node in the list
	 * @param node the node that will come after this node
	 */
	public void setNext(BookCatalogNode node){
		this._nextNode = node;
	}
	
	/**
	 * Sets the previous node in the list
	 * @param node the node that will come before this node
	 */
	public void setPrevious(BookCatalogNode node){
		this._previousNode = node;
	}
	
	/**
	 * Retrieves the book data within this node
	 * @return Book object containing book data, otherwise null
	 */
	public Book getBook(){
		return this._data;
	}
	
	/**
	 * Sets the book data within this node
	 * @param book the book object to be set
	 */
	public void setBook(Book book){
		this._data = book;
	}
}

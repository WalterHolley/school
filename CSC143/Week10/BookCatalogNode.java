
public class BookCatalogNode {
	private Book _data;
	private BookCatalogNode _nextNode;
	
	public BookCatalogNode(Book book, BookCatalogNode nextNode){
		this._data = book;
		this._nextNode = nextNode;
	}
	
	public BookCatalogNode getNext(){
		return this._nextNode;
	}
	
	public void setNext(BookCatalogNode node){
		this._nextNode = node;
	}
	
	public Book getBook(){
		return this._data;
	}
}

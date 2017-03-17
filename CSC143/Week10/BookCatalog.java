
public class BookCatalog extends BookCatalogNode {
		private BookCatalogNode _head;
		private BookCatalogNode _tail;
	public BookCatalog(){
		super();
		this._head = null;
		this._tail = null;
		
	}
	public BookCatalogNode next(){
		return this.getNext();
	}
	public void add(Book book){
		BookCatalogNode node = new BookCatalogNode(book, null);
		
		if(this._head == null){
			this._head = node;
		}
		else if(_tail == null){
			this._tail = node;
			this._head.setNext(_tail);
		}
		else{
			this._tail.setNext(node);
			this._tail = node;
		}
	}
	
	public void remove(Book book){
		BookCatalogNode current = _head;
		BookCatalogNode previous = null;
		
		while(!current.getBook().equals(book)){
			previous = current;
			current = current.getNext();
		}
		
		if(current.getBook().equals(book)){
			previous.setNext(current.getNext());
		}
		
	}
	
	public int size(){
		int size = 0;
		BookCatalogNode nodeCopy = _head;
		
		while(nodeCopy != null){
			size++;
			nodeCopy = nodeCopy.getNext();
		}
		return size;
	}
}

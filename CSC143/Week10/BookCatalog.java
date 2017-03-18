import java.util.ArrayList;

public class BookCatalog {
		private BookCatalogNode _head;
		private BookCatalogNode _tail;
		private int _size;
	public BookCatalog(){
		this._head = null;
		this._tail = null;
		this._size = 0;
		
	}
	
	

	
	public BookCatalogNode getCatalog(){
		return this._head;
	}
	
	/**
	 * Adds a book to the Book catalog
	 * @param book the book object to add
	 */
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
		this._size++;
	}
	
	public void remove(int element){
		if(this._size != 0 && element > 0 && element <= this._size){
			int middle = this._size / 2;
			int position = 0;
			BookCatalogNode node = null;
			if(middle <= element){
				position = this._size;
				node = this._tail;
			}
			else{
				node = this._head;
			}
			
			while(position != element){
				if(position < element){
					position++;
					node = node.getNext();
					if(position == element){
						BookCatalogNode temp = node.getNext();
						if(node.getPrevious() != null){
							node.getPrevious().setNext(temp);
						}
						else{
							node = node.getNext();
						}
						this._size--;
					}
				}
				else if(position > element){
					position--;
					node = node.getPrevious();
					if(position == element){
						BookCatalogNode temp = node.getPrevious();
						if(node.getNext() != null){
							node.getNext().setPrevious(temp);
						}
						else{
							node = node.getPrevious();
						}
						this._size--;
					}
				}
				
			}
		}
		
	}
	
	public int size(){
		return this._size;
	}
	
	public ArrayList<Book> searchByAuthorLastName(String searchCriteria){
		return search(searchCriteria, 1);
	}
	
	public ArrayList<Book> searchByAuthorFirstName(String searchCriteria){
		return search(searchCriteria, 2);
	}
	
	public ArrayList<Book> searchByISBN(String searchCriteria){
		return search(searchCriteria, 3);
	}
	
	private ArrayList<Book> search(String input, int method){
		ArrayList<Book> result = new ArrayList<Book>();
		BookCatalogNode bookNode = this._head;
		while(bookNode != null){
			Book book = bookNode.getBook();
			switch(method){
				case 1:
					if(book.getAuthorLastName().toLowerCase().equals(input.toLowerCase())){
						result.add(book);
					}
					break;
				case 2:
					if(book.getAuthorFirstName().toLowerCase().equals(input.toLowerCase())){
						result.add(book);
					}
					break;
				case 3:
					if(book.getISBN().equals(input)){
						result.add(book);
					}
					break;
			}
			bookNode = bookNode.getNext();
		}
		return result;
	}
	

}

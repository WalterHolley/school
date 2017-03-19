/**
 * CSC 143 Assignment 4:  Book Catalog
 * <p>
 * BookCatalog class: Represents a catalog for managing books
 * Responsible for adding, removing, and searching for
 * books within a linked list of BookCatalogNode objects.
 * @author Walter Holley III
 *
 */
public class BookCatalog {
		private BookCatalogNode _head;
		private BookCatalogNode _tail;
		private int _size;
	public BookCatalog(){
		this._head = null;
		this._tail = null;
		this._size = 0;
		
	}
	
	/**
	 * Gets the book catalog node collection
	 * @return the first element in the book catalog
	 */
	public BookCatalogNode getCatalog(){
		return this._head;
	}
	
	/**
	 * Retrieves a book based on the catalog element given
	 * @param element the number of the element 
	 * to return
	 * @return Book object tied to the element number,
	 * otherwise null
	 */
	public Book getBook(int element){
		return getNodeByElement(element).getBook();
	}
	
	/**
	 * Adds a book to the Book catalog
	 * @param book the book object to add
	 * @throws IllegalArgumentException if the ISBN of the book being added
	 * already exists within the catalog
	 */
	public void add(Book book)throws IllegalArgumentException{
		BookCatalogNode node = new BookCatalogNode(book, null);
		if(search(book.getISBN(), 3).size() > 0){
			throw new IllegalArgumentException("The ISBN for this book already exists in the Catalog");
		}
		else if(this._head == null){
			this._head = node;
		}
		else if(_tail == null){
			this._tail = node;
			this._tail.setPrevious(_head);
			this._head.setNext(_tail);
		}
		else{
			this._tail.setNext(node);
			node.setPrevious(this._tail);
			this._tail = node;
		}
		this._size++;
	}
	
	/**
	 * Removes a book from the Catalog based on its position within the catalog
	 * @param element the number position of the item to be removed
	 * @throws IndexOutOfBoundsException if element doesn't fall within
	 * the bounds of the list(less than 1, or greater than the size of the list).
	 */
	public void remove(int element)throws IndexOutOfBoundsException{
		if(this._size != 0 && element > 0 && element <= this._size){
			BookCatalogNode node = getNodeByElement(element);
			
			if(node.getNext() != null){
				if(node.getPrevious() != null){
					BookCatalogNode newNode = node.getPrevious();
					newNode.setNext(node.getNext());
					newNode.getNext().setPrevious(newNode);
				}
				else{
					this._head = node.getNext();
					this._head.setPrevious(null);

				}
				this._size--;
			}
			else{
				if(node.getPrevious() != null){
					this._tail = node.getPrevious();
					this._tail.setNext(null);
				}
				else{
					this._head = null;
				}
				this._size--;
			}
		}
		else{
			throw new IndexOutOfBoundsException("The element is outside the bounds of the catalog");
		}
	}
	
	/**
	 * gets size of book catalog
	 * @return size of book catalog
	 */
	public int size(){
		return this._size;
	}
	
	/**
	 * Searches for books based on the last name of the author
	 * @param searchCriteria search parameter provided by user
	 * @return List of book objects found from the search
	 */
	public BookCatalog searchByAuthorLastName(String searchCriteria){
		return search(searchCriteria, 1);
	}
	
	/**
	 * Searches for books based on the first name of the author
	 * @param searchCriteria search parameter provided by user
	 * @return List of book objects found from the search
	 */
	public BookCatalog searchByAuthorFirstName(String searchCriteria){
		return search(searchCriteria, 2);
	}
	
	/**
	 * Searches for books based on the ISBN# of the book
	 * @param searchCriteria search parameter provided by user
	 * @return List of book objects found from the search
	 */
	public BookCatalog searchByISBN(String searchCriteria){
		return search(searchCriteria, 3);
	}
	
	/**
	 * Searches for books based on criteria provided by the user
	 * @param searchCriteria search parameter provided by user
	 * @param method of search
	 * <p>
	 * 1 = last name, 2 = first name, 3 = ISBN
	 * @return List of book objects found from the search
	 */
	private BookCatalog search(String input, int method){
		BookCatalog result = new BookCatalog();
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
	
	/**
	 * Gets a node from the list based on its
	 * element position
	 * @param element the number position of the
	 * element to retrieve
	 * @return BookCatalogNode object from the element
	 * position
	 */
	private BookCatalogNode getNodeByElement(int element){
		BookCatalogNode node = null;
		if(this._size != 0 && element > 0 && element <= this._size){
			int middle = this._size / 2;
			int position = 1;
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
				}
				else if(position > element){
					position--;
					node = node.getPrevious();	
				}
			}
		}
		return node;
	}
	

}

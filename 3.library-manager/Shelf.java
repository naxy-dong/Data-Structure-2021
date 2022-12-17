/**
 * Shelf class contains books in a particular shelf of BookRepository
 */
public class Shelf {
    private Book headBook;
    private Book tailBook;
    private int length;
    private SortCriteria sortCriteria;

    //default constructor
    public Shelf(){
        headBook = null;
        tailBook = null;
        length = 0;
        sortCriteria = SortCriteria.ISBN;
    }

    /**
     * Adds a book to the shelf while maintaining that shelf’s current sorting criteria.
     * @param addedBook book to be added
     * @throws BookAlreadyExistsException: thrown if a book with the same ISBN already exists.
     */
    public void addBook(Book addedBook) throws BookAlreadyExistsException{
        if(BookRepository.getBookFromISBN(addedBook.getISBN()) != null){
            throw new BookAlreadyExistsException();
        }
        //if there is no book
        if(headBook == null){
            headBook = addedBook;
            tailBook = addedBook;
        }
        //if there is one book
        else if(headBook.getNextBook() == null){
            addedBook.setNextBook(headBook);
            tailBook = headBook;
            headBook = addedBook;
        }
        else{
            addedBook.setNextBook(headBook);
            headBook = addedBook;
        }
        sort(this.sortCriteria);
        length++;
    }

    /**
     * Removes the book with the name bookName from the shelf.
     * @param removedISBN name of book to remove.
     * @throws BookDoesNotExistException: A book with the same ISBN does not exist.
     * @throws InvalidISBNException: the ISBN is not valid
     */
    public void removeBook(String removedISBN) throws BookDoesNotExistException, InvalidISBNException{
        long ISBN = Utility.convertStringToISBN(removedISBN);
        //if nothing is in the shelf
        if(headBook == null){
            throw new BookDoesNotExistException();
        }
        Book removedBook = null;
        if(headBook.getISBN() == ISBN){
            removedBook = headBook;
            headBook = headBook.getNextBook();
            if(headBook == null){
                tailBook = null;
            }
        }
        else{
            Book cursor = headBook;
            while(removedBook == null && cursor.getNextBook() != null){
                if(cursor.getNextBook().getISBN() == ISBN){
                    removedBook = cursor.getNextBook();
                    cursor.setNextBook(cursor.getNextBook().getNextBook());
                    if(cursor.getNextBook() == null){
                        tailBook = cursor;
                    }
                    length--;
                }
                cursor = cursor.getNextBook();
            }
        }
        if(removedBook != null){
            System.out.println(removedBook.getName() + " has been removed from the list");
        }
        else{
            throw new BookDoesNotExistException();
        }
    }

    /**
     * gets the book from a specific ISBN
     * @param ISBN ISBN of the book
     * @return the book object associated with the ISBN, and null if the shelf can't find the book
     */
    public Book getBook(long ISBN){
        for(Book book = headBook; book != null; book = book.getNextBook()){
            if(ISBN == book.getISBN()){
                return book;
            }
        }
        return null;
    }

    /**
     * Sorts books on shelf in lexicographical/numeric order given 
     * some sorting criteria in ascending order using BUBBLE SORT
     * @param sortCriteria it sorts the shelf based on the ISBN
     */
    public void sort(SortCriteria sortCriteria){
        this.sortCriteria = sortCriteria; 
        for(Book i = headBook; i != tailBook; i = i.getNextBook()){
            for(Book j = headBook.getNextBook(); j != null; j = j.getNextBook()){
                switch(sortCriteria){
                    case ISBN:
                        if(i.getISBN() > j.getISBN()){
                            swap(i,j);
                        }
                        break;
                    case Name:
                        if(i.getName().compareTo(j.getName()) > 0){
                            swap(i,j);
                        }
                        break;
                    case Author:
                        if(i.getAuthor().compareTo(j.getAuthor()) > 0){
                            swap(i,j);
                        }
                        break;
                    case Genre:
                        if(i.getGenre().compareTo(j.getGenre()) > 0){
                            swap(i,j);
                        }
                        break;
                    case Year:
                        if(i.getYearPublished() > j.getYearPublished()){
                            swap(i,j);
                        }
                        break;
                    case Condition:
                        if(Utility.convertConditionToIndex(i.getCondition()) > Utility.convertConditionToIndex(j.getCondition())){
                            swap(i,j);
                        }
                        break;
                }
            }
        }
    }

    /**
     * It swaps the data of the two book objects without changing their reference(nextbook)
     * @param book1 
     * @param book2
     */
    public void swap(Book book1, Book book2){
        String tempName = book1.getName();
        String tempAuthor = book1.getAuthor();
        String tempGenre = book1.getGenre();
        Condition tempCondition = book1.getCondition();
        int tempYearPublished = book1.getYearPublished();
        long tempISBN = book1.getISBN();
        long tempCheckoutUserID = book1.getCheckoutUserID();
        Date tempDueDate = book1.getDueDate();
        boolean tempCheckedOut = book1.getCheckedOut();

        book1.setName( book2.getName()); 
        book1.setAuthor( book2.getAuthor()); 
        book1.setGenre( book2.getGenre()); 
        book1.setCondition( book2.getCondition()); 
        book1.setYearPublished( book2.getYearPublished()); 
        book1.setISBN( book2.getISBN()); 
        book1.setCheckoutUserID( book2.getCheckoutUserID()); 
        book1.setDueDate( book2.getDueDate()); 
        book1.setCheckedOut( book2.getCheckedOut()); 

        book2.setName( tempName); 
        book2.setAuthor( tempAuthor); 
        book2.setGenre( tempGenre); 
        book2.setCondition( tempCondition); 
        book2.setYearPublished( tempYearPublished); 
        book2.setISBN( tempISBN); 
        book2.setCheckoutUserID( tempCheckoutUserID); 
        book2.setDueDate( tempDueDate); 
        book2.setCheckedOut( tempCheckedOut); 
    }

    /**
     * This method checks if the book is empty or not
     * 
     * @return true if book is empty
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * 
     */
    public String toString(){
        String format = "|%1$-15s|%2$-15s|%3$-15s|%4$-15s|\n";
        //First Column String = condition
        String str = String.format(format, sortCriteria, "Checked Out", "Due Date", "Checkout UserID") + 
                                "=================================================================\n";
        for(Book book = headBook; book != null; book = book.getNextBook()){
            str += book.toString(sortCriteria);
        }
        return str;
    }
}
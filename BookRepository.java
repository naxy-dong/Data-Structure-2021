/**
 * BookRepository contains 10 endless shelves and is able to add, remove, check in, and check out Books.
 * @author Yu Xiang Dong
 */
public class BookRepository {
    private static Shelf[] shelves;

    //constructor
    public BookRepository(){
        shelves = new Shelf[10];
        for(int i = 0; i < shelves.length; i++){
            shelves[i] = new Shelf();
        }
    }
    /**
     * Checks in book with the ISBN checkedInISBN from BookRepository.
     * @param checkInISBN The ISBN of the book to be checked in
     */
    public static void checkInBook(long checkInISBN) throws BookDoesNotExistException{
        Book returnedBook = getBookFromISBN(checkInISBN);
        if(returnedBook == null){
            throw new BookDoesNotExistException();
        }
        returnedBook.setCheckedOut(false);
        returnedBook.setDueDate(null);
        returnedBook.setCheckoutUserID(-1);
    }
    /**
     * Checks out book with the ISBN checkedOutISBN from BookRepository
     * @param checkedOutISBN – The ISBN of the book to be checked out
     * @param checkedOutUserID  – The ID number of the person checking out this book
     * @param dueDate – The date the book must be returned by
     * @exception InvalidISBNException: Thrown if checked out book’s ISBN is invalid.
     * @exception InvalidUserIDException: Thrown if the person’s userID is invalid.
     * @exception BookAlreadyCheckedOutException: Thrown if a book has already been checked out.
     */
    public void checkOutBook(long checkedOutISBN, long checkedOutUserID, Date dueDate){
        try{
            Book checkedOutBook = getBookFromISBN(checkedOutISBN);
            if(checkedOutBook == null){
                throw new BookDoesNotExistException();
            }
            if(checkedOutBook.getCheckedOut()){
                throw new BookAlreadyCheckedOutException();
            }
            //if the dueDate is before the year published, which is impossible. 
            if(!Date.checkValidity(dueDate) || Date.compare(dueDate, new Date(1,1,checkedOutBook.getYearPublished())) < 0){
                throw new InvalidDateException();
            }
            checkedOutBook.setCheckedOut(true);
            checkedOutBook.setDueDate(dueDate);
            checkedOutBook.setCheckoutUserID(checkedOutUserID);
            System.out.println(checkedOutBook.getName() + " has been checked out by " 
                            + Utility.convertUserIDToString(checkedOutUserID) + " and must be returned by " + dueDate + "!");
        }
        catch(BookDoesNotExistException e){
            System.out.println("The book with the ISBN requested does not exist");
        }
        catch(BookAlreadyCheckedOutException e){
            System.out.println("The book is already checked out");
        }
        catch(InvalidDateException e){
            System.out.println("The date format entered is invalid or the date is before the published year");
        }
    }
    /**
     * 
     * @param ISBN
     * @param addName
     * @param addAuthor
     * @param addGenre
     * @param publishingYear
     * @param addCondition
     * @exception BookAlreadyExistsException: thrown if a book of the same ISBN already exists
     */
    public void addBook(long ISBN, String addName, String addAuthor, String addGenre, int publishingYear, Condition addCondition){
        try{
            if(getBookFromISBN(ISBN) != null){
                throw new BookAlreadyExistsException();
            }
            Book book = new Book(ISBN, addName, addAuthor, addGenre, publishingYear, addCondition);
            int firstDigit = Utility.getFirstDigitFromISBN(ISBN);    

            //the first digit is the index of the shelf
            shelves[firstDigit].addBook(book);
            System.out.println(addName + " has been successfully added to the book repository\n");
        }
        catch(BookAlreadyExistsException e){
            System.out.println("The book with the same ISBN already exists");
        }
    }
    /**
     * Removes a book from the book repository
     * @param ISBN
     * @throws InvalidISBNException
     * @exception BookDoesNotExistException
     */
    public void removeBook(long ISBN) throws InvalidISBNException{
        try{
            int firstDigit = Utility.getFirstDigitFromISBN(ISBN); 
            shelves[firstDigit].removeBook(Utility.convertISBNToString(ISBN));
        }
        catch(BookDoesNotExistException e){
            System.out.println("No book with the specified ISBN is found in THIS shelf");
        }
    }
    /**
     * Sorts a specific shelf by some criteria.
     * @param shelfIdx index of shelf to be sorted
     * @param sortCriteria criteria to sort shelf by
     * @throws InvalidSortCriteriaException Thrown if sortCriteria has no corresponding enum
     */
    public void sortShelf(int shelfIdx, String sortCriteria) throws InvalidSortCriteriaException{
        SortCriteria criteria;
        criteria = SortCriteria.ISBN;

        switch(sortCriteria){
            case "I":
                criteria = SortCriteria.ISBN;
                shelves[shelfIdx].sort(criteria);
                break;
            case "N":
                criteria = SortCriteria.Name;
                shelves[shelfIdx].sort(criteria);
                break;
            case "A":
                criteria = SortCriteria.Author;
                shelves[shelfIdx].sort(criteria);
                break;
            case "G":
                criteria = SortCriteria.Genre;
                shelves[shelfIdx].sort(criteria);
                break;
            case "Y":
                criteria = SortCriteria.Year;
                shelves[shelfIdx].sort(criteria);
                break;
            case "C":
                criteria = SortCriteria.Condition;
                shelves[shelfIdx].sort(criteria);
                break;
            case "Q":
                System.out.print("Good bye, have a nice day");
                break;
            default:
                throw new InvalidSortCriteriaException();
        }
        System.out.println("Shelf " + shelfIdx + " has been sorted by " + criteria);
    }
    /**
     * it gets the book from the requested ISBN 
     * @param ISBN
     * @return the book if the ISBN exists and null if it does not exist
     */
    public static Book getBookFromISBN(long ISBN){
        for(int i = 0; i < shelves.length; i++){
            if(shelves[i].getBook(ISBN) != null){
                return shelves[i].getBook(ISBN);
            }
        }
        return null;
    }
    /**
     * Prints the books in the shelf in a tabular format
     * @param shelfNumber the index of shelves
     */
    public void printBooksInShelf(int shelfNumber){
        System.out.print(shelves[shelfNumber]);
    }
}

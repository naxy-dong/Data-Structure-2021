/**
 * The book class contains variables for the book's name, author, year of publication,
 * genre, condition, and ISBN Number. In addition, it should also contain variables on whether 
 * it has been checked out, who checked it out, and its return date.
 * @author Yu Xiang Dong
 */

public class Book {
    //information that book contains
    private String name;
    private String author;
    private String genre;
    private Condition condition;
    private long ISBN;
    private long checkoutUserID;
    private int yearPublished;
    private Date dueDate;
    private boolean checkedOut;
    private Book nextBook;

    //default constructor
    public Book(){
        name = "sampleName";
        author = "sampleAuthor";
        genre = "sampleGenre";
        yearPublished = -1;
        condition = Condition.New;
        ISBN = -1;

        checkoutUserID = -1;
        dueDate = new Date();
        checkedOut = false;

        nextBook = null;
    }

    //constructor with parameters
    public Book(long ISBN, String name, String author, String genre, int yearPublished, Condition condition){
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.condition = condition;
        this.ISBN = ISBN;
        this.yearPublished = yearPublished;

        dueDate = null;
        this.checkoutUserID = -1;
        checkedOut = false;

        nextBook = null;
    }

    //Getters(For all variables)
    public String getName(){
        return name;
    }
    public String getAuthor(){
        return author;
    }
    public String getGenre(){
        return genre;
    }
    public Condition getCondition(){
        return condition;
    }
    public int getYearPublished(){
        return yearPublished;
    }
    public long getISBN(){
        return ISBN;
    }
    public long getCheckoutUserID(){
        return checkoutUserID;
    }
    public Book getNextBook(){
        return nextBook;
    }
    public Date getDueDate(){
        return dueDate;
    }
    public boolean getCheckedOut(){
        return checkedOut;
    }

    //Setters (only the program needs)\
    public void setName(String name){
        this.name = name;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public void setGenre(String genre){
        this.genre = genre;
    }
    public void setCondition(Condition condition){
        this.condition = condition;
    }
    public void setYearPublished(int yearPublished){
        this.yearPublished = yearPublished;
    }
    public void setISBN(long ISBN){
        this.ISBN = ISBN;
    }
    public void setCheckoutUserID(long checkoutUserID){
        this.checkoutUserID = checkoutUserID;
    }
    public void setNextBook(Book book){
        this.nextBook = book;
    }
    public void setDueDate(Date dueDate){
        this.dueDate = dueDate;
    }
    public void setCheckedOut(boolean checkedOut){
        this.checkedOut = checkedOut;
    }

    
    /**
     * @param sortCriteria the sorting criteria
     * @return a string that formats the information of the book in a tabular form
     */
    public String toString(SortCriteria sortCriteria){
        //first column is what we are sorting the list on
        String format = "|%1$-15s|%2$-15s|%3$-15s|%4$-15s|\n";
        String firstCol = "";
        switch(sortCriteria){
            case ISBN:
                firstCol = Utility.convertISBNToString(ISBN);
                break;
            case Name:
                firstCol = name;
                break;
            case Author:
                firstCol = author;
                break;
            case Genre:
                firstCol = genre;
                break;
            case Year:
                firstCol = Integer.toString(yearPublished);
                break;
            case Condition:
                firstCol = condition.toString();
                break;
        }
        String checkOutLetter = checkedOut ? "Y" : "N";
        String dueDateStr = checkedOut ? dueDate.toString() : "N/A";
        String userIDStr = checkedOut ? Utility.convertUserIDToString(checkoutUserID) : "N/A";

        return String.format(format, firstCol, checkOutLetter, dueDateStr, userIDStr);
    }
}

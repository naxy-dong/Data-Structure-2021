import java.util.EmptyStackException;
/**
 * ReturnStack class allows us to take in returns (push) and check in the last returned book (pop).
 * @author Yu Xiang Dong
 */
public class ReturnStack {
    private ReturnLog topLog;
    
    public ReturnStack(){
        topLog = null;
    }
    /**
     * Pushes a returned book’s ReturnLog into the stack.
     * @param ISBN – The ISBN of the book that was returned.
     * @param returnUserID - The ID number of the person returning this book.
     * @param returnDate – The date the book was actually returned.
     * @return whether if the book is return late or on-time
     * @throws BookNotCheckedOutException Thrown if a book with this ISBN is not
                checked out
     * @throws BookCheckedOutBySomeoneElseException : Thrown if a book was originally
                checked out by someone else.
     * @throws InvalidReturnDateException Thrown if the return date is invalid.
     */
    public boolean pushLog(long ISBN, long returnUserID, Date returnDate) throws BookNotCheckedOutException, BookCheckedOutBySomeoneElseException, InvalidReturnDateException, BookAlreadyReturnedException, BookDoesNotExistException{
        Book checkedOutBook = BookRepository.getBookFromISBN(ISBN);
        if(checkedOutBook == null){
            throw new BookDoesNotExistException();
        }
        //if the book is not checked out
        if(!checkedOutBook.getCheckedOut()){
            throw new BookNotCheckedOutException();
        }
        //if the user tries to return a book that's already returned
        if(ISBNExistsInStack(ISBN)){
            throw new BookAlreadyReturnedException();
        }
        //if it's not the original owner that's checking the book out
        if(checkedOutBook.getCheckoutUserID() != returnUserID){
            throw new BookCheckedOutBySomeoneElseException();
        }
        //if the date is not valid or the date is before the published year, which is impossible
        if(!Date.checkValidity(returnDate) || Date.compare(returnDate, new Date(1,1,checkedOutBook.getYearPublished())) < 0){
            throw new InvalidReturnDateException();
        }
        ReturnLog returnLog = new ReturnLog(ISBN, returnUserID, returnDate);
        //overloaded method of push log
        pushLog(returnLog);
        boolean onTime = Date.compare(checkedOutBook.getDueDate(), returnDate) >= 0;
        //if someone returns late, then check in all the books until the stack is empty
        if(!onTime){
            while(!isEmpty()){
                long returnISBN = popLog().getISBN();
                try{
                    BookRepository.checkInBook(returnISBN);
                }
                //if one of the book is removed from the library or no long have records
                catch(BookDoesNotExistException e){
                    System.out.println(returnISBN + " is no longer registered in the library repository");
                }
            }
        }
        return onTime;
    }

    /**
     * This this pushes the returnLog object to the stack
     * @param returnLog returnLog object to be pushed
     */
    public void pushLog(ReturnLog returnLog){
        returnLog.setNextLog(topLog);
        topLog = returnLog;
    }

    /**
     * Pops the last returned book’s ReturnLog from the top of ReturnStack
     * @return the removed returnLog
     * @throws EmptyStackException thrown if ReturnStack is empty.
     */
    public ReturnLog popLog() throws EmptyStackException{
        if(isEmpty()){
            throw new EmptyStackException();
        }
        ReturnLog returnLog = topLog;
        topLog = topLog.getNextLog();
        return returnLog;
    }

    /**
     * Peeks the last returned book
     * @return the last returned book
     * @throws EmptyStackException thrown if ReturnStack is empty.
     */
    public ReturnLog peekLog() throws EmptyStackException{
        if(isEmpty()){
            throw new EmptyStackException();
        }
        return topLog;
    }

    /**
     * Checks if the ISBN is already in the stack
     * @param ISBN
     * @return if the ISBN is in the stack
     */
    public boolean ISBNExistsInStack(long ISBN){
        boolean ISBNFound = false;
        for(ReturnLog cursor = topLog; cursor != null; cursor = cursor.getNextLog()){
            if(cursor.getISBN() == ISBN){
                ISBNFound = true;
            }
        }
        return ISBNFound;
    }

    /**
     * checks if the stack is empty
     * @return is the stack is empty or not
     */
    public boolean isEmpty() {
        return topLog == null;
    }

    /**
     * prints the stack of returnLogs to a tabular form
     */
    public String toString(){
        String format = "|%1$-15s|%2$-15s|%3$-15s|\n";
        String str = String.format(format, "ISBN", "UserID", "Return Date") + 
                                "=================================================\n";
        for(ReturnLog log = topLog; log != null; log = log.getNextLog()){
            str += log;
        }
        return str;
    }
}

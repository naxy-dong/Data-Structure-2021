/**
 * allows us to track which books have been returned to the library
 * @author Yu Xiang Dong
 */
public class ReturnLog {
    private long ISBN;
    private long userID;
    private Date returnDate;
    private ReturnLog nextLog;

    //default constructor
    public ReturnLog(){
        ISBN = -1;
        userID = -1;
        returnDate = null;
        nextLog = null;
    }

    //constructors with parameters
    public ReturnLog(long ISBN, long userID, Date returnDate){
        this.ISBN = ISBN;
        this.userID = userID;
        this.returnDate = returnDate;
        this.nextLog = null;
    }

    //Getters
    /**
     * @return retrieves the ISBN
     */
    public long getISBN(){
        return ISBN;
    }
    /**
     * @return retrieves the userID
     */
    public long getUserID(){
        return userID;
    }
    /**
     * @return retrieves the returnDate
     */
    public Date getReturnDate(){
        return returnDate;
    }
    /**
     * @return retrieves the nextLog
     */
    public ReturnLog getNextLog(){
        return nextLog;
    }

    //setters
    /**
     * @param ISBN the new ISBN
     * @postcondition ISBN will be changed to the parameter
     */
    public void setISBN(long ISBN){
        this.ISBN = ISBN;
    }
    /**
     * @param userID the new userID
     * @postcondition userID will be changed to the parameter
     */
    public void setUserID(long userID){
        this.userID = userID;
    }
    /**
     * @param returnDate the new returnDate
     * @postcondition returnDate will be changed to the parameter
     */
    public void setReturnDate(Date returnDate){
        this.returnDate = returnDate;
    }

    /**
     * @param nextLog the new nextLog
     * @postcondition nextLog will be changed to the parameter
     */
    public void setNextLog(ReturnLog nextLog){
        this.nextLog = nextLog;
    }
    
    /**
     *  prints the log in a tabular format
     */
    public String toString(){
        String format = "|%1$-15s|%2$-15s|%3$-15s|\n";
        return String.format(format, Utility.convertISBNToString(ISBN), Utility.convertUserIDToString(userID), returnDate);
    }
}

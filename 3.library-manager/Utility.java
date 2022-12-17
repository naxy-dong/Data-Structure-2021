/**
 * This is the class that contains method that handles all type conversions
 * @author Yu Xiang Dong
 */
public class Utility {
    /**
     * it converts the string array to an int
     * @param strArr string array 
     * @return an int array
     */
    public static int[] convertStringArrToIntArr(String[] strArr) throws NumberFormatException{
        int[] intArr = new int[strArr.length];
        
        for(int i = 0; i< intArr.length; i++){
            try{
                intArr[i] = Integer.parseInt(strArr[i]);
            }
            catch(NumberFormatException e){
                throw new NumberFormatException();
            }
        }

        return intArr;
    }
    /**
     * returns the first digit of the ISBN
     * @param ISBN ISBN of the book
     * @return int the first digit of the ISBN
     */
    public static int getFirstDigitFromISBN(long ISBN){
        if(getNumberLength(ISBN) < 13)
            return 0;
        while(ISBN >= 10){
            ISBN /= 10;
        }
        return (int) ISBN;
    }
    /**
     * converts the string to a date object
     * @param dateStr date as a string input in MM/DD/YYYY format
     * @return the date object
     */
    public static Date convertStringToDate(String dateStr) throws InvalidDateException{
        if(dateStr.length() != 10){
            throw new InvalidDateException();
        }
        try{
            int[] dateStrArr = convertStringArrToIntArr(dateStr.split("/"));
            if(dateStrArr.length != 3){
                throw new InvalidDateException();
            }
            Date date = new Date(dateStrArr[1],dateStrArr[0],dateStrArr[2]);
            return date;
        }
        catch(NumberFormatException e){
            throw new InvalidDateException();
        }
    }
    /**
     * determines the length of a number
     * @param number the number that's passed in
     * @return the length of the number
     */
    public static int getNumberLength(long number){
        int count = 0;
        while(number != 0){
            number /= 10; 
            count++;
        }
        return count;
    }
    /**
     * converts ISBN from a string to a long
     * @param ISBNstr ISBN number as a string
     * @return an ISBN number
     * @throws InvalidISBNException thrown if the ISBN is invalid
     */
    public static long convertStringToISBN(String ISBNstr) throws InvalidISBNException{
        long ISBN;
        if(ISBNstr.length() <= 13){
            try{
                ISBN = Long.parseLong(ISBNstr);
            }
            catch(NumberFormatException e){
                throw new InvalidISBNException();
            }
            if(ISBN < 0)
                throw new InvalidISBNException();
        }
        else{
            throw new InvalidISBNException();
        }
        return ISBN;
    }
    /**
     * converts a userID from a string to a long
     * @param userIDStr userID string
     * @return userID as a long
     * @throws InvalidUserIDException thrown if the user ID is invalid
     */
    public static long convertStringToUserID(String userIDStr) throws InvalidUserIDException{
        long userID;
        if(userIDStr.length() <= 10){
            try{
                userID = Long.parseLong(userIDStr);
            }
            catch(NumberFormatException e){
                throw new InvalidUserIDException();
            }
            if(userID < 0)
                throw new InvalidUserIDException();
        }
        else{
            throw new InvalidUserIDException();
        }
        return userID;
    }
    /**
     * converts the ISBN to a string with leading zeros
     * @param ISBN
     * @return ISBN string with leading zeros
     */
    public static String convertISBNToString(long ISBN){
        return addLeadingZeros(ISBN, 13);
    }
    /**
     * converts the userID to a string with leading zeros
     * @param userID 
     * @return userID string with leading zeros
     */
    public static String convertUserIDToString(long userID){
        return addLeadingZeros(userID, 10);
    }

    /**
     * adds the leading zero to a number to a length limit
     * @param number the number that's passed in without any leading zero
     * @param lengthLimit the desired length of the number
     * @return the number with leading 0s as a string  
     */
    public static String addLeadingZeros(long number, int lengthLimit){
        String numStr = Long.toString(number);
        while(numStr.length() != lengthLimit)
            numStr = "0" + numStr;
        return numStr;
    }

    /**
     * converts teh condition to index
     * 
     */
    public static int convertConditionToIndex(Condition condition){
        for(int i = 0; i < Condition.values().length; i++){
            if(condition == Condition.values()[i]){
                return i;
            }
        }
        return -1;
    }
}

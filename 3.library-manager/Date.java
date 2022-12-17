/**
 * Date contains the day, month, and year of a point in time.
 * @author Yu Xiang Dong
 */
public class Date{
    private int day;
    private int month;
    private int year;

    //Default Constructor
    public Date(){
        day = 1;
        month = 1;
        year = 1970;
    }
    //Constructor with parameters
    public Date(int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }
    //Getters
    /**
     * @return retrieves the day
     */
    public int getDay(){
        return day;
    }
    /**
     * @return retrieves the month
     */
    public int getMonth(){
        return month;
    }
    /**
     * @return retrieves the year
     */
    public int getYear(){
        return year;
    }

    //setters
    /**
     * @param day the new day
     * @postcondition day will be changed to the parameter
     */
    public void setDay(int day){
        this.day = day;
    }
    /**
     * @param month the new month
     * @postcondition month will be changed to the parameter
     */
    public void setMonth(int month){
        this.month = month;
    }
    /**
     * @param year the new year
     * @postcondition year will be changed to the parameter
     */
    public void setYear(int year){
        this.year = year;
    }

    /**
     * Prints the date object to a MM/DD/YYYY format
     */
    public String toString(){
        String dayStr = Integer.toString(day);
        String monthStr = Integer.toString(month);
        if(day < 10)
            dayStr = "0" + dayStr;
        if(month < 10)
            monthStr = "0" + monthStr;
        return monthStr + "/" + dayStr + "/" + year;
    }

    /**
     * Compares object x and y and returns an integer indicating the outcome
     * @param x
     * @param y
     * @return 0 if x == y, -1 if x < y, 1 if x > y
     */
    public static int compare(Object x, Object y){
        if(!(x instanceof Date) || !(y instanceof Date)){
            return -1;
        }
        Date d1 = (Date) x;
        Date d2 = (Date) y;
        if(d1.getDay() == d2.getDay() && d1.getMonth() == d2.getMonth() && d1.getYear() == d2.getYear()){
            return 0;
        }
        if(d1.getYear() > d2.getYear()){
            return 1;
        }
        else if(d1.getYear() == d2.getYear()){
            if(d1.getMonth() > d2.getMonth()){
                return 1;
            }
            else if(d1.getMonth() == d2.getMonth()){
                if(d1.getDay() > d2.getDay()){
                    return 1;
                }
            }
        }
        return -1;
    }

    /*
     * @param date the date enter to check validity
     * @return if the date is valid or not
     */
    public static boolean checkValidity(Date date){
        int dayLimit; 
        switch(date.month){
            case 1:
                dayLimit = 31;
                break;
            case 2:
                dayLimit = isLeapYear(date.getYear()) ? 29:28;
                break;
            case 3:
                dayLimit = 31;
                break;
            case 4:
                dayLimit = 30;
                break;
            case 5:
                dayLimit = 31;
                break;
            case 6:
                dayLimit = 30;
                break;
            case 7:
                dayLimit = 31;
                break;
            case 8:
                dayLimit = 31;
                break;
            case 9:
                dayLimit = 30;
                break;
            case 10:
                dayLimit = 31;
                break;
            case 11:
                dayLimit = 30;
                break;
            case 12:
                dayLimit = 31;
                break;
            default:
                return false;
        }

        return ((date.getDay() <= dayLimit && date.getDay() > 0) 
            && (date.getMonth() <= 12 && date.getMonth() > 0) 
            && Utility.getNumberLength(date.year) == 4);
    }

    /**
     * @param year
     * @return whether year is a leap year or not
     */
    public static boolean isLeapYear(int year){
        if(year % 400 == 0){
            return true;
        }
        if(year % 4 == 0){
            return true;
        }
        return false;
    }
}
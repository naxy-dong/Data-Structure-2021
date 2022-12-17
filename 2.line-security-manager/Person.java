
/**
 * Brief:  that contains ticket information of each person and references the next person in line.
 *  The Person class should contain variables for the person’s name, seat number, and next person in line
 * @author Yu Xiang Dong
 */
public class Person {
    private String name;
    private int seatNumber;
    private Person nextPerson;

    public Person(){
        this.name = "";
        this.seatNumber = 0;
        this.nextPerson = null;
    }

    public Person(String name, int seatNumber, Person nextPerson){
        this.name = name;
        this.seatNumber = seatNumber;
        this.nextPerson = nextPerson;
    }

    //Getters
    /**
     * @return retrieves the name of the person
     */
    public String getName(){
        return name;
    }

    /**
     * @return retrieves the seatNumber of the person
     */
    public int getSeatNumber(){
        return seatNumber;
    }
    
    /**
     * @return retrieves the person in the line
     */
    public Person getNextPerson(){
        return nextPerson;
    }

    //Setters
    /**
     * @param name the new name
     * @postcondition name will be changed to the value of the parameter
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @param name the new seatNumber
     * @postcondition seatNumber will be changed to the value of the parameter
     */
    public void setSeatNumber(int seatNumber){
        this.seatNumber = seatNumber;
    }

    /**
     * @param name the new seatNumber
     * @postcondition seatNumber will be changed to the value of the parameter
     */
    public void setNextPerson(Person nextPerson){
        this.nextPerson = nextPerson;
    }
}

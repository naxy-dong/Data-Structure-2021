import src.Person;

/**
 * Brief: The line class contains people waiting in line in ascending seat
    order. The Line class should contain a Person reference to the start of your line and its length.
    Additionally, each line should reference the next available line in your set of n lines
    @author Yu Xiang
 */
public class Line {
    Person headPerson;
    Person tailPerson;
    int length;
    Line lineLink;

    public Line(){
        this.headPerson = null;
        this.tailPerson = null;
        this.length = 0;
        this.lineLink = null;
    }

    
    //Getters
    /**
     * @return retrieves the next line
     */
    public Line getNextLine(){
        return lineLink;
    }
    /**
     * @return retrieves the length of the line
     */
    public int getLength(){
        return length;
    }
    /**
     * @return retrieves the headPerson(or the first person) of the line
     */
    public Person getHeadPerson(){
        return headPerson;
    }

    //Setters
    /**
     * @param line the new line
     * @postcondition the linelink will be changed to the value of the parameter
     */
    public void setNextLine(Line lineLink){
        this.lineLink = lineLink;
    }

    /**
     * @return a boolean that tells whether the list is empty or not
     */
    public boolean isEmpty(){
        return (headPerson == null);
    }

    /**
     * Adds a person to a line in ascending seat order
     * @param attendee: the person to be added to the line
     * @precondition: none
     * @postcondition: The person is added to the list in ascending order.
     * @return: none
     * @throws: none
     */
    public void addPerson(Person attendee){
        //when the linkedlist is empty
        if(headPerson == null){
            headPerson = attendee;
            tailPerson = attendee;
        }
        //comparing the first node
        else if(attendee.getSeatNumber() < headPerson.getSeatNumber()){
            attendee.setNextPerson(headPerson);
            headPerson = attendee;
        } 
        //if the list only contains one node
        else if(headPerson.getNextPerson()==null){
            headPerson.setNextPerson(attendee);
            attendee = tailPerson;
        }
        //Begin to insert the attendee in ascending order.
        else{
            Person node = headPerson;
            while(node.getNextPerson() != null && attendee.getSeatNumber() > node.getNextPerson().getSeatNumber()){
                node = node.getNextPerson();
            }
            if(node.getNextPerson() == null){
                tailPerson = attendee;
            }
            attendee.setNextPerson(node.getNextPerson());
            node.setNextPerson(attendee);
        }
        length++;
    }

    /**
     * Removes the person at the front of the line
     * @precondition: the list is not empty
     * @postcondition: The front person is removed from the list.
     * @return: Person removed from the list.
     * @throws: none
     */
    public Person removeFrontPerson(){
        Person personRemoved;
        if (headPerson == null){
            return null;
        }
        else{
            length--;
            Person person = new Person(headPerson.getName(), headPerson.getSeatNumber(), null);
            personRemoved = person;
            headPerson = headPerson.getNextPerson();
            return personRemoved;
        }
    }
    /**
     * Brief: the program compares each person's seat num and see if it already exists
     * @return whether the seat number is already present
     */
    public boolean seatNumAlreadyPresent(int seatNum){
        boolean found = false;
        for(Person person = headPerson; person!=null; person = person.getNextPerson()){
            if(person.getSeatNumber() == seatNum){
                found = true;
            }
        }
        return found;
    }
    /**
     * Brief: this is not used in the program, its purpose is used for testing and debugging the line class
     * @return a string that onlines the information of this line
     */
    public String toString(){
        if(headPerson == null)
            return "nothing";
        /* This code added, it doesn't affect the outcome of the program, but it saves some memory when there is only one node
        if(headPerson.getNextPerson() == null)
            return headPerson.getName() + "|" + headPerson.getSeatNumber();
        */

        String str = "";
        Person node = headPerson;
        
        while(node != null){
            str += node.getName() + "  |  " + node.getSeatNumber();
            node = node.getNextPerson();
        }
    
        return str;
    }
}

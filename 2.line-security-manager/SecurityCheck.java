import src.Person;

/**
 * Brief: the security check class contains a linkedlist that represent all the different lines. 
 * It is capapble of increasing and decreasing the number of lines available while maintaining 
 * balance of number of people in each lines
 * @author Yu Xiang Dong
 */
public class SecurityCheck {
    Line headLine;
    Line tailLine;
    Line cursorLine;
    int lineCount;

    public SecurityCheck(){
        headLine = new Line();
        tailLine = headLine;
        cursorLine = headLine;
        lineCount = 1;
    }

    /**
     * Brief: Adds a person to a line while maintaining the balance each of the lanes
     * @param name - Name of the person to be added
     * @param seatNumber - The number of this attendee's seat
     * @precondition - The person's seat number is not found in any line
     * @postcondition -  The person is added to a line while maintaining the balance each of the lanes
     * @throws TakenSeatException: Thrown if a person’s seat number is found in any line.
     */
    public void addPerson(String name, int seatNumber) throws TakenSeatException{
        //search for if the person's seatnumber is found in any line
        boolean seatFound = false;
        for(Line line = headLine; line!= null; line=line.getNextLine()){
            if(line.seatNumAlreadyPresent(seatNumber)){
                seatFound = true;
            }
        }
        if(seatFound == true){
            throw new TakenSeatException();
        }
        Person person = new Person(name, seatNumber, null);

        //if we only have one line
        if(headLine.getNextLine() == null){
            headLine.addPerson(person);
        }
        else{
            cursorLine = headLine;
            while(cursorLine.getNextLine() != null && cursorLine.getLength() == cursorLine.getNextLine().getLength()){
                cursorLine= cursorLine.getNextLine();
            }
            //if all the line lengths are equal
            if(cursorLine.getNextLine()==null){
                headLine.addPerson(person);
            }
            else{
                //comparing and decide which line to Add accordingly
                if(cursorLine.getLength() > cursorLine.getNextLine().getLength()){
                    cursorLine.getNextLine().addPerson(person);
                }
                else{
                    cursorLine.addPerson(person);
                }
                
            }
        }
    }

    /**
     * Brief: Removes the person from the list with the lowest seat number 
     * while maintaining the balance each of the lanes
     * @precondition - All lines are not empty
     * @postcondition -  The person from the list with the lowest seat number is removed while maintaining the balance of each lane
     * @return Person removed from the security checks lane
     * @throws AllLinesEmptyException: Thrown if all lines are empty but a next person is requested.
     */
    public Person removeNextAttendee() throws AllLinesEmptyException{
        //exception thrown if all line are empty
        boolean empty = true;
        cursorLine = headLine;
        while(cursorLine != null){
            if(!cursorLine.isEmpty()){
                empty = false;
            }
            cursorLine = cursorLine.getNextLine();
        }
        if(empty){
            throw new AllLinesEmptyException();
        }

        //first traversal, finding the correct lowest Person
        //if the list has a difference of 1, then we can check off the possibility that we are removing the
        //person from the lower length because by doing so violates condition 2
        //otherwise, just compare the seatnum of each node and locate the lowest seat num
        int HighestPeople = -1;
        int lowestSeatNum = Integer.MAX_VALUE;
        cursorLine = headLine;
        while(cursorLine != null){
            if(!cursorLine.isEmpty()){
                if(HighestPeople < cursorLine.getLength()){
                    HighestPeople = cursorLine.getLength();
                    lowestSeatNum = cursorLine.getHeadPerson().getSeatNumber();
                }
                else if(HighestPeople == cursorLine.getLength() && lowestSeatNum > cursorLine.getHeadPerson().getSeatNumber()){
                    lowestSeatNum = cursorLine.getHeadPerson().getSeatNumber();
                }
            }
            cursorLine = cursorLine.getNextLine();
        }
        //second traversal, removing the lowest person
        //once we acquired the lowestSeatNum, the seatNum will become our target and guide to remove the person.
        cursorLine = headLine;
        while(cursorLine.isEmpty() || cursorLine.getHeadPerson().getSeatNumber() != lowestSeatNum){
            cursorLine = cursorLine.getNextLine();
        }
        return cursorLine.removeFrontPerson(); 
    }

    /**
     * Brief: Adds new lines while maintaining the order of seat number and the balance of numbers of each lane.
     * @param newLines - The number of lines to add
     * @postcondition -  The number of available lines is adjusted while maintaining the order of seat number and the balance of numbers of each lane.
     * @throws InvalidLineCountException: Thrown if newLines is negative
     */
    public void addNewLines(int newLines) throws InvalidLineCountException{
        if(newLines < 0){
            throw new InvalidLineCountException();
        }
        //Part I: Adding the lines
        cursorLine = headLine;
        while(cursorLine.getNextLine()!= null){
            cursorLine = cursorLine.getNextLine();
        }

        for(int i = 0; i< newLines; i++){
            cursorLine.setNextLine(new Line());
            cursorLine = cursorLine.getNextLine();
        }

        tailLine = cursorLine;
        lineCount += newLines;
        
        //Part II: sorting the lines
        //step 1: figure out how many people should be present in each line
        cursorLine = headLine;
        int people = 0;
        while(cursorLine != null){
            people+=cursorLine.getLength();
            cursorLine = cursorLine.getNextLine();
        }
        
        int average = people/lineCount;
        
        //Step2: evenly distribute people in the linkedlist
        //this algorithm distribute until the length of each node (except the last node) is equal to the average
        //while loops
        cursorLine = headLine;
        while(cursorLine.getNextLine()!= null){
            while(cursorLine.getLength() > average){
                cursorLine.getNextLine().addPerson(cursorLine.removeFrontPerson());
            }
            cursorLine = cursorLine.getNextLine();
        }
        //after that, cursorLine1 is the last item in the list. 
        //Since average may not be the average because of leftovers, we have to distribute to other nodes who has the average by increasing one node
        Line cursorLine2 = headLine;
        while(cursorLine.getLength() > average){
            cursorLine2.addPerson(cursorLine.removeFrontPerson());
            cursorLine2 = cursorLine2.getNextLine();
        }
    }

    /**
     * Brief: Removing lines while maintaining the order of seat number and the balance of numbers of each lane.
     * @param removedLines - removedLines – An array containing the indices of lines removed (indices start at 1)
     * @postcondition -  The number of available lines is adjusted while maintaining conditions 1 and 2.
     * @throws LineDoesNotExistException: Thrown if a line does not exist
     * @throws SingleLineRemovalException: Thrown if one attempts to remove the only line available
     */
    public void removeLines(int[] removedLines) throws SingleLineRemovalException, LineDoesNotExistException{
        if(lineCount-removedLines.length <= 0){
            throw new SingleLineRemovalException();
        }
        for(int i = 0; i < removedLines.length; i++){
            if(removedLines[i] > lineCount){
                throw new LineDoesNotExistException();
            }
        }

        int lineNumber;
        int average;
        int people;

        //iterate through each items in the array and remove the line accordingly
        for(int i = 0; i < removedLines.length; i++){
            lineNumber = removedLines[i]-i; // we need minus i because if line 1 is deleted, line2 becomes line1 in the next iteration
            
            //check if lineCount is less than the value of removedLines
            //cursorline traverse through to the removed line
            Line removedLine;
            cursorLine = headLine;
            if(lineNumber == 1){
                removedLine = cursorLine; 
                headLine = headLine.getNextLine();
            }
            else{
                for(int j =2; j< lineNumber; j++){
                    cursorLine = cursorLine.getNextLine();
                }
                removedLine = cursorLine.getNextLine();
                cursorLine.setNextLine(cursorLine.getNextLine().getNextLine());
                if(cursorLine.getNextLine() == null){
                    tailLine = cursorLine;
                }
            }
            lineCount--;

            //calculate how many people we are going to add in each list
            cursorLine = headLine;
            people = 0;
            
            while(cursorLine != null){
                people+=cursorLine.getLength();
                cursorLine = cursorLine.getNextLine();
            }
            people += removedLine.getLength();
            average = people/lineCount;
            //this algorithm evenly distributes until the length of each node is equal to the average
            //while loops
            cursorLine = headLine;
            while(cursorLine != null){
                while(cursorLine.getLength() < average){
                    cursorLine.addPerson(removedLine.removeFrontPerson());
                }
                cursorLine = cursorLine.getNextLine();
            }
            Line cursorLine2;
            //Since average may not be the average because of leftovers, we have to distribute to other nodes who has the average by increasing one node
            cursorLine2 = headLine;
            while(removedLine.getLength() != 0){
                cursorLine2.addPerson(removedLine.removeFrontPerson());
                cursorLine2 = cursorLine2.getNextLine();
            }
        }
        
    }

    /**
     * Brief: prints the information of each person in each line in a table format
     */
    public void printAllLines(){
        int lineNum = 1;
        //Formatting the table
        String format = "|%1$-10s|%2$-10s|%3$-10s|\n";
        System.out.format(format, "Line", "Name", "Seat Number");
        System.out.println("==================================");
        for(Line line = headLine; line != null; line = line.getNextLine()){
            for(Person person = line.getHeadPerson(); person != null; person = person.getNextPerson()){
                System.out.format(format, lineNum, person.getName(), person.getSeatNumber());
            }
            lineNum++;
        }
    }

    /**
     * Brief: prints the length of each available line
     */
    public void printStatus(){
        int lineNum = 1;
        String word;
        System.out.print("\n");
        for(Line line = headLine; line != null; line = line.getNextLine()){
            word = line.getLength() == 1 ? "person" : "people";
            System.out.println("Line " + lineNum + ": " + line.getLength() + " " + word + " waiting");
            lineNum++;
        }
    }
}

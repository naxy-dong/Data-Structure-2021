import java.util.Scanner;
/**
 * This class will allow the stadium staff to queue new attendees and adjust the number of lines available
 * Brief: This program should greets the users and say good bye to the user when exiting the program
 * This method should implement the following menu options:
        o (A) – Add Person
        o (N) – Next Person
        o (R) – Remove Lines
        o (L) – Add New Lines
        o (P) – Print All Lines
        o (Q) – Quit
 * @author Yu Xiang Dong
 */

public class SecurityManager {
    private static SecurityCheck securityCheck;
    public static void main(String[] arg){
        securityCheck = new SecurityCheck();
        Scanner sc = new Scanner(System.in);
        String menuInput;

        do{
            securityCheck.printStatus();
            printMenu();
            System.out.print("Please select an option: ");
            menuInput = sc.nextLine().toUpperCase();

            switch(menuInput){
                case "A":
                    String name;
                    int seatNum;

                    System.out.print("Please enter the name: ");
                    name = sc.nextLine();
                    System.out.print("Please enter the seat number: ");
                    try{
                        seatNum = Integer.parseInt(sc.nextLine());
                        if(seatNum < 0)
                            throw new IllegalArgumentException();
                        securityCheck.addPerson(name, seatNum);
                        System.out.println(name + " has been added!");
                    }
                    catch(TakenSeatException e){
                        System.out.println("The seat is already taken.");
                    }
                    catch(NumberFormatException e){
                        System.out.println("You have to enter a valid seat number");
                    }
                    catch(IllegalArgumentException e){
                        System.out.println("Negative number is not allowed for seat number");
                    }
                    break;
                case "N":
                    try{
                        System.out.println(securityCheck.removeNextAttendee().getName() + " has been removed!");
                    }
                    catch(AllLinesEmptyException e){
                        System.out.println("Can't remove person, all lines are empty");
                    }
                    break;
                case "R":
                    String[] lineArr; 
                    String linesInput;

                    System.out.print("Lines to remove: ");
                    linesInput = sc.nextLine();
                    lineArr = linesInput.replaceAll(" ", "").split(",");
                    //differentiating a single item array vs array with more than 1 items
                    if(linesInput.indexOf(",") >= 0){
                        linesInput = linesInput.substring(0, linesInput.lastIndexOf(",")+1) + " and " + linesInput.substring(linesInput.lastIndexOf(",")+1);
                    }

                    //converting String[] to int[]
                    int[] numArr = new int[lineArr.length];
                    try{
                        for(int i = 0 ; i < lineArr.length; i++){
                            numArr[i] = Integer.parseInt(lineArr[i]);
                        }
                        securityCheck.removeLines(numArr);
                        System.out.println("Lines " + linesInput + " has been decommissioned");
                    }
                    catch(LineDoesNotExistException e){
                        System.out.println("One of the line numbers entered does not exist, please enter the correct indices (starting from 1)");
                    }
                    catch(SingleLineRemovalException e){
                        System.out.println("You can't remove the only line availble or you are removing more line than currently available lines");
                    }
                    catch(NumberFormatException e){
                        System.out.println("You have to enter a valid line number");
                    }
                    break;
                case "L":
                    int lineNum;
                    System.out.print("Please enter how many lines you wish to add: ");
                    try{
                        lineNum = Integer.parseInt(sc.nextLine());
                        securityCheck.addNewLines(lineNum);
                        System.out.println(lineNum + " lines has been added\n");
                    }
                    catch(InvalidLineCountException e){
                        System.out.println("The line count can't be negative");
                    }
                    catch(NumberFormatException e){
                        System.out.println("You have to enter a valid number");
                    }
                    break;
                case "P":
                    securityCheck.printAllLines();
                    break;
                case "Q":
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("invalid input");
            }
        }
        while(!menuInput.equals("Q"));
        sc.close();
    }

    /**
     * Brief: Prints the menu to the output
     */
    public static void printMenu(){
        System.out.println("\nMenu:");
        System.out.println("(A) - Add Person");
        System.out.println("(N) - Next Person");
        System.out.println("(R) - Remove Lines");
        System.out.println("(L) - Add New Lines");
        System.out.println("(P) - Print All Lines");
        System.out.println("(Q) - Quit\n");
    }
}

import java.util.Scanner;
/**
 * The PersonManager class allows the user to interact with the database by 
 * listing the people, adding to the list, removing, and retrieving people from the list
 * @author Yu Xiang Dong
 */
public class PersonManager{
    private static PersonDataManager personDataManager;
    public static void main(String[] args){
        System.out.println("Hello user, starting program...");
        personDataManager = new PersonDataManager();
        String menuInput;
        String userInput;
        Scanner sc = new Scanner(System.in);
        do{
            printMenu();
            System.out.print("Please select an option: ");
            menuInput = sc.nextLine().toUpperCase();
            
            switch(menuInput){
                case "I"://Any csv file should work
                    System.out.print("Please enter the file name/directory: ");
                    String fileName = sc.nextLine();
                    try{
                        PersonDataManager.buildFromFile(fileName);
                    }
                    catch(IllegalArgumentException e){
                        System.out.println("The file contains incorrect data format");
                    }
                    break;
                case "A":
                    try{
                        System.out.print("Please enter the name: ");
                        String name = sc.nextLine();
                        if(PersonDataManager.containsDigits(name)){
                            throw new Exception();
                        }
                        System.out.print("Please enter the age: ");
                        int age = Integer.parseInt(sc.nextLine());
                        System.out.print("Please enter the gender(M or F): ");
                        String gender = sc.nextLine();
                        if(!(gender.equals("M") || gender.equals("F"))){
                            throw new Exception();
                        }
                        System.out.print("Please enter the height(in inches): ");
                        double height = Double.parseDouble(sc.nextLine());
                        System.out.print("Please enter the weight(in lbs): ");
                        double weight = Double.parseDouble(sc.nextLine());
                        Person p = new Person(name,gender,age,height,weight);
                        
                        personDataManager.addPerson(p);
                    }
                    catch(PersonAlreadyExistsException e){
                        System.out.println("This person already exists. Duplicate Person Error");
                    }
                    catch(NumberFormatException e){
                        System.out.println("Cannot contain a character, try again");
                    }
                    catch(Exception e){
                        System.out.println("Incorrect Data format, try again");
                    }
                    break;
                case "R":
                    System.out.print("Please enter the name of this person: ");
                    userInput = sc.nextLine();
                    try{
                        personDataManager.removePerson(userInput);
                    }
                    catch(PersonDoesNotExistsException e){
                        System.out.println("This person does not exist");
                    }
                    break;
                case "G":
                    System.out.print("Please enter the name of this person: ");
                    userInput = sc.nextLine();
                    try{
                        personDataManager.getPerson(userInput);
                    }
                    catch(PersonDoesNotExistsException e){
                        System.out.println("This person does not exist");
                    }
                    break;
                case "P":
                    try{
                        personDataManager.printTable();
                    }
                    catch(NullPointerException e){
                        System.out.println("The table is empty.");
                    }
                    break;
                case "S":
                    System.out.print("Please select a name for the file: ");
                    userInput = sc.nextLine();
                    personDataManager.saveToFile(userInput);
                    break;
                case "Q":
                    System.out.println("Program terminated. Good bye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again");
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
        System.out.println("(I) - Import from File");
        System.out.println("(A) - Add Person");
        System.out.println("(R) - Remove Person");
        System.out.println("(G) - Get Info on Person");
        System.out.println("(P) - Print Table");
        System.out.println("(S) - Save to File");
        System.out.println("(Q) - Quit\n");
    }
}
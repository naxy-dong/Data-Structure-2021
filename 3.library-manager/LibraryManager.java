import java.util.EmptyStackException;
import java.util.Scanner;
/**
 * The library manager allow the user to interact
 * with the Stack in order to manipulate them
 * @author Yu Xiang Dong
 */
public class LibraryManager {
    public static BookRepository bookRepository;
    private static ReturnStack returnstack;
    private static Scanner sc;

    public static void main(String[] args){
        bookRepository = new BookRepository();
        returnstack = new ReturnStack();

        sc = new Scanner(System.in);
        String menuInput;

        do{
            printMenu();
            System.out.print("Select what to manage: ");
            menuInput = sc.nextLine();

            switch(menuInput){
                case "B":
                    System.out.print("Please select an option: ");
                    menuInput = sc.nextLine();   
                    ManageBookRepository(menuInput);                    
                    break;
                case "R":
                    System.out.print("Please select an option: ");
                    menuInput = sc.nextLine();                        
                    ManageReturnStack(menuInput);
                    break;
                case "Q":
                    System.out.print("Good bye, have a nice day");
                    break;
                default:
                    System.out.println("invalid input");
            }
        }
        while(!menuInput.equals("Q"));
        sc.close();
    }

    /**
     * This is the hub for managing the book repository
     * @param menuInput the input that user wants to use
     */
    public static void ManageBookRepository(String menuInput){
        long ISBN;
        int shelfNumber;
        switch(menuInput){
            case "C":
                try{
                    System.out.print("Please provide a library user ID(10 digits): ");
                    long userID = Utility.convertStringToUserID(sc.nextLine());
                    System.out.print("Please provide an ISBN number(13 digits): ");
                    ISBN = Utility.convertStringToISBN(sc.nextLine());
                    System.out.print("Please provide a due date(MM/DD/YYYY): ");
                    Date dueDate = Utility.convertStringToDate(sc.nextLine());
                    bookRepository.checkOutBook(ISBN, userID, dueDate);;
                }
                catch(InvalidISBNException e){
                    System.out.println("Invalid ISBN number(has to be 13 digits)");
                }
                catch(InvalidUserIDException e){
                    System.out.println("Invalid User ID(has to be 10 digits)");
                }
                catch(InvalidDateException e){
                    System.out.println("Invalid Date(has to be MM/DD/YYYY format)");
                }
                break;
            case "N":
                try{
                    System.out.print("Please provide an ISBN number(13 digits): ");
                    ISBN = Utility.convertStringToISBN(sc.nextLine());
                    System.out.print("Please provide a name: ");
                    String name = sc.nextLine();
                    System.out.print("Please provide an author: ");
                    String author = sc.nextLine();
                    System.out.print("Please provide a genre: ");
                    String genre = sc.nextLine();
                    System.out.print("Please provide a publishing year: ");
                    int publishingYear = Integer.parseInt(sc.nextLine());
                    System.out.print("Please provide a condition(New, Good, Bad, Replace): ");
                    Condition condition = Condition.valueOf(sc.nextLine());

                    bookRepository.addBook(ISBN, name, author, genre, publishingYear, condition);
                }
                catch(IllegalArgumentException e){
                    System.out.println("invalid input");
                }
                catch(InvalidISBNException e){
                    System.out.println("Invalid ISBN number(has to be 13 digits)");
                }
                break;
            case "R":
                try{
                    System.out.print("Please provide an ISBN number(13 digits): ");
                    ISBN = Utility.convertStringToISBN(sc.nextLine());
                    bookRepository.removeBook(ISBN);
                }
                catch(InvalidISBNException e){
                    System.out.println("Invalid ISBN number(has to be 13 digits)");
                }
                break;
            case "P":
                try{
                    System.out.print("Please provide a shelf number(starting from 0): ");
                    shelfNumber = Integer.parseInt(sc.nextLine());
                    if(shelfNumber >= 10 || shelfNumber < 0){
                        throw new IndexOutOfBoundsException();
                    }
                    bookRepository.printBooksInShelf(shelfNumber);
                    break;
                }
                catch(NumberFormatException e){
                    System.out.println("The shelf number is invalid.");
                }
                catch(IndexOutOfBoundsException e){
                    System.out.println("The index is not within the range of 0-9");
                }
                break;
            case "S":
                try{
                    System.out.print("Please provide a shelf number(starting from 0): ");
                    shelfNumber = Integer.parseInt(sc.nextLine());
                    if(shelfNumber >= 10 || shelfNumber < 0){
                        throw new IndexOutOfBoundsException();
                    }
                    System.out.print("Please select a sorting option: ");
                    menuInput = sc.nextLine();  
                    bookRepository.sortShelf(shelfNumber, menuInput);
                }
                catch(NumberFormatException e){
                    System.out.println("The shelf number entered is invalid");
                }
                catch(IndexOutOfBoundsException e){
                    System.out.println("The index is not within the range of 0-9");
                }
                catch(InvalidSortCriteriaException e){
                    System.out.println("The sorting option entered is not valid");
                }
                break;
            case "Q":
                System.out.print("Good bye, have a nice day");
                break;
            default:
                System.out.println("invalid input");
        }
    }
    /***
     * This is the hub for managing the return stack
     * @param menuInput
     */
    public static void ManageReturnStack(String menuInput){
        long ISBN;
        switch(menuInput){
            case "R":
                try{
                    System.out.print("Please provide an ISBN number of the book you're returning(13 digits): ");
                    ISBN = Utility.convertStringToISBN(sc.nextLine());
                    System.out.print("Please provide your library userID(10 digits): ");
                    Long userID = Utility.convertStringToUserID(sc.nextLine());
                    System.out.print("Please provide your current date(MM/DD/YYYY): ");
                    Date curDate = Utility.convertStringToDate(sc.nextLine());
    
                    if(returnstack.pushLog(ISBN, userID, curDate)){
                        System.out.println(BookRepository.getBookFromISBN(ISBN).getName() + " has been returned on time!");
                    }
                    else{
                        System.out.println(BookRepository.getBookFromISBN(ISBN).getName() + " has been returned LATE! Checking everything in...");
                    }
                }
                catch(InvalidDateException e){
                    System.out.println("Invalid Date(has to be MM/DD/YYYY format)");
                }
                catch(InvalidISBNException e){
                    System.out.println("Invalid ISBN Number(has to be 13 digits)");
                }
                catch(InvalidUserIDException e){
                    System.out.println("Invalid user ID(has to be 10 digits)");
                }
                catch(BookNotCheckedOutException e){
                    System.out.println("The book is not checked out");
                }
                catch(BookCheckedOutBySomeoneElseException e){
                    System.out.println("The book is checked out by someone else");
                }
                catch(InvalidReturnDateException e){
                    System.out.println("Invalid return date entered or the date is before the published year");
                }
                catch(BookAlreadyReturnedException e){
                    System.out.println("The book you're returning is already returned");
                }
                catch(BookDoesNotExistException e){
                    System.out.println("The book you're returning does not exist");
                }
                break;
            case "L":
                try{
                    //if the book repository no longer have that book
                    Book lastBookReturned = BookRepository.getBookFromISBN(returnstack.peekLog().getISBN());
                    if(lastBookReturned == null){
                        throw new BookDoesNotExistException();
                    }
                    String bookName = lastBookReturned.getName();
                    System.out.println(bookName + " is the next book to be checked in");
                }
                catch(EmptyStackException e){
                    System.out.println("There is no returned books in the stack");
                }
                catch(BookDoesNotExistException e){
                    System.out.println("The last book no longer exists in the book repository. Use Check-In Last Return to remove the log");
                }
                break;
            case "C":
                try{
                    long returnedBookISBN = returnstack.popLog().getISBN();
                    BookRepository.checkInBook(returnedBookISBN);
                    Book returnedBook = BookRepository.getBookFromISBN(returnedBookISBN);
                    System.out.println(returnedBook.getName() + " has been checked in!");
                }
                catch(BookDoesNotExistException e){
                    System.out.println("The book does not exist or is removed from the repository. Removing the return log from the stack...");
                }
                catch(EmptyStackException e){
                    System.out.println("There is no returned books in the stack");
                }
                break;
            case "P":
                System.out.println(returnstack);
                break;
            case "Q":
                System.out.print("Good bye, have a nice day");
                break;
            default:
                System.out.println("invalid input");
        }
    }
    /***
     * This prints the whole menu to the console
     */
    public static void printMenu(){
        System.out.println(
                            "\n(B) - Manage Book Repository\n"+
                                "\t* (C) - Checkout Book\n"+
                                "\t* (N) - Add New Book\n"+
                                "\t* (R) - Remove Book\n"+
                                "\t* (P) - Print Repository\n"+
                                "\t* (S) - Sort Shelf:\n"+
                                    "\t\to (I) - ISBN Number\n"+
                                    "\t\to (N) - Name\n"+
                                    "\t\to (A) - Author\n"+
                                    "\t\to (G) - Genre\n"+
                                    "\t\to (Y) - Year\n"+
                                    "\t\to (C) - Condition\n"+
                            "(R) - Manage Return Stack\n"+
                                "\t* (R) - Return Book\n"+
                                "\t* (L) - See Last Return\n"+
                                "\t* (C) - Check In Last Return\n"+
                                "\t* (P) - Print Return Stack\n"+
                            "(Q) - Quit\n"
         );
    }
}

import java.io.*;
import java.util.Scanner;

import src.Person;
/**
 * The PersonDataManager creates Person objects that are later stored in an array. 
 * It can manipute the data by features such as add, remove person from the list.
 * It can retrieve data and print them out the output
 * @author Yu Xiang Dong
 */
public class PersonDataManager {
    private static Person[] people;
    /**
     * Uses the File class to read the .csv file and store the information in a data structure
     * @param location File path of the file to be read as a String
     * @precondition valid file path
     * @postcondition none
     * @return the PersonDataManager constructed from csv file
     * @throws IllegalArgumentException  Thrown if the data entered is in the wrong
        format. For example, there is a letter in height or a number in the
        person’s name.
     * @throws FileNotFoundException  Thrown if the file is not found
     */
    public static void buildFromFile (String location) throws IllegalArgumentException{
        try(Scanner sc = new Scanner(new File(location));){
            String[] personData;
            String data;

            int age;
            double weight;
            double height;

            int len = lineCounter(location);
            people = new Person[len-1];

            sc.nextLine();
            int count = 0;
            
            do{
                data = sc.nextLine();
                if(data.isEmpty()){
                    break;
                }
                personData = data.replace(" ","").split(","); // removes the space and separates the comma
                
                //Error checking Stage
                if(containsDigits(personData[0]) || !(personData[1].replaceAll("\"", "").equals("F") || personData[1].replaceAll("\"", "").equals("M"))){
                    throw new IllegalArgumentException();
                }
                try{
                    age = Integer.parseInt(personData[2]);
                    height = Double.parseDouble(personData[3]);
                    weight = Double.parseDouble(personData[4]);
                }
                catch(NumberFormatException e){
                    throw new IllegalArgumentException();
                }

                //Create a new person and add it to the list
                Person person = new Person(personData[0], personData[1], age, height, weight);
                people[count] = person; 
                count++;
            }
            while(!data.equals(null) && sc.hasNextLine());
            System.out.println("Import successful");
        }
        catch(FileNotFoundException e){
            System.out.println("File not found, check to make sure your directory is correct");
        }
    }

    /**
     * Uses the File class to read the .csv file and return the number of lines in the csv file 
     * @param location File path of the file to be read as a String
     * @precondition valid file path
     * @postcondition none
     * @return the number of lines in the csv file
     * @throws FileNotFoundException  Thrown if the file is not found
     */
    public static int lineCounter(String location){
        int count = 0;
        String data;
        try(Scanner sc = new Scanner(new File(location));){
            do{
                data = sc.nextLine();
                if(data.isEmpty()){
                    break;
                }
                count++;
            }
            while(!data.equals(null) && sc.hasNextLine()); 
        }
        catch(FileNotFoundException e){
            System.out.println("File not found, check to make sure your directory is correct.");
        }
        return count;
    }
    /**
     * Checks if a string contains a digit
     * @param str the string to be checked
     * @return a boolean statement that indicates whether the str contains digits or not
     */
    public static boolean containsDigits(String str){
        char[] letters = str.toCharArray();
        for(char c: letters){
            if(Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }
    
    /**
     *  Adds the Person object in the data structure chosen in the correct
        alphabetical order that the list is in.
     * @param newPerson – Person object to be added.
     * @precondition The person does not exist in the list.
     * @postcondition The person is added to the list
     * @return None
     * @throws PersonAlreadyExistsException: Thrown if a person with all the same
        biological statistics already exists in the list.
     */
    public void addPerson (Person newPerson) throws PersonAlreadyExistsException{
        for(Person person: people){
            if(person.equals(newPerson)){
                throw new PersonAlreadyExistsException();
            }
        }
        int index = 0;
        for(Person person: people){
            if(person.getName().compareTo(newPerson.getName()) < 0){
                index++;
            }
            else{
                break;
            }
        }

        Person[] newPeople = new Person[people.length+1];
        for (int i = 0; i < index; i++){
            newPeople[i] = people[i];
        }
        newPeople[index]=newPerson;
        for (int i = index; i < people.length; i++){
            newPeople[i+1] = people[i];
        }
        
        people = newPeople;

        System.out.println(newPerson.getName() + " has been added to the list");
    }

    
    /**
     *  Retrieves and prints the data of the Person object from the data
        structure chosen
     * @param name – Name of the Person object to be printed.
     * @precondition The person with the given name exists.
     * @postcondition None
     * @return None
     * @throws PersonDoesNotExistsException: Thrown if a person with the given
        name does not exist.
     */
    public void getPerson (String name) throws PersonDoesNotExistsException{
        boolean found = false;
        for(Person person: people){
            if(person.getName().equals(name)){
                found = true;
                String gender =  person.getGender().equals("M") ? "male" : "female";
                int inches = (int) person.getHeight() % 12;
                int feet = (int) person.getHeight()/12;
                System.out.println(name + " is " + person.getAge() + " year old "  + gender + " who is " + feet + " feet and " + inches + " inches tall and weighs " + person.getWeight() + " pounds.");
                break;
            }
        }
        if(!found){
            throw new PersonDoesNotExistsException();
        }
    }
    /**
     *  Removes the Person from the data structure chosen.
     * @param name – Name of the Person object to be removed.
     * @precondition The person with the given name exists.
     * @postcondition None
     * @return None
     * @throws PersonDoesNotExistsException: Thrown if a person with the given
        name does not exist.
     */
    public void removePerson (String name) throws PersonDoesNotExistsException {
        boolean found = false;
        for(int i = 0; i < people.length; i++){
            if(people[i].getName().equals(name)){
                found = true;
                Person newPeople[] = new Person[people.length-1];
                for(int j = 0; j< i; j++){
                    newPeople[j] = people[j];
                }
                for(int j = i+1; j< people.length; j++){
                    newPeople[j-1] = people[j];
                }
                people = newPeople;
                System.out.println(name + " has been removed.");
                break;
            }
        }
        
        if(!found){
            throw new PersonDoesNotExistsException();
        }
    }

    /**
     * Creates a file and stores all information from the current array into the file 
     * @param filename – 
     * @precondition valid file path
     * @postcondition the new file with all the data is created
     * @return None
     * @throws IOException: Thrown if an error occured in any circumstance
     */
    public void saveToFile(String filename){
        try{
            File csvFile = new File(filename);
            if(csvFile.createNewFile()){
                System.out.println("File created successfuly: " + csvFile.getName());
            }
            else{
                System.out.println("The file already exists");
            }
            FileWriter fWriter = new FileWriter(filename);
            fWriter.write("\"Name\",     \"Sex\", \"Age\", \"Height (in)\", \"Weight (lbs)\"\n");
            for(Person person: people){
                fWriter.write(person.getName()+","+person.getGender()+","+person.getAge()+ "," + person.getHeight()+","+ person.getWeight()+"\n");
            }
            fWriter.close();
            System.out.println("Successfully wroted to the file");
        }
        catch(IOException e){
            System.out.println("An error occured");
        }
    }

    /**
     * Prints the PersonDataManager in tabular form
     * @throws NullPointerException: thrown if the data is empty
     */
    public void printTable() throws NullPointerException{
        if(people == null){
            throw new NullPointerException();
        }
        
        System.out.println("Name|Sex|Age|Height (in)|Weight (lbs)\n========================================");

        for(Person person: people){
            System.out.println(person);
        }
    }
}

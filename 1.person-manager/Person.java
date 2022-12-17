/**
 * The person class contains biological statistics of a person. 
 * The statistics should contain variables for the person’s name, age, gender, height, and weight
 * @author Yu Xiang Dong    
 */

public class Person{
    private int age;
    private double height, weight;
    private String name, gender;
    /**
     * Constructs an default person object
     * @postcondition the person has been initalized with the default attributes
     */
    public Person(){
        age = 0;
        height = 0;
        weight = 0;
        name = "John Doe";
        gender = "Undecided";
    }
    /**
     * Constructs a person object with specified attributes
     * @param name the name of the person
     * @param gender the gender of the person (M or F)
     * @param age the age of the person in years
     * @param height the height of the person in inches
     * @param weight the weight of the person in pounds
     * @postcondition the person has been initalized with the specified attributes
     */
    public Person(String name, String gender, int age, double height, double weight){
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.name = name.replaceAll("\"","");
        this.gender = gender.replaceAll("\"","");
    }

    /**
     * print all of the person’s data in tabular form
     */
    public String toString() {
        int inches = (int) this.height % 12;
        int feet = (int) this.height/12;
        return name + " | " + gender + " | " + age + " | " + feet + " feet " + inches + " inches " + " | " + weight + " pounds";
    }

    //Getters

    /**
     * @return retrieves the name of the person
     */
    public String getName(){
        return name;
    }
    /**
     * @return retrieves the gender of the person
     */
    public String getGender(){
        return gender;
    }
    /**
     * @return retrieves the age of the person
     */
    public int getAge(){
        return age;
    }
    /**
     * @return retrieves the height of the person
     */
    public double getHeight(){
        return height;
    }
    /**
     * @return retrieves the weight of the person
     */
    public double getWeight(){
        return weight;
    }
    
    
    //Setters
    /**
     * @param name the new name
     * @postcondition name will be changed to the parameter
     */
    public void setName(String name){
        this.name = name;
    }
    /**
     * @param gender the new gender
     * @postcondition gender will be changed to the parameter
     */
    public void setGender(String gender){
        this.gender = gender;
    }
    /**
     * @param age the new age
     * @postcondition age will be changed to the parameter
     */
    public void setAge(int age){
        this.age = age;
    }
    /**
     * @param height the new height
     * @postcondition height will be changed to the parameter
     */
    public void setHeight(double height){
        this.height = height;
    }
    /**
     * @param weight the new weight
     * @postcondition weight will be changed to the parameter
     */
    public void setWeight(double weight){
        this.weight = weight;
    }
    
    /**
     * Checks if the two persons class is equal by comparing all of the attributes.
     * @param obj the object the funciton is comparing to
     * @postcondition outputs if the two object are equal to each other or not
     */
    public boolean equals(Object obj){
        if(!(obj instanceof Person)){
            return false;
        }

        Person p = (Person) obj;
        return p.getName().equals(this.getName()) && p.getGender().equals(this.getGender()) && p.getAge() == this.getAge() && p.getHeight() == this.getHeight() && p.getWeight() == this.getWeight();
    }
}
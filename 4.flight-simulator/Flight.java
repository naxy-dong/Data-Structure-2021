import java.util.Random;
public class Flight {
    private String destination;
    private BoardingQueue boardingQueue = new BoardingQueue();

    private Passenger[] firstClass = new Passenger[2];
    private Passenger[] businessClass = new Passenger[3];
    private Passenger[] premiumEconomy = new Passenger[4];
    private Passenger[] economy = new Passenger[6];

    private int minitesLeftBoarding;
    private int minitesLeftDeparting; 

    private int firstClassInd;
    private int businessClassInd;
    private int premiumEconomyInd;
    private int economyInd;
    private int arrivalInd;

    private boolean boarding;

    //Indicating if the covid person is found in current flight
    public static boolean covidPersonFound;
    //Indicating if the event occurs in current flight
    public static boolean eventOccurs;
    //Indicating if the air force 1 plane currently present
    public static boolean airForcePresent;
    //Indicating if the current plane is the air force 1
    private boolean isAirForce1Plane;

    //Default constructor
    public Flight() {
        this.destination = "NOWHERE";
        this.firstClassInd = 0;
        this.businessClassInd = 0;
        this.premiumEconomyInd = 0;
        this.economyInd = 0;
        this.arrivalInd = 1;
        this.minitesLeftBoarding = 25;
        this.minitesLeftDeparting = 5; 
        this.boarding = true;
        this.isAirForce1Plane = false;
    }

    //constructor with parameters  
    public Flight(boolean airForcePresent) {
        this.destination = "NOWHERE";
        this.firstClassInd = 0;
        this.businessClassInd = 0;
        this.premiumEconomyInd = 0;
        this.economyInd = 0;
        this.arrivalInd = 1;
        this.minitesLeftBoarding = 25;
        this.minitesLeftDeparting = 5; 
        this.boarding = true;
        this.isAirForce1Plane = true;
        Flight.airForcePresent = true;
    }

    /**
     * Simulating the flight's current minute and handles events such as setting destination, boarding, and departing
     * @param dequeueProb the probability that a passenger will be dequeued
     * @param arrivalProb the probability that a passenger arrive
     * @param currentMinute the current minute of the simulation
     */
    public void simulateCurrentMinute(float dequeueProb, float arrivalProb, int currentMinute, Random randomNumberGenerator){
        //If the flight has just been created
        if(destination.equals("NOWHERE")){
            return;
        }
        if(airForcePresent && !isAirForce1Plane){
            return;
        }
        //If the plane is boarding, then substract the minitesleftBoarding
        //If the plane is not boarding, then it means that it's departing
        if(isBoarding()){
            minitesLeftBoarding--;
        }
        else{
            minitesLeftDeparting--;
        }

        float randomDequeueNum = randomNumberGenerator.nextFloat();
        float randomArrivalNum = randomNumberGenerator.nextFloat();
        
        //if there is no event happening, meaning no dequeuing a person and no person arrives 
        if((randomDequeueNum >= dequeueProb && randomArrivalNum >= arrivalProb) || !boarding){
            eventOccurs = false;
            return;
        }
        else{
            eventOccurs = true;
        }

        //Event 1, if a passenger is dequeued from the boarding queue.
        try{
            if(randomDequeueNum < dequeueProb && isBoarding()){
                Passenger pass = boardingQueue.dequeueHighestPriorityPassenger();
                addToFlight(pass);
                //if the plane is full, then depart right away
                if(getPassengerCount() == 15){
                    prepareDeparture();
                    if(randomArrivalNum < arrivalProb)
                        System.out.println("- Passenger is attempting to enter boarding queue... failed due to a recently full plane!");
                }
            }
        }
        catch(NoPassengerException e){
            System.out.println("- Attempted to board a new passenger at " + this.destination + " boarding queue... failed due to lack of passengers in boarding queue.");
        }
        
        //Event 2, if a new passenger arrives in its boarding queue
        /**
         * 0 <= randomPass < 0.1 --> passenger is first class
         * 0.1 <= randomPass < 0.2 --> passenger is business class
         * 0.2 <= randomPass < 0.5 --> passenger is premium economy class
         * 0.5 <= randomPass < 0.9 --> passenger is economy class
         * 0.9 <= randomPass < 1 --> passenger has COVID
         */
        try{
            if(randomArrivalNum < arrivalProb && isBoarding()){
                //if the plane is full, or the plane has 15 people, then deny any people who boards the queue
                if(getPassengerCount() == 15){
                    return;
                }
                float randomPassNum = randomNumberGenerator.nextFloat();
                if(!(randomPassNum >= 0.98)){
                    Passenger pass;
                    if(randomPassNum < 0.12){
                        pass = new Passenger(TravelClass.FIRST, arrivalInd, currentMinute);
                        boardingQueue.enqueuePassenger(pass); 
                    }
                    else if(randomPassNum < 0.24){
                        pass = new Passenger(TravelClass.BUSINESS, arrivalInd, currentMinute);
                        boardingQueue.enqueuePassenger(pass); 
                    }
                    else if(randomPassNum < 0.56){
                        pass = new Passenger(TravelClass.PREMIUM_ECONOMY, arrivalInd, currentMinute);
                        boardingQueue.enqueuePassenger(pass); 
                    }
                    else{
                        pass = new Passenger(TravelClass.ECONOMY, arrivalInd, currentMinute);
                        boardingQueue.enqueuePassenger(pass); 
                    }
                    arrivalInd++;
                    System.out.println("- " + pass.getTravelClass().toString().toLowerCase() + "-class passenger (ID " + pass.getPassengerID() 
                                    + ") on flight to " + destination +" has entered the flight's boarding queue!");
                }
                else{
                    Flight.covidPersonFound = true;
                    System.out.println("- COVID positive passenger found attempting to board flight to "+ destination 
                                    + "! All current departures and boarding extended by 10 minutes!");
                }
            }
        }
        catch(NoRoomException e){
            System.out.println("- Passenger is attempting to enter boarding queue... failed due to a recently full plane!");
        }
    }

    /**
     * Prepares the departure of the flight
     */
    public void prepareDeparture(){
        boarding = false;
        minitesLeftBoarding = 0;
        eventOccurs = true;
        if(this.isAirForce1Plane){
            System.out.println("- Air Force 1 flight to "+this.destination+" has "+ getPassengerCount()+" passengers and is now preparing for departure!");
        }
        else{
            System.out.println("- Flight to "+this.destination+" has "+ getPassengerCount()+" passengers and is now preparing for departure!");
        }
    }

    /**
     * notifying the departure of the flight
     */
    public void startDeparture(){
        eventOccurs = true;
        if(this.isAirForce1Plane){
            System.out.println("- Air Force 1 flight to " + this.destination + " is departed! Resuming departures and boarding... ");
        }
        else{
            System.out.println("- Flight to " + this.destination + " is departed!");
        }
    }

    /**
     * Adds a passenger to the correct passenger array
     * @param boardedPass  The passenger boarding the plane.
     */
    public void addToFlight(Passenger boardedPass){
        boolean seatNotFound = true;
        int priorityIdx = Utility.convertTravelClassToIndex(boardedPass.getTravelClass());

        //0 is the highest priority, and 3 is the lowest.
        //get the passenger's ID and decide what array would go. 
        while(seatNotFound && priorityIdx < TravelClass.values().length){
            switch(TravelClass.values()[priorityIdx]){
                case FIRST:
                    if(Utility.addToArray(firstClass, firstClassInd, boardedPass)){
                        seatNotFound = false;
                        firstClassInd++;
                    }
                    else
                    {
                        priorityIdx++;
                    }
                    break;
                case BUSINESS:
                    if(Utility.addToArray(businessClass, businessClassInd, boardedPass)){
                        seatNotFound = false;
                        businessClassInd++;
                    }
                    else
                    {
                        priorityIdx++;
                    }
                    break;
                case PREMIUM_ECONOMY:
                    if(Utility.addToArray(premiumEconomy, premiumEconomyInd, boardedPass)){
                        seatNotFound = false;
                        premiumEconomyInd++;
                    }
                    else
                    {
                        priorityIdx++;
                    }
                    break;
                case ECONOMY:
                    if(Utility.addToArray(economy, economyInd, boardedPass)){
                        seatNotFound = false;
                        economyInd++;
                    }
                    else
                    {
                        priorityIdx++;
                    }
                    break;
            }
        }
        if(seatNotFound || priorityIdx == TravelClass.values().length){
            System.out.println("- A " + boardedPass.getTravelClass().toString().toLowerCase()+
            "-class passenger is rejected to the flight due to lack of seat in the plane");
        }
        else{
            System.out.println("- " + boardedPass.getTravelClass().toString().toLowerCase() + "-class passenger (ID " + boardedPass.getPassengerID() 
            + ") on flight to " + destination +" has boarded on a "+ TravelClass.values()[priorityIdx].toString().toLowerCase() 
            +"-class seat!");
        }
    }

    /**
     * returns the string that prints all of the Flight"s data in tabular form.
     */
    public String toString(){
        String format = "|%1$-15s|%2$-15s|%3$-15s|\n";
        String str="";
        str += "RFK -> " + destination + "\n";
        str += String.format(format, "Class", "ID", "Arrival Time") + 
        "=================================================\n";
        for(Passenger pass: firstClass){
            if(pass != null){
                str += String.format(format, "First Class", pass.getPassengerID(), pass.getArrivalTime());
            }
        }
        for(Passenger pass: businessClass){
            if(pass != null){
                str += String.format(format, "Business Class", pass.getPassengerID(), pass.getArrivalTime());
            }
        }
        for(Passenger pass: premiumEconomy){
            if(pass != null){
                str += String.format(format, "Premium Economy", pass.getPassengerID(), pass.getArrivalTime());
            }
        }
        for(Passenger pass: economy){
            if(pass != null){
                str += String.format(format, "Economy", pass.getPassengerID(), pass.getArrivalTime());
            }
        }
        return str;
    }

    /**
     * retrieves the destination of the current flight
     * @return the destination of the current flight as a string
     */
    public String getDestination() {
        return destination;
    }
    
    /**
     * Sets the current destination to the destination passed in
     * @param destination the destination passed in
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    /**
     * retrieves the minutes left for people to board the plane
     * @return the minutes left for people to board
     */
    public int getMinitesLeftBoarding() {
        return minitesLeftBoarding;
    }
    
    /**
     * Sets the current minute left boarding to the minute passed in
     * @param minitesLeftBoarding the minutes passed in
     */
    public void setMinitesLeftBoarding(int minitesLeftBoarding) {
        this.minitesLeftBoarding = minitesLeftBoarding;
    }
    
    /**
     * retrieves the minutes left for plane to depart
     * @return the minutes left for plane to depart
     */
    public int getMinitesLeftDeparting() {
        return minitesLeftDeparting;
    }
    
    /**
     * Sets the current minute left departing to the minute passed in
     * @param minitesLeftBoarding the minutes passed in
     */
    public void setMinitesLeftDeparting(int minitesLeftDeparting) {
        this.minitesLeftDeparting = minitesLeftDeparting;
    }

    /**
     * @return boolean indicating if the plane is boarding or not
     */
    public boolean isBoarding() {
        return boarding;
    }
    
    /**
     * retrieves the number of passenger currently in the boarding queue 
     * @return number of passenger
     */
    public int getBoardingQueueSize(){
        return boardingQueue.getSize();
    }
    
    /**
     * retrieves the current number of passenger in the plane
     * @return current number of passenger in the plane
     */
    public int getPassengerCount(){
        return Utility.countElementsInObjArray(firstClass) +Utility.countElementsInObjArray(businessClass)
        +Utility.countElementsInObjArray(premiumEconomy)+Utility.countElementsInObjArray(economy);
    }
    
    /**
     * @return boolean indicating if the current plane is a air force 1 plane or not
     */
    public boolean isAirForce1Plane() {
        return isAirForce1Plane;
    }
}

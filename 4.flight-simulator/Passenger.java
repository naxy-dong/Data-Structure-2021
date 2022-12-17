public class Passenger {
    private TravelClass travelClass;
    int passengerID;
    int arrivalTime;

    //default constructor
    public Passenger() {
        this.travelClass = null;
        this.passengerID = -1;
        this.arrivalTime = -1;
    }

    //constructor with parameters
    public Passenger(TravelClass travelClass, int passengerID, int arrivalTime) {
        this.travelClass = travelClass;
        this.passengerID = passengerID;
        this.arrivalTime = arrivalTime;
    }

    /**
     * compares the priority of the current passenger the other passenger and 
     * returns an integer that indicates which passenger has the higher priority
     * @param other the other passeneger to compare to
     * @return 0 if the priority are the same;
     *         greater than 0 if current passenger has a higher priority
     *         less than 0 if current passenger class has a lower priority
     */
    public int comparePriority(Passenger other){
        return Utility.convertTravelClassToIndex(other.getTravelClass()) - Utility.convertTravelClassToIndex(this.travelClass);
    }

    /**
     * @return the class of the this passenger
     */
    public TravelClass getTravelClass() {
        return this.travelClass;
    }

    /**
     * sets the current travelClass to the travelClass passed in
     * @param travelClass travelClass passed in
     */
    public void setTravelClass(TravelClass travelClass) {
        this.travelClass = travelClass;
    }

    /**
     * @return the passenger ID 
     */
    public int getPassengerID() {
        return passengerID;
    }

    /**
     * sets the current passenger ID to the passenger ID passed in
     * @param travelClass passenger ID passed in
     */
    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    /**
     * @return the arrivalTime
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * sets the current arrival time to the current arrival time passed in
     * @param travelClass arrival time passed in
     */
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}

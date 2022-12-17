import java.util.Scanner;
import java.util.Random;

public class FlightSimulator {
    private static Flight[] flightTerminal = new Flight[20];
    private static int flightIdx = 0; // this points to the first empty flight index
    public static Random randomNumberGenerator;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args){
        System.out.println("Welcome to RFK Private International Airport");
        
        boolean userSettingDone = false;
        long seed=0;
        float arrivalProb = -1;
        float dequeueProb = -1;
        float newFlightProb = -1;
        int totalSimulationMinutes = -1;

        /** ============USER INPUT=========== */
        while(!userSettingDone){
            try{
                System.out.print("Enter the seed for this simulation: ");
                seed = Long.parseLong(sc.nextLine());
                randomNumberGenerator = new Random(seed);

                System.out.print("Enter the probability of a passenger's arrival: ");
                arrivalProb = Float.parseFloat(sc.nextLine());
                if(arrivalProb < 0 || arrivalProb >= 1)
                    throw new IllegalArgumentException(); 

                System.out.print("Enter the probability that a passenger is dequeued: ");
                dequeueProb = Float.parseFloat(sc.nextLine()); 
                if(dequeueProb < 0 || dequeueProb >= 1)
                    throw new IllegalArgumentException(); 

                System.out.print("Enter the probability that there is a new flight at RFK: ");
                newFlightProb = Float.parseFloat(sc.nextLine()); 
                if(dequeueProb < 0 || dequeueProb >= 1)
                    throw new IllegalArgumentException(); 

                System.out.print("Enter how many minutes this simulation should take: ");
                totalSimulationMinutes = Integer.parseInt(sc.nextLine());
                System.out.println();
                if(totalSimulationMinutes < 0)
                    throw new IllegalArgumentException(); 

                userSettingDone = true;
            }
            catch(IllegalArgumentException e){
                System.out.println("Invalid Input, try again");
            }
        }
        
        /** ==========SIMULATION STARTS========== */
        for(int currentMinute = 0; currentMinute< totalSimulationMinutes; currentMinute++){
            Flight newestFlightCreated = null;
            boolean noEventOccurs = true;
            int activeFlightNum = Flight.airForcePresent ? 1 : Utility.countElementsInObjArray(flightTerminal);
            System.out.println("Minute: " + currentMinute + "\tCurrent Active Flights: " + activeFlightNum +
            "\n===============================================");

            /** ======EVENTS====== */
            System.out.println("Events:");
            
            //if a new flight is created
            if(randomNumberGenerator.nextFloat() < newFlightProb){
                noEventOccurs = false;
                if(flightIdx != flightTerminal.length && flightTerminal[flightIdx] == null){
                    //determininng if the flight is a normal or an air force one
                    if(randomNumberGenerator.nextFloat() < 0.95){
                        flightTerminal[flightIdx] = new Flight();
                    }
                    else{
                        //There can only be one Air Force 1 plane present at the time, if it lands on 0.05 chance, then it will just be a normal flight
                        if(!Flight.airForcePresent){
                            flightTerminal[flightIdx] = new Flight(true);
                        }
                        else{
                            flightTerminal[flightIdx] = new Flight();
                        }
                    }
                    newestFlightCreated = flightTerminal[flightIdx];
                    flightIdx++;
                }
                else{
                    //This event happens if and only if there are 20 active terminals and there is a new flight.
                    //if anyone of them is not active, then the program will create a new flight there, meaning there will be no holes in the array 
                    System.out.println("- Attempting to start boarding for new flight... failed due to lack of open terminal.");
                }
            }
            //very crucial step: sorting the flight has to happen before simulating each flight and after the new flight is created
            //This is because the flightIndex, which points to the next empty flight terminal, has to be updated after the new flight is created.
            sortFlightInAscendingOrder();
            
            //For every terminal, simulate the current minute
            for(Flight flight : flightTerminal){
                if(flight != null){
                    flight.simulateCurrentMinute(dequeueProb, arrivalProb, currentMinute, randomNumberGenerator);
                    if(Flight.covidPersonFound){
                        extendAllFlightsBy10Minutes();
                        Flight.covidPersonFound = false;
                    }
                    if(Flight.eventOccurs){
                        noEventOccurs = false;
                    }
                }
            }

            //had to have this as a separate for loop such that it does not depart at the minute that a covid person is found
            for(Flight flight : flightTerminal){
                if(flight != null){
                    if(flight.getMinitesLeftBoarding() == 0 && flight.isBoarding()){
                        flight.prepareDeparture();
                    }
                    if(flight.getMinitesLeftDeparting() == 0){
                        flight.startDeparture();
                        if(flight.isAirForce1Plane()){
                            Flight.airForcePresent = false;
                        }
                    }
                }
            }

            //After all the events have been printed, then we asked the user to name the flight
            if(newestFlightCreated != null){
                String airForceStr = "";
                if(newestFlightCreated.isAirForce1Plane()){
                    airForceStr = "on Air Force 1 ";
                }
                System.out.print(String.format("- A new flight terminal %sis open! Please enter the destination: ", airForceStr));
                newestFlightCreated.setDestination(FlightSimulator.sc.nextLine());
                
                System.out.println(String.format("- A new flight %sto ... %s ... has begun boarding!", airForceStr, newestFlightCreated.getDestination()));
            }
            //If nothing occurs, then print this
            if(noEventOccurs){
                System.out.println("Nothing to note...");
            }
            System.out.println();

            /** ======BOARDING====== */
            System.out.println("Currently Boarding:");
            boolean noFlightIsBoarding = true;
            for(Flight flight:flightTerminal){
                if(flight !=null && flight.isBoarding()){
                    if(Flight.airForcePresent){
                        if(flight.isAirForce1Plane()){
                            System.out.println("- Air Force 1 flight to " + flight.getDestination() + " has " + flight.getMinitesLeftBoarding() 
                            + " minutes to board, " + flight.getPassengerCount() + " passenger(s), and " 
                            + flight.getBoardingQueueSize() +" person(s) waiting to board");
                        }
                    }
                    else{
                        System.out.println("- Flight to " + flight.getDestination() + " has " + flight.getMinitesLeftBoarding() 
                        + " minutes to board, " + flight.getPassengerCount() + " passenger(s), and " 
                        + flight.getBoardingQueueSize() +" person(s) waiting to board");
                    }
                    noFlightIsBoarding = false;
                }
            }
            if(Flight.airForcePresent){
                System.out.println("- Boarding will resume once Air Force 1 departs");
            }
            else if(noFlightIsBoarding){
                System.out.println("Nothing to note...");
            }
            System.out.println();
            
            /** ======DEPARTING====== */
            System.out.println("Departing:");
            boolean noFlightIsPreparingDeparture = true;
            for(Flight flight:flightTerminal){
                if(flight !=null && !flight.isBoarding() && flight.getMinitesLeftDeparting() != 0){
                    if(Flight.airForcePresent){
                        if(flight.isAirForce1Plane()){
                            System.out.println("- Air Force 1 flight to " + flight.getDestination() + " has " + flight.getPassengerCount() + 
                            " passengers and "+flight.getMinitesLeftDeparting() + " minutes before departure");
                        }
                    }
                    else{
                        System.out.println("Flight to " + flight.getDestination() + " has " + flight.getPassengerCount() + 
                        " passengers and "+flight.getMinitesLeftDeparting() + " minutes before departure");
                    }
                    noFlightIsPreparingDeparture = false;
                }
            }
            if(Flight.airForcePresent){
                System.out.println("- Departures will resume once Air Force 1 departs");
            }
            else if(noFlightIsPreparingDeparture){
                System.out.println("Nothing to note...");
            }
            System.out.println();

            /** ======FINAL DEPARTURES====== */
            System.out.println("Final Departures:");
            boolean noFlightIsDeparting = true;
            //There might be a possibility that there are two/three departures due to covid timings, or we want to have our 
            //program to expand and handle multiple departures.
            for(int i = 0; i < flightTerminal.length; i++){
                if(flightTerminal[i] !=null && flightTerminal[i].getMinitesLeftDeparting() == 0){
                    System.out.println(flightTerminal[i]);
                    flightTerminal[i] = null; //delete this flight
                    flightIdx = i; // the next new flight will be created here
                    noFlightIsDeparting = false;
                }
            }
            if(noFlightIsDeparting){
                System.out.println("Nothing to note...");
            }
            System.out.println();
        }
        /*==============End of program===========*/
        System.out.println("Minute "+ totalSimulationMinutes +":\nSimulation terminated. Thank you for choosing RFK!");
    }

    /**
     * Extends all the current active flights by 10 minutes. 
     */
    public static void extendAllFlightsBy10Minutes(){
        for(Flight flight:flightTerminal){
            if(flight != null){
                if(Flight.airForcePresent){
                    if(!flight.isAirForce1Plane()){
                        return;
                    }
                    else{
                        flight.setMinitesLeftBoarding((flight.getMinitesLeftBoarding() + 10));
                    }
                }
                else{
                    if(flight.isBoarding()){
                        flight.setMinitesLeftBoarding((flight.getMinitesLeftBoarding() + 10));
                    }
                    else{
                        flight.setMinitesLeftDeparting((flight.getMinitesLeftDeparting() + 10));
                    }
                }
            }
        }
    }

    //We want the flightTerminal to be sorted such that it's in a ascending order such that the first element has the lowest time remaining
    //Extra requirement: the sorting algorithm needs to deal with holes in the array as we are going to delete the flight after it's finished
    //Update the flightIdx to its first empty null spot
    //takes O(n^2) time complexity. But I would love some suggestions to a quicker way to solve it.
    /**
     * sorts the flights in the terminal in an ascender order by the boarding time left 
     */
    public static void sortFlightInAscendingOrder(){
        for(int i = 0; i< flightTerminal.length; i++){
            //swap it with the lowest boarding minute.
            int lowestFlightIdx = determineLowestTimeLeftBoardingFlightIndex(i);
            if(lowestFlightIdx == -1){
                flightIdx = i;
                return;
            }
            Utility.swap(lowestFlightIdx, i, flightTerminal);
        }
    }

    /**
     * Determines the index of the flight with the lowest boarding time left
     * @param startingIndex the starting index to search
     * @return the index of the flight with the lowest boarding time left
     */
    public static int determineLowestTimeLeftBoardingFlightIndex(int startingIndex){
        int lowestFlightIdx = -1;
        for(int i = startingIndex; i < flightTerminal.length; i++){
            if(flightTerminal[i] != null){
                if(lowestFlightIdx == -1 || flightTerminal[lowestFlightIdx].getMinitesLeftBoarding() > flightTerminal[i].getMinitesLeftBoarding()){
                    lowestFlightIdx = i;
                }
            }
        }
        return lowestFlightIdx;
    }
}
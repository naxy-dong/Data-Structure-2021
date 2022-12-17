
/**
 * Boarding queue representing a flight’s line for boarding.
 * This is implemented use queue structure using HEAP data structure
 */
public class BoardingQueue{
    private Passenger[] data;
    private int heapSize;
    private int maxSize;

    //default constructor
    public BoardingQueue(){
        this.maxSize = 10;
        this.heapSize= 0;
        this.data = new Passenger[maxSize];
    }

    //constructor with parameter(s)
    public BoardingQueue(int maxSize){
        this.maxSize = maxSize;
        this.heapSize= 0;
        this.data = new Passenger[maxSize];
    }

    /**
     * Finds the parent index of the index passed in
     * @param idx the index passed in
     * @return the parent idx of the index passed in
     */
    public int getParent(int idx){
        return (idx-1)/2;
    }

    /**
     * Finds the left child index of the index passed in
     * @param idx index passed in 
     * @return left child index
     */
    public int getLeft(int idx){
        return idx * 2 +1;
    }

    /**
     * Finds the right child index of the index passed in
     * @param idx index passed in 
     * @return right child index
     */
    public int getRight(int idx){
        return idx * 2 + 2;
    }
    
    /**
     * Get the size of the heap
     * @return the current size of the heap
     */
    public int getSize(){
        return heapSize;
    }
    
    /**
     * Checks if the heap is empty or not
     * @return boolean indicating if the heap is empty or not
     */
    public boolean isEmpty() {
        return heapSize == 0;
    }

    /**
     * Checks if the heap is full or not
     * @return boolean indicating if the heap is full or not
     */
    public boolean isFull() {
        return heapSize == maxSize;
    }

    /**
     * swaps data in index 1 and index 2
     * @param pos1 index 1
     * @param pos2 position
     */
    public void swap(int pos1, int pos2){
        Utility.swap(pos1, pos2, data);
    }  

    /**
     * Enqueues the passenger in a ascending priority and passenger ID
     * @param pass passenger to be enqueued 
     * @throws NoRoomException Exception thrown when there is no passenger in the boarding queue and tries to dequeuing a passenger 
     */
    public void enqueuePassenger(Passenger pass) throws NoRoomException{
        if(isFull()){
            throw new NoRoomException();
        }
        data[heapSize] = pass;
        int position = heapSize;
        //swap if the parent's priority is lower than the current priority
        //if they have the same priority, then check who has the less passengerID
        while(position != 0){
            if(data[getParent(position)].comparePriority(data[position]) < 0 ||
            (data[getParent(position)].comparePriority(data[position]) == 0 && data[getParent(position)].getPassengerID() > data[position].getPassengerID())){
                swap(getParent(position), position);
            }
            position = getParent(position);
        }
        heapSize++;
    }

    /**
     * Dequeue the passenger with the highest priority and passenger ID while maintaining the heap structure
     * @return the passenger with the highest priority and passenger ID
     * @throws NoPassengerException exception thrown if there is no room in the boarding queue and tries to enqueue a passenger 
     */
    public Passenger dequeueHighestPriorityPassenger() throws NoPassengerException{
        Passenger pass = data[0];
        if(isEmpty()){
            throw new NoPassengerException();
        }
        data[0]=data[heapSize-1];
        heapSize--;
        fixHeap();
        return pass;
    }

    /**
     * Fixes the heap by maintaining the condition that the top passenger is the highest priority.
     */
    public void fixHeap(){
        int position = 0;
        int childPos;
        while(getLeft(position) < heapSize){
            childPos = getLeft(position);
            if(getRight(position) < heapSize){
            /**
             * Determining the child to swap with
                * //if the priority of the right child is greater than the left child, then there is no need to check their passenger ID
                * //if the priority of the right child is equal to the left child, then if the passengerID of the right child is less than the left child
                * meaning that the right child arrived faster than the left child, then childPos++
             */
                if(data[childPos].comparePriority(data[getRight(position)]) < 0
                || (data[childPos].comparePriority(data[getRight(position)]) == 0 && data[childPos].getPassengerID() > data[getRight(position)].getPassengerID())
                ){
                    childPos++;
                }
            }
            /*
                //if the priority of the current position is less than the child's priority, then swap immediately
                //if the priority of the current position is equal to the child's priority, then compare the IDs
                    if the passengerID of the the child is less than the current,
                    meaning that the child arrived faster than the current, then swap
            */
            if(data[position].comparePriority(data[childPos]) < 0
            ||(data[position].comparePriority(data[childPos]) == 0 && data[position].getPassengerID() > data[childPos].getPassengerID())){
                swap(childPos, position);
            }
            position = childPos;
        }
    }
}
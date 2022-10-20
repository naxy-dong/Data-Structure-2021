/**
 * Exception thrown when there is no passenger in the boarding queue and tries to dequeuing a passenger 
 * @author Yu Xiang Dong
 */
public class NoPassengerException extends Exception{
    public NoPassengerException(){
        super();
    }
}

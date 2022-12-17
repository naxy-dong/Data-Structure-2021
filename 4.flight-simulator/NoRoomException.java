/**
 * exception thrown if there is no room in the boarding queue and tries to enqueue a passenger 
 * @author Yu Xiang Dong
 */
public class NoRoomException extends Exception{
    public NoRoomException(){
        super();
    }
}

public class Utility {
    /**
     * add an item to array at an index 
     * @param <T> Any type 
     * @param arr array to be changed
     * @param idx index for the item to be added in
     * @param item item to be added in
     * @return returns whether if the insertion is successful or not
     */
    public static <T> boolean addToArray(T[] arr, int idx, T item){
        if(idx < arr.length){
            arr[idx] = item;
            return true;
        }
        return false;
    }

    /**
     * Converts the travel class to an index
     * @param travelClass travel class passed in
     * @return the index of the travel class
     */
    public static int convertTravelClassToIndex(TravelClass travelClass){
        for(int i = 0; i < TravelClass.values().length; i++){
            if(travelClass == TravelClass.values()[i]){
                return i;
            }
        }
        return -1;
    }

    /**
     * convers the index to a travel class
     * @param i index passed in
     * @return corresponding travel class
     */
    public static TravelClass convertIndexToTravelClass(int i){
        return TravelClass.values()[i];
    }

    /**
     * counts the elements in object array
     * @param <T> any type
     * @param arr array
     * @return the number of elements in the array
     */
    public static <T> int countElementsInObjArray(T[] arr){
        int count = 0;
        for(T item: arr){
            if(item != null){
                count++;
            }
        }
        return count;
    }

    /**
     * swap two elements in the array 
     * @param <T> any type
     * @param idx1 index1
     * @param idx2 index2
     * @param arr array to be changed
     */
    public static <T> void swap(int idx1, int idx2, T[] arr){
        T tempItem = arr[idx1];
        arr[idx1] = arr[idx2];
        arr[idx2] = tempItem;
    }
}

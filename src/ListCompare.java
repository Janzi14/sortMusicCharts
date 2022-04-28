import java.util.Comparator;
import java.util.List;

public class ListCompare implements Comparator<List> {

    @Override
    public int compare(List o1, List o2) {
        int firstCompare = -1 * (Integer.compare(Integer.parseInt((String) o1.get(1)), Integer.parseInt((String) o2.get(1))));
        if (firstCompare == 0) {
            // if there is more than one artist with the same amount of songs, use the average rating
            return -1 * (Float.compare(Float.parseFloat((String) o1.get(2)), Float.parseFloat((String) o2.get(2))));
        } else {
            // return the result of the first comparison
            return firstCompare;
        }
    }
}

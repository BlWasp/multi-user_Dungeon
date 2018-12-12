package Server;

import javafx.util.Pair;

import java.io.Serializable;

public class Zone extends Pair implements Comparable, Serializable {
    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public Zone(Integer key, Integer value) {
        super(key, value);
    }

    @Override
    public int compareTo(Object o) {
        Zone z = (Zone) o;
        Integer start1, start2, end1, end2;
        start1=(Integer) this.getKey();
        start2=(Integer) z.getKey();
        end1=(Integer) this.getValue();
        end2=(Integer) z.getValue();
        if(start1.compareTo(start2)!=0)
            return start1.compareTo(start2);
        else
            return end1.compareTo(end2);
    }
}

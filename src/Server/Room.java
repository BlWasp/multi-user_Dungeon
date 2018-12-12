package Server;

import java.io.Serializable;

public class Room implements Serializable {
    private final int id;
    private Border north;
    private Border west;
    private Border south;
    private Border east;

    public Room(int id, Border north, Border west, Border south, Border east) {
        this.id = id;
        this.north = north;
        this.west = west;
        this.south = south;
        this.east = east;
    }

    public int getId() {
        return id;
    }

    public Border getNorth() {
        return north;
    }

    public void setNorth(Border north) {
        this.north = north;
    }

    public Border getWest() {
        return west;
    }

    public void setWest(Border west) {
        this.west = west;
    }

    public Border getSouth() {
        return south;
    }

    public void setSouth(Border south) {
        this.south = south;
    }

    public Border getEast() {
        return east;
    }

    public void setEast(Border east) {
        this.east = east;
    }

}

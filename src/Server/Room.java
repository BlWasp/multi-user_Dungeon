package Server;

import java.io.Serializable;

/**
 * Classe d'une case
 */
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

    /**
     * Récupère le numéro de la case
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Récupère la destination en allant au Nord
     * @return
     */
    public Border getNorth() {
        return north;
    }

    public void setNorth(Border north) {
        this.north = north;
    }

    /**
     * Récupère la destination en allant à l'Ouest
     * @return
     */
    public Border getWest() {
        return west;
    }

    public void setWest(Border west) {
        this.west = west;
    }

    /**
     * Récupère la destination en allant au Sud
     * @return
     */
    public Border getSouth() {
        return south;
    }

    public void setSouth(Border south) {
        this.south = south;
    }

    /**
     * Récupère la destination en allant à l'Est
     * @return
     */
    public Border getEast() {
        return east;
    }

    public void setEast(Border east) {
        this.east = east;
    }

}

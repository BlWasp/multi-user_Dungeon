package Server;

import java.io.Serializable;

/**
 * Représente un passage entre deux cases adjacentes
 */
public class Door extends Border implements Serializable {
    //pour afficher une porte vertical alignement="v" sinon ="h"
    private String alignment;

    /**
     * Constructeur de la classe Door
     * @param dest
     *          Destination de la porte
     * @param alignment
     */
    public Door(int dest, String alignment) {
        this.crossable=true;
        this.dest=dest;
        this.alignment=alignment;
    }

    /**
     * Affichage
     * @return
     */
    @Override
    public String toString() {
        if(alignment.equals("v"))
            return "▯";
        else
            return "▭";
    }


}

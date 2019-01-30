package Client;

import Server.Entity;

import java.io.Serializable;

/**
 * Il s'agit de la classe représentant un Avatar, donc le personnage en jeu
 */
public class Avatar extends Entity {

    private static final long serialVersionUID = 1548468510L;


    /**
     * Constructeur d'un avatar de base
     * @param name
     *          Son nom
     */
    public Avatar(String name) {
        super(name);
        lifePoint=10;
    }

    /**
     * Constructeur d'un avatar à une position précise
     * @param name
     *          Son nom
     * @param pos
     *          Sa position
     */
    public Avatar (String name, Integer pos){
        super(name);
        lifePoint=10;
        position = pos;
    }

    /**
     * Permet de modifier la position courante de l'avatar
     * @param position
     *              La nouvelle position
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

}

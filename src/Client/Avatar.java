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
        maxLifePoint=10;
        lifePoint=maxLifePoint;
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
        maxLifePoint=10;
        lifePoint=maxLifePoint;
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

    public void levelUp(){
        if(isInLife==false){
            isInLife=true;
            lifePoint=1;
        }
        System.out.println("max : "+maxLifePoint);
        maxLifePoint=maxLifePoint+5;
        restoreLife();
    }

    public int heal(int value){
        if(isInLife==false){
            isInLife=true;
            lifePoint=1;
        }
        if(lifePoint==maxLifePoint) return -1;
        lifePoint=lifePoint+value;
        if(lifePoint>maxLifePoint) lifePoint=maxLifePoint;
        return 0;
    }

}

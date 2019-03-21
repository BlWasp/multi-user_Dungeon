package Server;

import java.util.Objects;

/**
 * Classe permettant de créer un monstre
 */
public class Monster extends Entity {

    private static final long serialVersionUID = 1548468510L;

    /**
     * Construction de monstres standards
     * @param name
     *              Nom du monstre
     * @param pos
     *              Case dans laquelle il sera
     */
    //Constructeur pour monstres "standard"
    public Monster (String name, Integer pos){
        super(name);
        lifePoint = 10;
        position = pos;
    }

    /**
     * Construction de monstre avec une vie personnalisée
     * @param name
     *              Nom du monstre
     * @param pos
     *              Case dans laquelle il sera
     * @param maxLifePoint
     *              Quantité de vie voulue
     */
    //Constructeur pour monstres "spéciaux"
    public Monster (String name, Integer pos, Integer maxLifePoint) {
        super(name);
        this.maxLifePoint = maxLifePoint;
        lifePoint = this.maxLifePoint;
        position = pos;
    }

    /**
     * Permet de modifier la position courante du monstre
     * @param position
     *              Nouvelle position voulue
     */
    public void setPosition (Integer position) {
        this.position = position;
    }

    /**
     * Redéfinition de equals pour Monster.
     * La vérification se fait en fonction de la position
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        // null check
        if (obj == null)
            return false;
        // type check and cast
        if (getClass() != obj.getClass())
            return false;

        Entity ent = (Entity) obj;
        // field comparison
        return Objects.equals(position, ent.position);
    }
}

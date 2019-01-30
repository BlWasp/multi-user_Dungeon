package Server;
import java.io.Serializable;
import java.util.Objects;

/**
 * Classe mère des différentes entités du serveur de jeu (Avatar, Monstres)
 */
public abstract class Entity implements Serializable, Cloneable {

    protected final String name;
    protected int lifePoint;
    protected Integer position;
    protected boolean isInLife;
    protected long uid;

    /**
     * Constructeur de la classe
     */
    public Entity(){
        uid=Long.MIN_VALUE + ((long) Math.random() * (Long.MAX_VALUE - Long.MIN_VALUE));
        name="Boo";
        isInLife=true;
    }

    /**
     * Redéfinition la méthode équals pour comparer deux entités
     * @param obj
     * @return
     */
    //On redéfinis la méthode equals pour pouvoir comparer des Entité
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
        return Objects.equals(uid, ent.uid)
                && Objects.equals(name, ent.name);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Entity(String name){
        this.name = name;
        isInLife = true;
    }

    /**
     * Récupère le nome de l'entité
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Récupère le nombre de points de vie de l'entité
     * @return
     */
    public int getLifePoint() {
        return lifePoint;
    }

    /**
     * Permet de modifier le nombre de points de vie de l'entité
     * @param lifePoint
     *          Nouvelle valeur de la vie de l'entité
     */
    public void setLifePoint(int lifePoint) {
        this.lifePoint = lifePoint;
    }

    /**
     * Récupère la position de l'entité sur le plateau
     * @return
     */
    public Integer getPosition() {
        return position;
    }

    public boolean isInLife() {
        return isInLife;
    }

    /**
     * Permet de retirer de la vie à l'entité (dans un combat, lors d'un escape, etc)
     * @param lifeLosed
     *              Quantité de vie perdue
     * @return
     *              Retourne la nouvelle vie
     */
    public int loseLife(int lifeLosed) {
        if(!isInLife){
            System.out.println("Vous êtes déjà mort");
            return -1;
        }
        int currentLife = this.getLifePoint();
        this.setLifePoint(currentLife - lifeLosed);
        if(lifePoint<=0){
            isInLife=false;
            System.out.println("Vous êtes mort.");
        }
        return this.getLifePoint();
    }

}

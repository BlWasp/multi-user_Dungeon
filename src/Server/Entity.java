package Server;
import java.io.Serializable;
import java.util.Objects;

public abstract class Entity implements Serializable, Cloneable {

    protected final String name;
    protected int lifePoint;
    protected Integer position;
    protected boolean isInLife;
    protected long uid;

    public Entity(){
        uid=Long.MIN_VALUE + ((long) Math.random() * (Long.MAX_VALUE - Long.MIN_VALUE));
        name="Boo";
        isInLife=true;
    }

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

    public String getName() {
        return name;
    }

    public int getLifePoint() {
        return lifePoint;
    }

    public void setLifePoint(int lifePoint) {
        this.lifePoint = lifePoint;
    }

    public Integer getPosition() {
        return position;
    }

    public boolean isInLife() {
        return isInLife;
    }

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

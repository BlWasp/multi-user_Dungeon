package Server;

import java.io.Serializable;

public abstract class Entity implements Serializable, Cloneable {
    protected final String name;
    protected int lifePoint;
    protected Integer position;
    protected boolean isInLife;

    public Entity(){
        name="Boo";
        isInLife=true;
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

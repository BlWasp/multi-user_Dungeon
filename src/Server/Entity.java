package Server;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    protected final String name;
    protected int lifePoint;
    protected Integer position;

    public Entity(){
        name="Boo";
    }

    public Entity(String name){
        this.name = name;
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

    public int loseLife(int lifeLosed) {
        int currentLife = this.getLifePoint();
        this.setLifePoint(currentLife - lifeLosed);
        return this.getLifePoint();
    }
}

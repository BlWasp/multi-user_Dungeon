package Client;

import Server.Entity;

import java.io.Serializable;

public class Avatar extends Entity {

    private static final long serialVersionUID = 1548468510L;


    public Avatar(String name) {
        super(name);
        lifePoint=10;
    }

    public Avatar (String name, Integer pos){
        super(name);
        lifePoint=10;
        position = pos;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}

package Server;

public class Monster extends Entity {
    public Monster (String name, Integer pos){
        super(name);
        lifePoint=5;
        position = pos;
    }
}

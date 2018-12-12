package Server;

public class Monster extends Entity {

    private static final long serialVersionUID = 1548468510L;

    //Constructeur pour monstres "standard"
    public Monster (String name, Integer pos){
        super(name);
        lifePoint = 10;
        position = pos;
    }

    //Constructeur pour monstres "spéciaux"
    public Monster (String name, Integer pos, Integer life) {
        super(name);
        lifePoint = life;
        position = pos;
    }

    public void setPosition (Integer position) {
        this.position = position;
    }

}

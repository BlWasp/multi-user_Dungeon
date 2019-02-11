package Server;

/**
 * Classe pour créer un mur non traversable
 */
public class Wall extends Border {
    private String alignment;
    public Wall(String alignment) {
        crossable=false;
        dest=-1;
        this.alignment = alignment;

    }

    @Override
    public String toString() {
        if(alignment.equals("v"))
            return "▮";
        else
            return "▬";
    }
}

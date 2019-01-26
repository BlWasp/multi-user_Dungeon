package Server;

/**
 * Classe pour créer un mur non traversable
 */
public class Wall extends Border {

    public Wall() {
        crossable=false;
        dest=-1;

    }

    @Override
    public String toString() {
        return "x";
    }
}

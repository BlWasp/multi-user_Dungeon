public class Wall extends Border {
    //pour afficher un mur vertical alignement="v" sinon ="h"
    private String alignement;
    public Wall(String alignment) {
        crossable=false;
        dest=-1;
        this.alignement=alignment;
    }

    @Override
    public String toString() {
        if(alignement=="v")
            return "|";
        else
            return "_";
    }
}

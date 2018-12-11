package Server;

public class Door extends Border {
    //pour afficher une porte vertical alignement="v" sinon ="h"
    private String alignment;

    public Door(int dest, String alignment) {
        this.crossable=true;
        this.dest=dest;
        this.alignment=alignment;
    }

    @Override
    public String toString() {
        if(alignment=="v")
            return "|";
        else
            return "-";
    }


}

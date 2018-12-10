package Server;

public class Door extends Border {

    public Door(int dest) {
        this.crossable=true;
        this.dest=dest;
    }

    @Override
    public String toString() {
        return "D";
    }

}

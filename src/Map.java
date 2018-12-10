import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

public class Map {
    int size;
    private Room board[][];

    public Map(int size) {
        this.size = size;
        board = new Room[size][size];

        for (int i = 0; i < size*size; i++) {

            //On initialise les bordures de la futur pièce en fonction de sa position
            Border n,w,s,e;
            //Si on est sur les cases du haut on place un mur au nord
            n=(i<size) ? new Wall("h") : new Door(i-size);
            //Si on est sur les cases du bord droit on place un mur à l'est
            e=(i%size==size-1) ? new Wall("v") : new Door(i+1);
            //pareil pour le bord inférieur
            s=(i>=size*size-size) ? new Wall("h") : new Door(i+size);
            //puis pour le bord gauche
            w=(i%size==0) ? new Wall("v") : new Door(i-1);

            //On instantie la pièce
            Room current = new Room(i,n,w,s,e);

            //On l'ajoute au plateau
            board[i/size][i%size]=current;

        }
    }

    public void displayGrid(){
        for (int i = 0; i < size; i++) {
            System.out.print(" "+board[0][i].getNorth().toString()+" ");
        }
        System.out.println("\n");
        for (int i = 0; i < size; i++) {
            System.out.print(board[0][i].getWest().toString()+board[0][i].getId()+board[0][i].getEast().toString());
        }
        System.out.println("\n");
        for (int i = 0; i < size; i++) {
            System.out.print(" "+board[0][i].getSouth().toString()+" ");
        }
    }
}

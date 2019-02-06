package Client;

import Server.IChatServer;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

public class DisplayManager {
    private List<Avatar> playerList; //liste des joueur sur la même case
    private Avatar myAvatar;

    public DisplayManager(Avatar myAvatar) {
        this.playerList = new LinkedList<>();
        this.myAvatar = myAvatar;
    }

    public void updateList(IChatServer cs){
        try {
            playerList=cs.getNeighbour(myAvatar);
        } catch (RemoteException e) {
            System.out.println("impossible de mettre a jour la liste des voisins");
        }
    }

    public void displayNeighbour(IChatServer cs){
        updateList(cs);
        System.out.println("Player in the same room :");
        for( Avatar av : playerList){
            System.out.println(av.getName()+" "+av);
        }
    }

}

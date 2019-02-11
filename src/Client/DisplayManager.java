package Client;

import Server.Entity;
import Server.IChatServer;
import Server.IGameServer;
import Server.Room;
import javafx.util.Pair;

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

    public void displayPosition(IGameServer gs, IChatServer cs){
        Pair<Room, Entity> result = null;
        try {
            result = gs.getRoomInfo(myAvatar);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Room currentRoom = result.getKey();
        Entity monster = result.getValue();
        updateList(cs);
        System.out.println("◼ ▬ "+currentRoom.getNorth()+" "+currentRoom.getNorth()+" "+currentRoom.getNorth() +" ▬ ◼");
        System.out.println("▮           ▮"+"     Monster's name : "+monster.getName());
        System.out.println(currentRoom.getWest()+"           "+currentRoom.getEast()+"     Monster's life point : "+monster.getLifePoint());
        System.out.println(currentRoom.getWest()+"    "+parsePosition(currentRoom.getId())+"    "+currentRoom.getEast()+"     "+playerList.size()+" players in your room");
        System.out.println(currentRoom.getWest()+"           "+currentRoom.getEast());
        System.out.println("▮           ▮");
        System.out.println("◼ ▬ "+currentRoom.getSouth()+" "+currentRoom.getSouth()+" "+currentRoom.getSouth() +" ▬ ◼");
        //System.out.println(currentRoom.getNorth().toString()+" "+currentRoom.getEast().toString()+" "+monster.getName()+" "+monster.getLifePoint()+"□");
    }

    public String parsePosition(Integer position){
        if(position<100){
            if(position<10){
                if(position<1){
                    return "000";
                }
                return "00"+position;
            }
            return "0"+position;
        }
        return position.toString();
    }



}

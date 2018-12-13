package Server;

import Client.Avatar;

import java.util.*;

public class GameServerSimple {
    private int available;
    private Grid gGrid;
    private Zone z = new Zone(0,0);
    int size = 8;
    private Map<Integer, List<String>> positionMap;
    private List<Avatar> listAvatar;
    protected GameServerSimple(){
        available=1;
        gGrid = new Grid(size);
        gGrid.displayGrid();
        positionMap = new LinkedHashMap<>();
        listAvatar = new LinkedList<>();
        for (int i = 0; i < size*size; i++) {
            positionMap.put(i, new ArrayList<String>());
        }
    }

    public GameServerSimple(Grid grid, int size, Zone z){
        gGrid=grid;
        this.size = size;
        positionMap = new LinkedHashMap<>();
        listAvatar = new LinkedList<>();
        available=1;
        this.z = z;
        for (int i = 0; i < size*size; i++) {
            positionMap.put(i, new ArrayList<String>());
        }
        gGrid.displayGrid();
    }

    public GameServerSimple(int state){
        available=state;
    }

    public int connection(String avUsed, Integer position) {
        if(available==0)
            return available;
        List<String> user = positionMap.get(position);
        user.add(avUsed);

        Avatar avConnect = new Avatar(avUsed,position);
        listAvatar.add(avConnect);
        return available;
    }


    public int move(String avUsed, int position, String goTo) {
        List<String> src = positionMap.get(position);
        src.remove(avUsed);
        Integer x,y;
        x=position/8;
        y=position%8;
        Room r = gGrid.getRoom(x,y);
        Integer dest;
        switch (goTo) {
            case "N" : dest = r.getNorth().dest; break;
            case "W" : dest = r.getWest().dest; break;
            case "E" : dest = r.getEast().dest; break;
            case "S" : dest = r.getSouth().dest; break;
            default : dest = -1; break;
        }
        if(dest==-1)
            return -1;
        //Si le serveur ne gère pas la case
        if(dest<(Integer) z.getKey() || dest>(Integer) z.getValue())
            return -2;

        List<String> lDest = positionMap.get(dest);
        lDest.add(avUsed);
        return dest;

    }

    public void escape(Avatar avUsed, int position, String goTo) {
        this.move(avUsed.getName(), position, goTo);
        for (int i=0;i<listAvatar.size();i++) {
            if (listAvatar.get(i).getName().contentEquals(avUsed.getName())) {
                listAvatar.remove(i);
            }
        }
        listAvatar.add(avUsed);
    }


    public void displayGameInfo() {

    }

    public int getPosition(Avatar av){
        return 0;
    }

    public Zone getZ() {
        return z;
    }

    public void setZ(Zone z) {
        if (this.z!=null)
            this.z = z;
    }
}

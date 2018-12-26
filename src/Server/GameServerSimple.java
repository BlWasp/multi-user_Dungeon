package Server;

import Client.Avatar;

import java.util.*;

public class GameServerSimple {
    private int available;
    private Grid gGrid;
    private Zone z = new Zone(0,0);
    int size = 8;
    private Map<Integer, List<Avatar>> positionAvatar;
    private Map<Integer, Monster> positionMonster;
    protected GameServerSimple(){
        available=1;
        gGrid = new Grid(size);
        gGrid.displayGrid();
        positionAvatar = new LinkedHashMap<>();
        for (int i = 0; i < size*size; i++) {
            positionAvatar.put(i, new ArrayList<Avatar>());
        }
    }

    public GameServerSimple(Grid grid, int size, Zone z){
        gGrid=grid;
        this.size = size;
        positionAvatar = new LinkedHashMap<>();
        positionMonster = new LinkedHashMap<>();
        available=1;
        this.z = z;
        for (int i = 0; i < size*size; i++) {
            positionAvatar.put(i, new ArrayList<Avatar>());
            //positionMonster.put(i, new Monster());
        }
        gGrid.displayGrid();
    }

    public GameServerSimple(int state){
        available=state;
    }

    public int connection(Avatar avUsed, Integer position) {
        if(available==0)
            return available;
        List<Avatar> user = positionAvatar.get(position);
        user.add(avUsed);
        return available;
    }


    public int move(Avatar avUsed, int position, String goTo) {
        List<Avatar> src = positionAvatar.get(position);
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

        List<Avatar> lDest = positionAvatar.get(dest);
        lDest.add(avUsed);
        return dest;

    }

    public void escape(Avatar avUsed, int position, String goTo) {
        this.move(avUsed, position, goTo);
        avUsed.setLifePoint(avUsed.getLifePoint() - 2);
        /*for (int i=0;i<listAvatar.size();i++) {
            if (listAvatar.get(i).getName().contentEquals(avUsed.getName())) {
                listAvatar.remove(i);
            }
        }*/
        //listAvatar.add(avUsed);
    }

    public int attackAvatar(Entity target, Avatar ifAvatar, Integer position, int lifeLosed) {
        if (target.getClass() == Avatar.class) {
            List<Avatar> tmp = positionAvatar.get(position);
            Avatar tmpAv = ifAvatar;
            for (int i=0; i<tmp.size(); i++) {
                if (tmp.get(i).equals(ifAvatar)) {
                    tmpAv = tmp.get(i);
                    tmpAv.loseLife(lifeLosed);
                }
            }
            System.out.println(positionAvatar.get(position));
            return tmpAv.getLifePoint();
        } else {
            Monster tmp = positionMonster.get(position);
            tmp.loseLife(lifeLosed);
            return tmp.getLifePoint();
        }
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

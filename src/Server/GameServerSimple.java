package Server;

import Client.Avatar;
import Client.IPlayer;
import com.sun.security.ntlm.Client;

import java.awt.datatransfer.DataFlavor;
import java.rmi.RemoteException;
import java.util.*;

public class GameServerSimple {
    private int available;
    private Grid gGrid;
    private Zone z = new Zone(0,0);
    int size = 8;
    private Map<Integer, List<Avatar>> positionAvatar;
    private Map<Integer, Monster> positionMonster;
    private Map<Avatar, IPlayer> lclient = new LinkedHashMap<>();
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

    public int connection(Avatar avUsed, Integer position, IPlayer player) {
        if(available==0)
            return available;
        if(lclient.containsKey(avUsed)) return -1;
        avUsed.setPosition(position);
        positionAvatar.get(position).add(avUsed);
        lclient.put(avUsed,player);
        return available;
    }


    public int move(Avatar avUsed, String goTo) {
        avUsed=getAvatar(avUsed);
        if(avUsed==null) return -10;
        if(!avUsed.isInLife)return -9;
        int position = avUsed.getPosition();
        positionAvatar.get(position).remove(avUsed);
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

        positionAvatar.get(dest).add(avUsed);
        avUsed.setPosition(dest);
        try {
            lclient.get(avUsed).updateAvatar(avUsed);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return dest;

    }

    public void makeDamage(Avatar av, int damage){
        av.loseLife(damage);
        try {
            lclient.get(av).updateAvatar(av);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int escape(Avatar avUsed, int position, String goTo) {
        //On récupère l'avatar de la liste pour être sûr de manipuler le bon objet
        avUsed=getAvatar(avUsed);
        if (avUsed==null) return -10;
        //int res = move(avUsed, goTo);
        makeDamage(avUsed,2);

        /*for (int i=0;i<listAvatar.size();i++) {
            if (listAvatar.get(i).getName().contentEquals(avUsed.getName())) {
                listAvatar.remove(i);
            }
        }*/
        //listAvatar.add(avUsed);
        return 1;
    }

    public int attack(Entity enUsed, Integer position, int lifeLosed) {
        if (enUsed.getClass() == Avatar.class) {
            positionMonster.get(position).loseLife(lifeLosed);
            return positionMonster.get(position).getLifePoint();
        } else {
            makeDamage(positionAvatar.get(position).get(0),2);
            return positionAvatar.get(position).get(0).getLifePoint();
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

    public Avatar getAvatar(Avatar av){

        List<Avatar> lav = positionAvatar.get(av.getPosition());
        for (Avatar avatar : lav) {
            if(avatar.equals(av))
                return  avatar;
        }

        //Si on ne le trouves pas à la bonne position on le cherche dans les cases ajacentes
        int pos[]={(av.getPosition()+1 & 0xff)%(size*size),(av.getPosition()-1 & 0xff)%(size*size),(av.getPosition()+size & 0xff)%(size*size),(av.getPosition()-size & 0xff)%(size*size)};
        for (int i : pos) {
            if(positionAvatar.get(i).contains(av)){
                av.setPosition(i);
                Avatar avatar= getAvatar(av);
                avatar.setPosition(i);
                return avatar;
            }
        }

        //Si il n'existe pas on quitte
        if(positionAvatar.containsValue(av)) return null;

        //Sinon on cherche dans toute la grille
        for (Map.Entry<Integer, List<Avatar>> entry : positionAvatar.entrySet())
        {
            if(positionAvatar.get(entry.getKey()).contains(av)){
                av.setPosition(entry.getKey());
                Avatar avatar= getAvatar(av);
                avatar.setPosition(entry.getKey());
                return avatar;
            }
        }
        return null;

    }
}

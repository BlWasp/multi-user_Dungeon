package Server;

import Client.Avatar;
import Client.IPlayer;
import com.sun.security.ntlm.Client;

import java.awt.datatransfer.DataFlavor;
import java.rmi.RemoteException;
import java.util.*;

public class GameServerSimple implements Runnable{
    private int available;
    private Integer round;
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
        round=0;
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
        round=0;
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


    public synchronized int move(Avatar avUsed, String goTo) throws InterruptedException {
        Integer currentRound = round;
        avUsed=getAvatar(avUsed);
        if(avUsed==null) return -10;
        if(!avUsed.isInLife)return -9;
        int position = avUsed.getPosition();
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
        //permet d'attendre le prochain tour, pour synchroniser les tour de jeux
        if(round==currentRound)
            wait();
        positionAvatar.get(position).remove(avUsed);
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


    //Permet à un joueur d'attaquer au choix le monstre ou un autre joueur
    public synchronized int attackAvatar(Entity target, Avatar ifAvatar, Integer position, int lifeLosed) throws InterruptedException {
        Integer currentRound = round;
        if (target.getClass() == Avatar.class) {

            Avatar tmpAv = getAvatar(ifAvatar);
            if(round==currentRound)
                wait();
            makeDamage(tmpAv,lifeLosed);
            return target.getLifePoint();
        } else {
            if(round==currentRound)
                wait();
            positionMonster.get(position).loseLife(lifeLosed);
            return positionMonster.get(position).getLifePoint();
        }
    }

    public int attackMonster(Avatar target, int lifeLosed) {
        Avatar tmpAv = getAvatar(target);
        makeDamage(tmpAv,lifeLosed);
        return target.getLifePoint();
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

    @Override
    public void run() {
        while(available==1){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            addRound();

        }
    }

    public synchronized void addRound() {
        round++;
        System.out.println(round);
        notifyAll();
    }
}

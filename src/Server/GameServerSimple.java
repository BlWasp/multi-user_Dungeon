package Server;

import Client.Avatar;
import Client.IPlayer;
import com.sun.security.ntlm.Client;
import javafx.util.Pair;

import java.awt.datatransfer.DataFlavor;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Classe implémentant le code interne des différentes méthodes de GameServerImpl
 * L'utilité de certaines méthodes et leurs paramètres sont décrit dans GameServerImpl
 */
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
            positionMonster.put(i, new Monster("Galy",i));
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
        x=position/size;
        y=position%size;
        Room r = gGrid.getRoom(x,y);
        Integer dest;
        switch (goTo) {
            case "N" : dest = r.getNorth().dest; break;
            case "W" : dest = r.getWest().dest; break;
            case "E" : dest = r.getEast().dest; break;
            case "S" : dest = r.getSouth().dest; break;
            default : dest = -1; break;
        }
        //si la destination n'est pas disponible
        if(dest==-1)
            return -1;
        //Si le serveur ne gère pas la case
        if(dest<(Integer) z.getKey() || dest>(Integer) z.getValue()) {
            avUsed.setPosition(dest);
            return -2;
        }
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

    /**
     * Retire de la vie à un avatar et met à jour la vue du player
     * @param av
     *              Avatar à update
     * @param damage
     *              Quantité de vie perdue
     */
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
        try {
            int res = move(avUsed, goTo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        makeDamage(avUsed,2);
        return 1;
    }


    //Permet à un joueur d'attaquer un autre joueur
    public synchronized int attackAvatar(Avatar ifAvatar, Avatar attacker, int lifeLosed) throws InterruptedException {
        Integer currentRound = round;
        double nbAleatoire = Math.random();
        Avatar avVic = getAvatar(ifAvatar);
        Avatar avAtt = getAvatar(attacker);
        if (round == currentRound)
            wait();
        if (nbAleatoire == 0) { //Le joueur touche le joueur
            makeDamage(avVic, lifeLosed);
            return ifAvatar.getLifePoint();
        } else { //L'autre joueur a contré, l'attaquant se prend l'attaque
            makeDamage(avAtt, lifeLosed);
            return attacker.getLifePoint();
        }
    }

    //Lorsqu'un joueur attaque le monstre de la case
    public synchronized int attackM(Avatar attacker, Integer position, int lifeLosed) throws InterruptedException {
        Integer currentRound = round;
        double nbAleatoire = Math.random();
        if (round == currentRound)
            wait();
        if (nbAleatoire == 0) { //Le joueur touche le monstre
            positionMonster.get(position).loseLife(lifeLosed);
            return positionMonster.get(position).getLifePoint();
        } else { //Le monstre a contré
            Avatar avAtt = getAvatar(attacker);
            makeDamage(avAtt,lifeLosed);
            return  attacker.getLifePoint();
        }
    }

    public int attackMonster(Avatar target, Integer position, int lifeLosed) {
        Avatar tmpAv = getAvatar(target);
        double nbAleatoire = Math.random();
        if (nbAleatoire == 0) { //Le monstre touche
            makeDamage(tmpAv,lifeLosed);
            return target.getLifePoint();
        } else { //Le monstre ne touche pas
            positionMonster.get(position).loseLife(lifeLosed);
            return positionMonster.get(position).getLifePoint();
        }
    }



    public void displayGameInfo() {

    }

    /**
     * Récupère la position d'un avatar
     * @param av
     *              Avatar voulu
     * @return
     */
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

    /**
     * permet de retouver un avatar dans la grille
     * @param av
     * avatar à recherché
     * @return
     * l'avatar avec sa position mise à jour
     */
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
        notifyAll();
    }

    public Pair<Room, Entity> getRoomInfo(Entity avatar){
        avatar = getAvatar((Avatar) avatar);
        Pair<Room,Entity> res = new Pair<>(gGrid.getRoom(avatar.getPosition()/gGrid.size,avatar.getPosition()%gGrid.size),positionMonster.get(avatar.getPosition()));
        return res;
    }
}

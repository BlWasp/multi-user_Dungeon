package Server;

import Client.Avatar;
import Client.IPlayer;
import DataBase.DataBaseLink;
import com.sun.security.ntlm.Client;
import javafx.util.Pair;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.awt.datatransfer.DataFlavor;
import java.rmi.RemoteException;
import java.util.*;

import static Tools.Text.printE;
import static Tools.Text.printS;

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
    private Set<Entity> updateRequest;
    private Set<Integer> needRes;
    private Map<Integer, List<Avatar>> positionAvatar;
    Map<Integer, Monster> positionMonster;
    private Map<Avatar, IPlayer> lclient = new LinkedHashMap<>();
    private DataBaseLink dbl = new DataBaseLink();

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
        updateRequest = new HashSet<Entity>();
        needRes = new HashSet<Integer>();
        available=1;
        this.z = z;
        Integer monsterLife;
        connectDB();
        for (int i = 0; i < size*size; i++) {
            positionAvatar.put(i, new ArrayList<Avatar>());

            //Les monstres sont déjà enregistrés dans la BD
            if (!searchDB("Place","Monster","Place", String.valueOf(i)).
                    matches("Erreur lecture base de données")) {
                monsterLife = Integer.parseInt(searchDB("MaxLifePoint","Monster",
                        "Place", String.valueOf(i)));
                positionMonster.put(i, new Monster("Chuck",i,monsterLife));
                if (Integer.parseInt(searchDB("Life","Monster",
                        "Place", String.valueOf(i)))==0) {
                    //System.out.println(i);
                    needRes.add(i);
                }

            } else{ //Aucun monstre n'est enregistré dans la BD
                int line = i/size;
                int column = i%size;
                positionMonster.put(i, new Monster("Chuck",i,line*10+column*5));
                insertDB("("+String.valueOf(i)+","+positionMonster.get(i).getLifePoint().toString()+","
                                +positionMonster.get(i).getMaxLifePoint().toString()+")","Monster");
            }
        }
        round=0;
        gGrid.displayGrid();
    }

    public GameServerSimple(int state){
        available=state;
    }


    /**
     * Connexion au serveur MySQL
     */
    public void connectDB(){
        dbl.connectDB();
    }

    /**
     * Insertion de nouvelles données dans la BD
     * @param datas
     *          La donnée à insérer
     * @param table
     *          La table où insérer
     */
    public void insertDB(String datas, String table) {
        dbl.insertDB(datas,table);
    }

    /**
     * Mise à jour d'une donnée dans la BD
     * @param data1
     *          La donnée que l'on veut modifier
     * @param data2
     *          La nouvelle valeur de la donnée
     * @param table
     *          La table où se trouve la donnée
     * @param option1
     *          Donnée qui sert d'indice dans la table pour savoir où modifier la donnée (après le WHERE)
     * @param option2
     *          Valeur de l'indice voulu
     */
    public void updateDB(String data1, String data2, String table, String option1, String option2) {
        String datas = data1 + "=" + data2;
        String options = option1 + "=" + option2;
        dbl.updateDB(datas,table,options);
    }

    /**
     * Recherche dans la BD
     * @param datas
     *          Donnée dont on veut la valeur - colonne a récupérer
     * @param table
     *          Table où elle se trouve
     * @param option1
     *
     * @param option2
     */
    public String searchDB(String datas, String table, String option1, String option2) {
        String options = option1 + "=" + option2;
        return dbl.searchDB(datas,table,options);
    }

    public String multiSearchDB(String datas, String table, String option1, String option2) {
        String options = option1 + "=" + option2;
        return dbl.multiSearchDB(datas,table,options);
    }

    public String preConnection(String username) throws RemoteException {
        if (username.compareTo(searchDB("UsernamePl", "Player", "UsernamePl",
                "\"" + username + "\"")) == 0) {
            return multiSearchDB("UsernameAv","Avatar","UsernamePl",
                    "\""+username+"\"");
        }
        return "";
    }

    /**
     * Gère la connexion d'un joueur au serveur. Vérifie si le joueur est nouveau dans la BD ou déjà existant.
     * Et le cas échéant, il vérifie si l'avatar demandé existe déjà ou pas
     * @param avUsed
     *          Avatar demandé
     * @param position
     *          Position de l'avatar
     * @param player
     *          Interface du joueur qui se connecte
     * @return
     */
    public Avatar connection(Avatar avUsed, Integer position, IPlayer player) {
        if(available==0)
            return null;
        if(lclient.containsKey(avUsed))return null;
        try {
            int found = 0;
            //Si ce joueur existe déjà dans la BD
            if (player.getUid().compareTo(searchDB("UsernamePl", "Player", "UsernamePl",
                    "\""+player.getUid()+"\"")) == 0) {
                //Si l'avatar existe déjà dans la BD
                String avatars[] = multiSearchDB("UsernameAv", "Avatar", "UsernamePl",
                        "\""+player.getUid()+"\"").split(" ");
                for (int i=0; i<avatars.length;i++) {
                    if (avUsed.getName().compareTo(avatars[i]) == 0) {
                        position = Integer.parseInt(searchDB("Position","Avatar","UsernameAv",
                                "\""+avUsed.getName()+"\""));
                        avUsed.setPosition(position);
                        avUsed.setLifePoint(Integer.parseInt(searchDB("Life","Avatar","UsernameAv",
                                "\""+avUsed.getName()+"\"")));
                        avUsed.setMaxLifePoint(Integer.parseInt(searchDB("MaxLifePoint","Avatar","UsernameAv",
                                "\""+avUsed.getName()+"\"")));

                        found = 1;
                        System.out.println("already exist");
                    }
                }
                if (found == 0) { //Si l'avatar n'existe pas encore
                    insertDB("("+"\""+avUsed.getName()+"\""+","+"\""+player.getUid()+"\""+
                                    ","+"\""+position.toString()+"\""+","+"\""+avUsed.getLifePoint().toString()+"\""+","
                                    +"\""+avUsed.getMaxLifePoint().toString()+"\""+")","Avatar");
                    avUsed.setPosition(position);
                    System.out.println("create avatar");
                }

            } else { //Si le joueur n'existe pas encore
                insertDB("("+"\""+player.getUid()+"\""+","+"\""+"mdpTest"+"\""+")",
                        "Player");
                insertDB("("+"\""+avUsed.getName()+"\""+","+"\""+player.getUid()+"\""+","+"\""+position.toString()+"\""+
                                ","+"\""+avUsed.getLifePoint().toString()+"\""+","+"\""+avUsed.getMaxLifePoint().toString()+"\""+")",
                        "Avatar");
                avUsed.setPosition(position);
                System.out.println("player doesn't exist");
            }
        } catch(RemoteException e) {
            e.printStackTrace();
        }
        positionAvatar.get(position).add(avUsed);
        lclient.put(avUsed,player);
        return avUsed;
    }


    public synchronized int move(Avatar avUsed, String goTo) throws InterruptedException {
        Integer currentRound = round;
        avUsed=getAvatar(avUsed);
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
        //permet d'attendre le prochain tour, pour synchroniser les tours de jeux
        if(round==currentRound)
            wait();
        positionAvatar.get(position).remove(avUsed);
        positionAvatar.get(dest).add(avUsed);
        avUsed.setPosition(dest);
        updateRequest.add(avUsed);
        //updateDB("Position",dest.toString(),"Avatar","UsernameAv", "\""+avUsed.getName()+"\"");
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
            updateRequest.add(av);
            //updateDB("Life",av.getLifePoint().toString(),"Avatar","UsernameAv", "\""+av.getName()+"\"");
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
            if (res >= 0 || res == -3) {
                printS(avUsed.getName()+": fuit");
                makeDamage(avUsed,2);
                printS("Votre vie est maintenant de : " + avUsed.getLifePoint());
                return res;
            } else if (res == -1) {
                printE("Impossible de fuir dans un mur !");
            } else if (res == -2) {
                printE("Case non gérée par le serveur, fuite impossible");
            }
            return res;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }


    //Permet à un joueur d'attaquer un autre joueur
    public synchronized int attackAvatar(Avatar ifAvatar, Avatar attacker, int lifeLosed) throws InterruptedException {
        Integer currentRound = round;
        if(!ifAvatar.isInLife())
            return -1;
        if(!attacker.isInLife())
            return -2;
        double nbAleatoire = Math.random();
        int nbRandom = (int) (nbAleatoire*100);
        Avatar avVic = getAvatar(ifAvatar);
        Avatar avAtt = getAvatar(attacker);
        if (round == currentRound)
            wait();
        if ((nbRandom % 2)==0) { //Le joueur touche le joueur
            makeDamage(avVic, lifeLosed);
            return ifAvatar.getLifePoint();
        } else { //L'autre joueur a contré, l'attaquant se prend l'attaque
            makeDamage(avAtt, lifeLosed);
            return attacker.getLifePoint();
        }
    }

    /**
     * Permet au joueur d'attaquer le monstre de la salle
     * @param attacker
     * avatar attaquant
     * @param position
     * position de l'avatar
     * @return
     * 0 si le monstre subit des dégats
     * 1 si le monstre contre l'attaque
     * @throws InterruptedException
     */
    public synchronized int attackM(Avatar attacker, Integer position) throws InterruptedException, RemoteException {
        Integer currentRound = round;
        Integer lifeLosed;
        if(!positionMonster.get(position).isInLife())
            lclient.get(attacker).fightMessage(7,0);
        if(!attacker.isInLife())
            lclient.get(attacker).fightMessage(8,0);
        double nbAleatoire = Math.random();
        int nbRandom = (int) (nbAleatoire*100);
        double nbAleatoire2 = Math.random();
        int nbRandom2 = (int) (nbAleatoire2*100);
        System.out.println(": "+nbRandom+" "+nbRandom2);
        if(nbRandom<20){
            lifeLosed=3; //coup ciritique
            if(nbRandom2>90 && nbRandom<10) lifeLosed=1000; //coup fatale
        }
        else {
            lifeLosed=1;
        }
        if (round == currentRound)
            wait();
        if ((nbRandom % 2)==0) { //Le joueur touche le monstre
            positionMonster.get(position).loseLife(lifeLosed);
            updateRequest.add(positionMonster.get(position));

            if (lifeLosed<3)
                lclient.get(attacker).fightMessage(0,lifeLosed);
            else if(lifeLosed<1000)
                lclient.get(attacker).fightMessage(1,lifeLosed);
            else
                lclient.get(attacker).fightMessage(2,lifeLosed);

            //updateDB("Life",positionMonster.get(position).getLifePoint().toString(),"Monster","Place", position.toString());
            if(!positionMonster.get(position).isInLife()) { //le monstre se fait tuer

                for (Avatar av : positionAvatar.get(position)) {
                    av.levelUp();
                    try {
                        lclient.get(av).updateAvatar(av);
                        updateRequest.add(av);
                    }
                    catch (Exception e){
                        System.out.println("client injoignable");

                    }
                }
                lclient.get(attacker).fightMessage(6,0);
            }

            return lifeLosed;
        } else { //Le monstre a contré
            Avatar avAtt = getAvatar(attacker);
            makeDamage(avAtt, lifeLosed);
            if (lifeLosed<3)
                lclient.get(attacker).fightMessage(3,lifeLosed);
            else if(lifeLosed<1000)
                lclient.get(attacker).fightMessage(4,lifeLosed);
            else
                lclient.get(attacker).fightMessage(5,lifeLosed);
            return  -lifeLosed;
        }
    }

    /**
     *Permet au monstre d'attaquer
     * @param target
     * @param position
     * @param lifeLosed
     * @return
     */
    public int attackMonster(Avatar target, Integer position, int lifeLosed) {
        Avatar tmpAv = getAvatar(target);
        double nbAleatoire = Math.random();
        if (nbAleatoire == 0) { //Le monstre touche
            makeDamage(tmpAv,lifeLosed);
            return target.getLifePoint();
        } else { //Le monstre ne touche pas
            positionMonster.get(position).loseLife(lifeLosed);
            updateRequest.add(positionMonster.get(position));
            //updateDB("Life",positionMonster.get(position).getLifePoint().toString(),"Monster","Place", position.toString());
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
                for(Entity ent : updateRequest) {
                    if(ent.getClass()==Avatar.class) {
                        updateDB("Life",ent.getLifePoint().toString(),"Avatar",
                                "UsernameAv", "\""+ent.getName()+"\"");
                        updateDB("Position",ent.getPosition().toString(),"Avatar",
                                "UsernameAv", "\""+ent.getName()+"\"");
                        updateDB("MaxLifePoint",ent.getMaxLifePoint().toString(), "Avatar",
                                "UsernameAv", "\""+ent.getName()+"\"");
                    } else{
                        updateDB("Life",ent.getLifePoint().toString(),"Monster",
                                "Place", ent.getPosition().toString());
                        if (!ent.isInLife()) {
                            needRes.add(ent.getPosition());
                        }
                    }
                }
                updateRequest.clear();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            addRound();

        }
    }

    public synchronized void addRound() {
        round++;
        if (round%5==0) {
            for (int pos : needRes) {
                positionMonster.get(pos).restoreLife();
                positionMonster.get(pos).isInLife = true;
                updateRequest.add(positionMonster.get(pos));
            }
            needRes.clear();
        }
        notifyAll();
    }

    public Pair<Room, Entity> getRoomInfo(Entity avatar){
        avatar = getAvatar((Avatar) avatar);
        Pair<Room,Entity> res = new Pair<>(gGrid.getRoom(avatar.getPosition()/gGrid.size,avatar.getPosition()%gGrid.size),positionMonster.get(avatar.getPosition()));
        return res;
    }

    public void disconnection(Avatar av, IPlayer player) throws RemoteException{
        Avatar avUsed=getAvatar(av);
        int position = avUsed.getPosition();
        positionAvatar.get(position).remove(av);
        System.out.println(positionAvatar.toString());
        lclient.remove(av);
        updateDB("Life",av.getLifePoint().toString(),"Avatar","UsernameAv",
                "\""+av.getName()+"\"");
        updateDB("Position",av.getPosition().toString(),"Avatar","UsernameAv",
                "\""+av.getName()+"\"");
        updateDB("MaxLifePoint",av.getMaxLifePoint().toString(),"Avatar","UsernameAv",
                "\""+av.getName()+"\"");
    }


    public void playerAvatar(String username) throws RemoteException{
        dbl.searchAvatarDB("\""+username+"\"");
    }

    public int heal(Avatar av) throws InterruptedException {
        Avatar avUsed = getAvatar(av);
        if(positionMonster.get(avUsed.getPosition()).isInLife()){
            return -1;
        }
        int res = avUsed.heal(1);
        if(res==-1)
            return -2;
        Thread.sleep(2000);
        try {
            lclient.get(avUsed).updateAvatar(avUsed);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        updateRequest.add(avUsed);
        return 0;
    }
}

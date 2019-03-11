package Client;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import Server.*;
import Server.Server_Interface.IChatServer;
import Server.Server_Interface.IGameServer;
import Server.Server_Interface.IServerController;
import javafx.util.Pair;

import static Tools.Colors.*;
import static Tools.Text.*;
import static Tools.Way.allWay;
import static Tools.Way.isAWay;

/**
 * Classe représentant le client.
 * Permet de faire effectuer des actions à l'avatar sur le serveur
 */
public class Player extends UnicastRemoteObject implements IPlayer, Serializable {
    private String uid;
    private IServerController mainServer;
    private IGameServer obj;
    private IChatServer cs;
    private Avatar av;
    private OrderProcessor op;
    private DisplayManager dm;


    /**
     * Constructeur d'un Player (client)
     * @param av
     *          Son avatar
     * @throws RemoteException
     */
    public Player(Avatar av, String uid) throws RemoteException {
        super();
        this.av=av;
        this.uid = uid;
        dm = new DisplayManager(av);
    }

    public DisplayManager getDm() {
        return dm;
    }

    public IServerController getMainServer() {
        return mainServer;
    }

    public IGameServer getObj() {
        return obj;
    }

    public IChatServer getCs() {
        return cs;
    }

    /**
     * Renvoie l'UID du joueur. Est exploité lors de la connexion au serveur
     * @return
     * @throws RemoteException
     */
    public String getUid() throws RemoteException {
        return uid;
    }

    public void disconnection(Avatar av, Player p) throws RemoteException {
        obj.disconnection(av, p);
        cs.disconnection(av, p);
        getDm().clearScreen();
        bye();
    }

    public int moveAvatarCs(Avatar av, int position, IChatServer chatServer, Player p) throws RemoteException {
        Integer res = cs.moveTo(av, position);
        if(res!=0){
            System.out.println("changement de serveur de chat");
            cs = mainServer.findChatServer(res);
            if(cs==null){
                System.out.println("aucun serveur trouvé");
                return -1;
            }
            p.cs =cs;
            p.op.setChatserver(cs);
            cs.connection(av, av.getPosition(), p);
            res = moveAvatarCs(av, position,cs,p);
        }
        // System.out.println("Vous êtes arrivé sur la case n°" + av.getPosition());
        return 0;
    }

    /**
     * Permet de déplacer l'avatar sur une case adjacente
     * @param av
     *          Son avatar
     * @param way
     *          La direction du déplacement
     * @param gameServer
     *          L'identifiant du serveur de jeu où se trouve l'avatar
     * @return
     * @throws RemoteException
     */
    public int moveAvatar(Avatar av, String way, IGameServer gameServer, IChatServer chatServer, Player p) throws RemoteException {
        while(!isAWay(way)){
            printE("Direction invalide");
            printI("Entrez une direction valide : "+allWay());
            Scanner scan = new Scanner(System.in);
            way=scan.nextLine().toUpperCase();
        }
        int res = gameServer.move(av, way);
        while (res == -3) {
            System.out.println("Déplacement impossible, il y a un monstre dans la salle.");
            System.out.println("Pour quitter cette salle il vous faut occire ce monstre ou bien fuir comme un pleutre.");
            //demander où aller
            Scanner scan = new Scanner(System.in);
            String answer = scan.nextLine();
            try {
                res = op.process(op.spliter(answer), av);
            } catch (InterruptedException e) {
                System.out.println("orderProcessor InterruptesException");
                e.printStackTrace();
            }
            System.out.println(res);
            return -1;
        }
        while (res == -1) {
            System.out.println("Il y a pas moyen de passer par là");
            System.out.println("Dites-nous vers où vous voulez aller.");
            //demander où aller
            Scanner scan = new Scanner(System.in);
            String answer = scan.nextLine();
            try {
                res = op.process(op.spliter(answer), av);
            } catch (InterruptedException e) {
                System.out.println("orderProcessor InterruptesException");
                e.printStackTrace();
            }
            //System.out.println(res);
            return -1;
        }
        if (res == -2) {
            System.out.println("Case non géré par le serveur");
            obj = mainServer.findGameServer(av.getPosition(), way);
            if (obj == null) {
                System.out.println("aucun serveur trouvé");
                return -2;
            }
            obj.connection(av, av.getPosition(), p);
            return moveAvatar(av, way, obj, chatServer, p);
        }
        //moveAvatarCs(av, res, chatServer, p);
        // System.out.println("Vous êtes arrivé sur la case n°" + av.getPosition());
        return 0;
    }


    /**
     * Permet au joueur de s'échapper pendant un combat à allant sur une case adjacente
     * Il perd de la vie en le faisant
     * @param av
     *          Son avatar
     * @param way
     *          La direction de l'échappatoire
     * @param gameServer
     *          L'identifiant du serveur où se trouve l'avatar
     * @return
     * @throws RemoteException
     */
    //Permet au joueur de s'échapper pendant un combat
    //Pareil que moveAvatar mais affecte un malus de -2 pt à l'avatar
    public int escapeAvatar(Avatar av, String way, IGameServer gameServer, IChatServer cs, Player p) throws RemoteException {

        while(!isAWay(way)){
            printE("Direction invalide");
            printI("Entrez une direction valide : "+allWay());
            Scanner scan = new Scanner(System.in);
            way=scan.nextLine().toUpperCase();
        }
        int res = gameServer.escape(av, way);
        while(res==-1){
            System.out.println("Il y a pas moyen de passer par là");
            System.out.println("Dites-nous vers où vous voulez aller.");
            //demander où aller
            Scanner scan = new Scanner(System.in);
            String answer=scan.nextLine();
            try {
                res = op.process(op.spliter(answer), av);
            } catch (InterruptedException e) {
                System.out.println("orderProcessor InterruptesException");
                e.printStackTrace();
            }
            //System.out.println(res);
            return -1;
        }
        if(res==-2){
            System.out.println("Case non géré par le serveur");
            obj = mainServer.findGameServer(av.getPosition(),way);
            if(obj==null){
                System.out.println("Aucun serveur trouvé");
                return -2;
            }
            obj.connection(av, av.getPosition(), p);
            return escapeAvatar(av, way, obj, cs,p);
        }
        //moveAvatarCs(av,res,cs,p);
        // System.out.println("Vous êtes arrivé sur la case n°" + av.getPosition());
        return 0;



        //int res = 0;
        /*res = moveAvatar(av,way,gameServer, cs,p);
        if(res>-1)*/
            //System.out.println(av.getName()+": fuit");
        /*res = gameServer.escape(av,way);
        System.out.println(res);*/
        //System.out.println("Votre vie est maintenant de : " + av.getLifePoint());

        //return av.getLifePoint();
    }


    /**
     * Permet à un joueur d'attaquer un autre joueur sur la même case
     * @param ifAvatar
     *              C'est l'avatar qui est attaqué
     * @param attacker
     *              C'est l'avatar attaquant
     * @param gameServer
     *              C'est le serveur où se trouve les avatars
     * @param power
     *              C'est la puissance de l'attaque
     * @throws RemoteException
     */
    //target est la cible qui est attaquée
    //ifAvatar est utilisé lorsqu'un joueur en attaque un autre. Il spécifie ici lequel il attaque, sinon à null
    private void attackAvatar (Avatar ifAvatar, Avatar attacker, IGameServer gameServer, int power) throws RemoteException {
        gameServer.attackAvatar(ifAvatar,attacker,power);
        System.out.println("Petite attaque de derrière les fagots !");
    }

    /**
     * Permet à un joueur d'attaquer un monstre
     * @param attacker
     *              C'est le joueur attaquant
     * @param position
     *              C'est la position actuelle du joueur (sa case)
     * @param gameServer
     *              Serveur sur lequel se trouve le joueur actuellement
     * @param power
     *              C'est la puissance de l'attaque
     * @throws RemoteException
     */
    private void attackM (Avatar attacker, Integer position, IGameServer gameServer, int power) throws RemoteException {
        gameServer.attackM(attacker, position, power);
        System.out.println("Petite attaque sur le monstre en mode ninja !");
    }


    public static void main(String args[]) {
        try {
            welcome();
            Scanner scan = new Scanner(System.in);
            printI("Enter your player name :");
            String username=scan.next();
            printI("Enter your avatar's name :");
            String name=scan.next();
            Avatar avTest = new Avatar(name);
            //Avatar avBis = new Avatar("Pong");

            Player p = new Player(avTest,username);
            //Player p2 = new Player(avBis);
            // Récupération d'un proxy sur l'objet
            //IGameServer obj = (IGameServer) Naming.lookup("//localhost/Dungeon");
            p.mainServer = (IServerController) Naming.lookup("//localhost/Dungeon");
            p.obj = p.mainServer.findGameServer(0);
            if(p.obj==null){
                printE("Aucun serveur trouvé");
                return;
            }
            if(p.obj.connection(p.av, 0,p)==1) {
                avTest.setPosition(0);
                printS("Connected");
            }
            else
                printE("Connection failed");

            p.cs = p.mainServer.findChatServer(0);
            if(p.cs==null){
                printE("Aucun serveur de chat trouvé");
                return;
            }
            if(p.cs.connection(p.av, 0, p)==1) {
                avTest.setPosition(0);
                printS("Connected");
            }
            else
                System.out.println(red("Connection failed"));
            p.op=new OrderProcessor(p);
            p.getDm().displayPosition(p.getObj(),p.getCs());
            String answer=scan.nextLine();
            int play = 0;
            while(play == 0) {
                answer=scan.nextLine();
                play = p.op.process(p.op.spliter(answer), p.av);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet d'effectuer une mise à jour de l'état de l'avatar vu par le Player, après une modification sur le serveur
     * @param avatar
     *              Avatar du joueur
     * @throws RemoteException
     */
    @Override
    public void updateAvatar(Avatar avatar) throws RemoteException {
        if(!avatar.getPosition().equals(av.getPosition())){
            dm.displayPosition(getObj(),getCs());
            System.out.println(avatar.getName()+": nouvelle position = "+avatar.getPosition());

        }
        if (avatar.getLifePoint()!=av.getLifePoint()){
            System.out.println(avatar.getName()+": point de vie = "+avatar.getLifePoint());
        }
        this.av=avatar;
    }

    /**
     *
     * @param attacked
     * @param attacker
     * @throws RemoteException
     */
    /**
     * Permet de savoir si l'on est attaqué ou soigné
     * @param attacked
     * celui qui est ciblé
     * @param attacker
     * celui qui génère l'évènement
     * @throws RemoteException
     */
    @Override
    public void underAttack(Avatar attacked, Entity attacker) throws RemoteException {
        String attackedName = attacked.getName();
        String attackerName = attacker.getName();
        Integer damage = av.getLifePoint()-attacked.getLifePoint();
        if (damage>0)
            System.out.println(attackedName+" : se fait attaquer par "+attackerName+" et perd "+damage+" PV.");
        else
            System.out.println(attackedName+" : se fait soigner par "+attackerName+" et gagne "+damage*-1+" PV.");
        this.updateAvatar(attacked);
    }

    /**
     * Permet de recevoir les messages des joueur présent sur la même case
     * @param sender
     * Emméteur du message
     * @param message
     * Contenu du message
     * @throws RemoteException
     */
    @Override
    public void receiveMessage(Avatar sender, String message) throws RemoteException{
        System.out.println(cyan(sender.getName())+purple(" : ")+message);
    }

    /**
     * Permet au serveur de vérifier si le client est toujours joignable
     * @return
     * retourne 1 s'il est joignable -> génère une exception sinon (cote serveur)
     * @throws RemoteException
     */
    @Override
    public int ping() throws RemoteException {
        return 1;
    }
}
//intro, cas d'utilisation, diagramme de sequence, diagramme de classe (diagramme de composant), explication design pattern + explication générale (pk on le fait comme ca)
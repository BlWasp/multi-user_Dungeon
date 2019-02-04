package Client;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import Server.*;

/**
 * Classe représentant le client.
 * Permet de faire effectuer des actions à l'avatar sur le serveur
 */
public class Player extends UnicastRemoteObject implements IPlayer, Serializable {
    private String uid = "Rmi31";
    private IServerController mainServer;
    private IGameServer obj;
    private IChatServer cs;
    private Avatar av;
    private OrderProcessor op;


    /**
     * Constructeur d'un Player (client)
     * @param av
     *          Son avatar
     * @throws RemoteException
     */
    public Player(Avatar av) throws RemoteException {
        super();
        this.av=av;
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
    public int moveAvatar(Avatar av, String way, IGameServer gameServer) throws RemoteException {
        int res = gameServer.move(av, way);
        while(res==-1){
            System.out.println("Il y a pas moyen de passer par là");
            System.out.println("Dites-nous vers où vous voulez aller.");
            //demander où aller
            res = gameServer.move(av, way);
        }
        if(res==-2){
            System.out.println("Case non géré par le serveur");
            obj = mainServer.findGameServer(av.getPosition(),way);
            if(obj==null){
                System.out.println("aucun serveur trouvé");
                return -3;
            }
            return moveAvatar(av, way, obj);
        }
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
    public int escapeAvatar(Avatar av, String way, IGameServer gameServer) throws RemoteException {
        int res = moveAvatar(av,way,gameServer);
        if(res>-1)
            System.out.println(av.getName()+": fuit");
        if(res>=0) gameServer.escape(av,way);
        //System.out.println("Votre vie est maintenant de : " + av.getLifePoint());

        return av.getLifePoint();
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
            Avatar avTest = new Avatar("Ping");
            Avatar avBis = new Avatar("Pong");

            Player p = new Player(avTest);
            Player p2 = new Player(avBis);

            // Récupération d'un proxy sur l'objet
            //IGameServer obj = (IGameServer) Naming.lookup("//localhost/Dungeon");
            p.mainServer = (IServerController) Naming.lookup("//localhost/Dungeon");
            p.obj = p.mainServer.findGameServer(0);
            if(p.obj==null){
                System.out.println("Aucun serveur trouvé");
                return;
            }
            if(p.obj.connection(p.av, 0,p)==1) {
                avTest.setPosition(0);
                System.out.println("Connected");
            }
            else
                System.out.println("Connection failed");

            p2.mainServer = (IServerController) Naming.lookup("//localhost/Dungeon");
            p2.obj = p2.mainServer.findGameServer(0);
            if(p2.obj==null){
                System.out.println("Aucun serveur trouvé");
                return;
            }
            if(p2.obj.connection(p2.av, 0,p2)==1) {
                avBis.setPosition(0);
                System.out.println("Connected");
            }
            else
                System.out.println("Connection failed");
            //connection au serveur de chat
            p.cs = p.mainServer.findChatServer(0);
            if(p.cs==null){
                System.out.println("Aucun serveur de chat trouvé");
                return;
            }
            if(p.cs.connection(p.av, 0)==1) {
                avTest.setPosition(0);
                System.out.println("Connected");
            }
            else
                System.out.println("Connection failed");
            p2.cs = p2.mainServer.findChatServer(0);
            if(p2.cs==null){
                System.out.println("Aucun serveur de chat trouvé");
                return;
            }
            if(p2.cs.connection(p2.av, 0)==1) {
                avTest.setPosition(0);
                System.out.println("Connected");
            }
            else
                System.out.println("Connection failed");
            //fin connection

          /*  p.attackAvatar(avBis,avTest,p.obj,1);
            p.attackM(avBis,avBis.getPosition(),p.obj,1);

            p.escapeAvatar(p.av,"S", p.obj);
            p2.moveAvatar(p2.av, "S",p2.obj);
            //avTest.setPosition(avTest.getPosition()+1);
            p.escapeAvatar(p.av,"S", p.obj);
            //escapeAvatar(avTest,"S", obj);
            p.escapeAvatar(p.av,"S", p.obj);
            //escapeAvatar(avTest,"S", obj);
            //escapeAvatar(avTest, "S", obj);*/
            p.op=new OrderProcessor(p);
            Scanner scan = new Scanner(System.in);

            while(true) {
                String answer=scan.nextLine();
                p.op.process(p.op.spliter(answer), p.av);
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
    //a utiliser dans le futur pour les attaques. A discuter avec Guillaume
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
}
//intro, cas d'utilisation, diagramme de sequence, diagramme de classe (diagramme de composant), explication design pattern + explication générale (pk on le fait comme ca)
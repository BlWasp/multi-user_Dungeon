package Client;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import Server.Entity;
import Server.IChatServer;
import Server.IGameServer;
import Server.IServerController;

public class Player extends UnicastRemoteObject implements IPlayer, Serializable {
    private String uid = "Rmi31";
    private IServerController mainServer;
    private IGameServer obj;
    private IChatServer cs;
    private Avatar av;

    public Player(Avatar av) throws RemoteException {
        super();
        this.av=av;
    }

    private int moveAvatar(Avatar av, String way, IGameServer gameServer) throws RemoteException {
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


    //Permet au joueur de s'échapper pendant un combat
    //Pareil que moveAvatar mais affecte un malus de -2 pt à l'avatar
    private int escapeAvatar (Avatar av, String way, IGameServer gameServer) throws RemoteException {
        int res = moveAvatar(av,way,gameServer);
        if(res>-1)
            System.out.println(av.getName()+": fuit");
        if(res>=0) gameServer.escape(av,way);
        //System.out.println("Votre vie est maintenant de : " + av.getLifePoint());

        return av.getLifePoint();
    }


    //target est la cible qui est attaquée
    //ifAvatar est utilisé lorsqu'un joueur en attaque un autre. Il spécifie ici lequel il attaque, sinon à null
    private void attackAvatar (Entity target, Avatar ifAvatar, Integer position, IGameServer gameServer, int power) throws RemoteException {
        gameServer.attackAvatar(target,ifAvatar,position,power);
        System.out.println("Petite attaque de derrière les fagots !");
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

            p.attackAvatar(avBis,avBis,avTest.getPosition(),p.obj,1);

            p.escapeAvatar(p.av,"S", p.obj);
            p2.moveAvatar(p2.av, "S",p2.obj);
            //avTest.setPosition(avTest.getPosition()+1);
            p.escapeAvatar(p.av,"S", p.obj);
            //escapeAvatar(avTest,"S", obj);
            p.escapeAvatar(p.av,"S", p.obj);
            //escapeAvatar(avTest,"S", obj);
            //escapeAvatar(avTest, "S", obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

package Server;

import DataBase.DataBaseLink;
import Client.Avatar;
import Client.IPlayer;
import Server.Server_Interface.IGameServerManagement;
import Server.Server_Interface.IServerControllerServerSide;
import javafx.util.Pair;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Serveur de jeu
 * Il implémente l'interface RMI possédant toutes ses méthodes.
 * Le code interne de chaque méthode est décrit dans la classe GameServerSimple
 */
public class GameServerImpl extends UnicastRemoteObject implements IGameServerManagement {

    private static GameServerSimple gs;

    protected GameServerImpl() throws RemoteException {
        super();
    }

    /**
     * Permet de récuperer la liste des avatars pour un joueur donné
     * @param username
     * nom du joueur à rechercher
     * @return
     * @throws RemoteException
     */
    @Override
    public String preConnection(String username) throws RemoteException {
        return gs.preConnection(username);
    }

    /**
     * Connection d'un joueur sur le serveur
     * @param avUsed
     *              Avatar du joueur
     * @param position
     *              Position de l'avatar sur le plateau
     * @param player
     *              Identifiant du player
     * @return
     * @throws RemoteException
     */
    @Override
    public Avatar connection(Avatar avUsed, Integer position, IPlayer player) throws RemoteException{
        return gs.connection(avUsed, position, player);
    }

    /**
     * Méthode appelée lorsque le joueur s'échappe
     * @param avUsed
     *              Avatar du joueur
     * @param goTo
     *              Direction de l'échappatoire
     * @return
     * @throws RemoteException
     */
    @Override
    public int escape(Avatar avUsed, String goTo) throws RemoteException{
        int res = 0;
        avUsed=gs.getAvatar(avUsed);
        if(avUsed==null) return -10;
        if(!avUsed.isInLife())return -9;
        return gs.escape(avUsed, gs.getPosition(avUsed), goTo);
    }

    /**
     * Méthode appelée lorsque que le joueur déplace son avatar
     * @param avUsed
     *              Avatar du joueur
     * @param goTo
     *              Direction du déplacement
     * @return
     * @throws RemoteException
     */
    @Override
    public int move(Avatar avUsed, String goTo) throws RemoteException{
        int res = 0;
        avUsed=gs.getAvatar(avUsed);
        if(avUsed==null) return -10;
        if(!avUsed.isInLife())return -9;
        int position = avUsed.getPosition();
        Monster monster = gs.positionMonster.get(position);
        //S'il y a un monstre dans la salle
        if(monster.isInLife())
            return -3;
        try {
            res = gs.move(avUsed, goTo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return res;
    }


    /**
     * Méthode appelée lorsque le joueur attaque un autre joueur
     * @param ifAvatar
     *              Avatar à attaquer
     * @param attacker
     *              Avatar attaquant
     * @param lifeLosed
     *              Puissance de l'attaque
     * @return
     * @throws RemoteException
     */
    @Override //Attaque d'un joueur sur un autre joueur
    public int attackAvatar(Avatar ifAvatar, Avatar attacker, int lifeLosed) throws RemoteException {
        if(!attacker.isInLife) return -2;
        try {
            return gs.attackAvatar(ifAvatar, attacker, lifeLosed);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Méthode appelée lorsque le joueur attaque le monstre de la case
     * @param attacker
     *              Avatar attaquant
     * @param position
     *              Position actuelle de l'avatar du joueur
     * @param lifeLosed
     *              Puissance de l'attaque
     * @return
     * @throws RemoteException
     */
    @Override //Attaque d'un joueur sur un monstre
    public int attackM(Avatar attacker, Integer position, int lifeLosed) throws RemoteException {
        if(!attacker.isInLife()) return -2;
        try {
            return gs.attackM(attacker, position, lifeLosed);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Méthode appelée à chaque tour et permettant au monstre d'attaquer un avatar
     * @param target
     *              Avatar cible
     * @param position
     *              Position du monstre attaquant
     * @param lifeLosed
     *              Puissance de l'attaque
     * @return
     * @throws RemoteException
     */
    @Override //Attaque d'un monstre sur un joueur
    public int attackMonster(Avatar target, Integer position, int lifeLosed) throws RemoteException {
        return gs.attackMonster(target, position, lifeLosed);
    }


    /**
     * Affiche des informations sur la partie
     * @throws RemoteException
     */
    @Override
    public void displayGameInfo() throws RemoteException {
        gs.displayGameInfo();
    }

    @Override
    public void disconnection(Avatar av, IPlayer player) throws RemoteException{
        gs.disconnection(av, player);
    }

    /**
     * Permet de mettre la zone souhaitée à jour suite à des changements
     * @param z
     *              Zone à update
     * @throws RemoteException
     */
    @Override
    public void updateZone(Zone z) throws RemoteException {
        gs.setZ(z);
        System.out.println(gs.getZ());
    }

    /**
     * Récupère les avatars d'un player
     * @param username
     *          Nom du player
     * @throws RemoteException
     */
    public void playerAvatar(String username) throws RemoteException {
        gs.playerAvatar(username);
    }

    /**
     * Permet de heal un avatar
     * @param av
     *          L'avatar qu'il faut heal
     * @return
     * @throws RemoteException
     * @throws InterruptedException
     */
    @Override
    public int heal(Avatar av) throws RemoteException, InterruptedException {
        return gs.heal(av);
    }

    @Override
    public Pair<Room, Entity> getRoomInfo(Entity avatar) throws RemoteException{
        return gs.getRoomInfo(avatar);
    }


    public static void main(String args[]) throws Exception {
        //Démarre le rmiregistry
        //LocateRegistry.createRegistry(1099);
        GameServerImpl obj = new GameServerImpl();
        IServerControllerServerSide mainServer = (IServerControllerServerSide) Naming.lookup("//localhost/Dungeon");
        Pair<Grid,Zone> res = mainServer.gameServerConnection(obj);
        gs = new GameServerSimple(res.getKey(),res.getKey().size,res.getValue());
        System.out.println(gs.getZ().getKey()+ " " + gs.getZ().getValue());
        Thread thread = new Thread(gs);
        thread.start();
        //Naming.rebind("Dungeon", obj);
        System.out.println("Server.GameServerImpl launched");
    }

}

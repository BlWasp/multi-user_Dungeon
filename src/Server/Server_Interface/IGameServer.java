package Server.Server_Interface;

import Client.Avatar;
import Client.IPlayer;
import Server.Entity;
import Server.Room;
import Server.Zone;
import javafx.util.Pair;

import java.rmi.RemoteException;

/**
 * Interface RMI pour le serveur de jeu.
 * Le code de chaque méthode est dans GameServerSimple.
 * La méthode main du serveur et l'appel des méthodes est dans GameServerImpl
 * Méthodes dédiés aux clients
 */
public interface IGameServer extends java.rmi.Remote{
    //permet de lister les avatars disponibles pour un joueur
    String preConnection(String username) throws RemoteException;
    //permet la connection du joueur au server de jeu et de spécifier le personnage utilisé
    Avatar connection(Avatar avUsed, Integer position, IPlayer player) throws RemoteException;
    //permet au joueur de s'échaper d'une case
    int escape(Avatar avUsed, String goTo) throws RemoteException;
    int move(Avatar avUsed, String goTo) throws RemoteException;

    //permet de gérer les attaques des entités
    int attackAvatar(Avatar ifAvatar, Avatar attacker, int lifeLosed) throws RemoteException;
    int attackM(Avatar attacker, Integer position, int lifeLosed) throws RemoteException;
    int attackMonster(Avatar target, Integer position, int lifeLosed) throws RemoteException;

    void displayGameInfo() throws RemoteException;
    public void disconnection(Avatar av, IPlayer player) throws  RemoteException;

    //Récupère les avatars d'un joueur
    void playerAvatar(String username) throws RemoteException;
    public int heal(Avatar av) throws RemoteException, InterruptedException;

    Pair<Room, Entity> getRoomInfo(Entity avatar) throws RemoteException;
}

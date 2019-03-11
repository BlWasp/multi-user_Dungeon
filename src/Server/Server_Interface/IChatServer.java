package Server.Server_Interface;

import Client.Avatar;
import Client.IPlayer;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface RMI pour le serveur de chat, dédié aux clients
 */
public interface IChatServer extends java.rmi.Remote {
    public void speak(Avatar sender, String text) throws RemoteException;
    int connection(Avatar av, Integer position, IPlayer player) throws RemoteException;
    public int move(Avatar avUsed, String goTo) throws RemoteException;
    public int moveTo(Avatar avUsed, Integer position) throws RemoteException;
    public List<Avatar> getNeighbour(Avatar av) throws  RemoteException;
    public void disconnection(Avatar av, IPlayer player) throws RemoteException;
}

package Server;

import Client.Avatar;
import Client.IPlayer;

import java.rmi.RemoteException;

/**
 * Interface RMI pour le serveur de chat
 */
public interface IChatServer extends java.rmi.Remote {
    public void speak(Avatar sender, String text) throws RemoteException;
    void updateZone(Zone z) throws RemoteException;
    int connection(Avatar av, Integer position, IPlayer player) throws RemoteException;
}

package Server.Server_Interface;

import Server.Zone;
import javafx.util.Pair;

import java.rmi.RemoteException;

/**
 * Interface RMI du serveur controller, méthodes utilisables par les serveurs
 */
public interface IServerControllerServerSide extends java.rmi.Remote{
    public Pair gameServerConnection(IGameServerManagement serv) throws RemoteException;

    Pair chatServerConnection(IChatServerManagement serv) throws RemoteException;

    void gameServerDisconnection(Zone z) throws RemoteException;

    public void chatServerDisconnection(Zone serv) throws RemoteException;
}

package Server;

import javafx.util.Pair;

import java.rmi.RemoteException;

public interface IServerControllerServerSide extends java.rmi.Remote{
    public Pair gameServerConnection(IGameServerManagement serv) throws RemoteException;

    Pair chatServerConnection(IChatServerManagement serv) throws RemoteException;

    void gameServerDisconnection(Zone z) throws RemoteException;

    public void chatServerDisconnection(Zone serv) throws RemoteException;
}

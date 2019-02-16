package Server;

import javafx.util.Pair;

import java.rmi.RemoteException;

/**
 * Interface RMI du serveur controller, permettant de centraliser le contôle de tous les serveurs
 */
public interface IServerController extends java.rmi.Remote{
    public Pair gameServerConnection(IGameServerManagement serv) throws RemoteException;

    Pair chatServerConnection(IChatServerManagement serv) throws RemoteException;

    void gameServerDisconnection(Zone z) throws RemoteException;

    public void chatServerDisconnection(Zone serv) throws RemoteException;

    public IGameServer findGameServer(Integer position) throws RemoteException;
    public IGameServer findGameServer(Integer position, String way) throws RemoteException;
    public IChatServer findChatServer(Integer position) throws RemoteException;
    public IChatServer findChatServer(Integer position, String way) throws RemoteException;
}

package Server;

import javafx.util.Pair;

import java.rmi.RemoteException;

public interface IServerController extends java.rmi.Remote{
    public Pair gameServerConnection(IGameServer serv) throws RemoteException;

    Pair chatServerConnection(IChatServer serv) throws RemoteException;

    void gameServerDisconnection(Zone z) throws RemoteException;

    public void chatServerDisconnection(Zone serv) throws RemoteException;

    public IGameServer findGameServer(Integer position) throws RemoteException;
    public IGameServer findGameServer(Integer position, String way) throws RemoteException;
    public IChatServer findChatServer(Integer position) throws RemoteException;
    public IChatServer findChatServer(Integer position, String way) throws RemoteException;
}

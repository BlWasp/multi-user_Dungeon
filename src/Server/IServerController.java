package Server;

import javafx.util.Pair;

import java.rmi.RemoteException;

public interface IServerController extends java.rmi.Remote{
    public Pair gameServerConnection(IGameServer serv) throws RemoteException;
    public Pair chatServerConnection(IChatServer serv) throws RemoteException;
    public void serverDisconnection() throws RemoteException;
    public IGameServer findGameServer(Integer position) throws RemoteException;

    public IChatServer findChatServer(Integer position) throws RemoteException;

    IChatServer findChatServer(Integer position, String way) throws RemoteException;

    public IGameServer findGameServer(Integer position, String way) throws RemoteException;
}

package Server;

import java.rmi.RemoteException;

public interface IServerController extends java.rmi.Remote{
    public Grid gameServerConnection(IGameServer serv) throws RemoteException;
    public Grid chatServerConnection(IChatServer serv) throws RemoteException;
    public void serverDisconnection() throws RemoteException;
    public IGameServer findGameServer(Integer position) throws RemoteException;
}

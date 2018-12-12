package Server;

import Client.Avatar;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class GameServerImpl extends UnicastRemoteObject implements IGameServer{

    private GameServerSimple gs = new GameServerSimple();

    protected GameServerImpl() throws RemoteException {
        super();
    }

    @Override
    public int connection(String avName, Integer position) throws RemoteException{
        return gs.connection(avName, position);
    }

    @Override
    public void escape(Avatar avUsed, String goTo) throws RemoteException{
        gs.escape(avUsed, gs.getPosition(avUsed), goTo);
    }

    @Override
    public int move(Avatar avUsed, String goTo) throws RemoteException{
        int res = gs.move(avUsed.getName(), avUsed.getPosition(), goTo);
        if(res!=-1){
            System.out.println(res);
        }
        return res;
    }

    @Override
    public void displayGameInfo() throws RemoteException {
        gs.displayGameInfo();
    }

    public static void main(String args[]) throws Exception {
        // Démarre le rmiregistry
        //LocateRegistry.createRegistry(1099);
        GameServerImpl obj = new GameServerImpl();
        IServerController mainServer = (IServerController) Naming.lookup("//localhost/Dungeon");
        mainServer.gameServerConnection(obj);
        //Naming.rebind("Dungeon", obj);
       // System.out.println("Server.GameServerImpl launched");
    }

}

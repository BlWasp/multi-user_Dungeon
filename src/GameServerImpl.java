import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class GameServerImpl extends UnicastRemoteObject implements IGameServer {


    private GameServerSimple gs = new GameServerSimple();

    protected GameServerImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean connection(String uid, String avName) throws RemoteException{
        return gs.connection(uid, avName);
    }

    @Override
    public void escape(Avatar avUsed, String goTo) throws RemoteException{
        gs.escape(avUsed, gs.getPosition(avUsed), goTo);
    }

    @Override
    public void move(Avatar avUsed, String goTo) throws RemoteException{
        gs.move(avUsed, gs.getPosition(avUsed), goTo);
    }

    @Override
    public void displayGameInfo() throws RemoteException {
        gs.displayGameInfo();
    }

    public static void main(String args[]) throws Exception {
        // Démarre le rmiregistry
        LocateRegistry.createRegistry(1099);
        GameServerImpl obj = new GameServerImpl();
        Naming.rebind("Dungeon", obj);
        System.out.println("GameServerImpl launched");
    }
}

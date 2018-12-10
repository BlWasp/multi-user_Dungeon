import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class GameServer extends UnicastRemoteObject implements IGameServer {


    private GameServerImpl gs = new GameServerImpl();

    protected GameServer() throws RemoteException {
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
        GameServer obj = new GameServer();
        Naming.rebind("Dungeon", obj);
        System.out.println("GameServer launched");
    }
}

package Server;

import javafx.util.Pair;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerController extends UnicastRemoteObject implements IServerController {
    private Grid board;
    private int size = 8;
    private int nbRoomByServ;
    private int nbServ;
    //La première valeur du tableau représente la première case géré l'autre la dernière
    private Map<Zone,IGameServer> lgame = new TreeMap<>();
    private Map<Zone,IGameServer> lchat = new TreeMap<>();
    protected ServerController() throws RemoteException {
        super();
        nbServ=0;
        board = new Grid(size);
        board.displayGrid();
    }


    @Override
    public Pair gameServerConnection(IGameServer serv) throws RemoteException{
        //ajouter la gestion dynamique de la répartition des cases!!!
        Integer begin=0, end=(size*size)-1;
        Zone z = new Zone(begin,end);
        lgame.put(z,serv);
        Pair<Grid,Zone> res = new Pair<>(board,z);
        return res;
    }

    @Override
    public Grid chatServerConnection(IChatServer serv) throws RemoteException{
        return null;
    }

    @Override
    public void serverDisconnection() throws RemoteException{

    }

    @Override
    public IGameServer findGameServer(Integer position) throws RemoteException{
        Zone z = new Zone(0,(size*size)-1);
        return lgame.get(z);
    }

    @Override
    public IGameServer findGameServer(Integer position, String way) throws RemoteException {
        Zone z = new Zone(0,(size*size)-1);
        return lgame.get(z);
    }



    public static void main(String args[]) throws Exception {
        // Démarre le rmiregistry
        LocateRegistry.createRegistry(1099);
        ServerController obj = new ServerController();
        Naming.rebind("Dungeon", obj);
        System.out.println("Server.GameServerImpl launched");
    }
}

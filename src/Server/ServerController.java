package Server;

import javafx.util.Pair;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Serveur permettant de centraliser le contrôle de tous les serveurs du jeu.
 */
public class ServerController extends UnicastRemoteObject implements IServerController {
    private Grid board;
    private int size = 8;
    private int nbGameServ =0;
    private int nbChatServ =0;
    //La première valeur du tableau représente la première case géré l'autre la dernière
    private Map<Zone,IGameServer> lgame = new TreeMap<>();
    private Map<Zone,IChatServer> lchat = new TreeMap<>();
    protected ServerController() throws RemoteException {
        super();
        nbGameServ =0;
        nbChatServ =0;
        board = new Grid(size);
        board.displayGrid();
    }
    public Zone distributeZone(){
        Integer last=0, i=0;
        Map<Zone,IGameServer> tmp = new TreeMap<>(lgame);
        lgame.clear();
        Zone lastZ = new Zone(last,i);
        for (Map.Entry<Zone, IGameServer> pair : tmp.entrySet()) {
            i=i+(size*size)/ nbGameServ;
            //pour gérer nombre impair de serveur
            if(i==size*size-1) i++;
            Zone z = new Zone(last,i);
            lastZ=z;
            last=i+1;
            lgame.put(z,pair.getValue());
        }
        return lastZ;

    }

    public Zone distributeChatZone(){
        Integer last=0, i=0;
        Map<Zone,IChatServer> tmp = new TreeMap<>(lchat);
        lchat.clear();
        Zone lastZ = new Zone(last,i);
        for (Map.Entry<Zone, IChatServer> pair : tmp.entrySet()) {
            i=i+(size*size)/ nbChatServ;
            //pour gérer nombre impair de serveur
            if(i==size*size-1) i++;
            Zone z = new Zone(last,i);
            lastZ=z;
            last=i+1;
            lchat.put(z,pair.getValue());
        }
        return lastZ;

    }

    public void updateAllServ(IGameServer serv) throws RemoteException {
        for (Map.Entry<Zone, IGameServer> pair : lgame.entrySet()) {
            if(pair.getValue()!=serv)
                pair.getValue().updateZone(pair.getKey());
        }
    }

    public void updateAllChat(IChatServer serv) throws RemoteException {
        for (Map.Entry<Zone, IChatServer> pair : lchat.entrySet()) {
            if(pair.getValue()!=serv)
                pair.getValue().updateZone(pair.getKey());
        }
    }

    @Override
    public Pair gameServerConnection(IGameServer serv) throws RemoteException{
        //ajouter la gestion dynamique de la répartition des cases!!!
        nbGameServ++;
        lgame.put(new Zone(9999,9999),serv);
        Zone z = distributeZone();
        updateAllServ(serv);
        Pair<Grid,Zone> res = new Pair<>(board,z);
        return res;
    }

    @Override
    public Pair chatServerConnection(IChatServer serv) throws RemoteException{
        nbChatServ++;
        lchat.put(new Zone(9999,9999),serv);
        Zone z = distributeChatZone();
        updateAllChat(serv);
        Pair<Grid,Zone> res = new Pair<>(board,z);
        return res;
    }

    @Override
    public void gameServerDisconnection(Zone z) throws RemoteException {
        nbGameServ--;
        updateAllServ(lgame.get(z));
        lgame.remove(z);

    }


    @Override
    public void chatServerDisconnection(Zone z) throws RemoteException {
        nbChatServ--;
        updateAllChat(lchat.get(z));
        lchat.remove(z);

    }




    @Override
    public IGameServer findGameServer(Integer position) throws RemoteException{
        Zone z = new Zone(0,(size*size)-1);
        long i = 0;
        Zone res;
        for (Map.Entry<Zone, IGameServer> pair : lgame.entrySet()) {
            if((Integer) pair.getKey().getKey()<=position && (Integer) pair.getKey().getValue()>=position) {
                return lgame.get(pair.getKey());
            }
        }
        return null;
    }

    @Override
    public IChatServer findChatServer(Integer position) throws RemoteException{
        Zone z = new Zone(0,(size*size)-1);
        long i = 0;
        Zone res;
        for (Map.Entry<Zone, IChatServer> pair : lchat.entrySet()) {
            if((Integer) pair.getKey().getKey()<=position && (Integer) pair.getKey().getValue()>=position) {
                return lchat.get(pair.getKey());
            }
        }
        return null;
    }

    @Override
    public IChatServer findChatServer(Integer position, String way) throws RemoteException {
        Zone z = new Zone(0,(size*size)-1);
        long i = 0;
        Zone res;
        Integer x,y;
        x=position/size;
        y=position%size;
        Room r = board.getRoom(x,y);
        Integer dest;
        switch (way) {
            case "N" : dest = r.getNorth().dest; break;
            case "W" : dest = r.getWest().dest; break;
            case "E" : dest = r.getEast().dest; break;
            case "S" : dest = r.getSouth().dest; break;
            default : dest = -1; break;
        }
        for (Map.Entry<Zone, IChatServer> pair : lchat.entrySet()) {
            if((Integer) pair.getKey().getKey()<=dest&&(Integer) pair.getKey().getValue()>=dest) {
                return lchat.get(pair.getKey());
            }
        }
        return null;

    }

    @Override
    public IGameServer findGameServer(Integer position, String way) throws RemoteException {
        Zone z = new Zone(0,(size*size)-1);
        long i = 0;
        Zone res;
        Integer x,y;
        x=position/size;
        y=position%size;
        Room r = board.getRoom(x,y);
        Integer dest;
        switch (way) {
            case "N" : dest = r.getNorth().dest; break;
            case "W" : dest = r.getWest().dest; break;
            case "E" : dest = r.getEast().dest; break;
            case "S" : dest = r.getSouth().dest; break;
            default : dest = -1; break;
        }
        for (Map.Entry<Zone, IGameServer> pair : lgame.entrySet()) {
            if((Integer) pair.getKey().getKey()<=dest&&(Integer) pair.getKey().getValue()>=dest) {
                return lgame.get(pair.getKey());
            }
        }
        return null;

    }



    public static void main(String args[]) throws Exception {
        // Démarre le rmiregistry
        LocateRegistry.createRegistry(1099);
        ServerController obj = new ServerController();
        Naming.rebind("Dungeon", obj);
        System.out.println("Server.ServerController launched");

    }
}

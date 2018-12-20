package Server;

import Client.Avatar;
import javafx.util.Pair;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ChatServer  extends UnicastRemoteObject implements IChatServer{
    private int available;
    private Grid gGrid;
    private Zone z = new Zone(0,0);
    int size = 8;
    private Map<Integer, List<Avatar>> positionMap;
    private List<Avatar> listAvatar;

    public ChatServer() throws RemoteException{
        super();
        gGrid=new Grid(0);
        this.size = 0;
        positionMap = new LinkedHashMap<>();
        listAvatar = new LinkedList<>();
        available=0;
        this.z = new Zone(0,0);
    }
    public void setChatServer(Grid grid, int size, Zone z){
        gGrid=grid;
        this.size = size;
        available=1;
        this.z = z;
        for (int i = 0; i < size*size; i++) {
            positionMap.put(i, new ArrayList<Avatar>());
        }
        gGrid.displayGrid();
    }
    public ChatServer(Grid grid, int size, Zone z) throws RemoteException{
        super();
        gGrid=grid;
        this.size = size;
        positionMap = new LinkedHashMap<>();
        listAvatar = new LinkedList<>();
        available=1;
        this.z = z;
        for (int i = 0; i < size*size; i++) {
            positionMap.put(i, new ArrayList<Avatar>());
        }
        gGrid.displayGrid();
    }

    @Override
    public void speak(String uid, String text) throws RemoteException {

    }

    @Override
    public void updateZone(Zone z) throws RemoteException {
        this.z=z;
        System.out.println(this.getZ());
    }

    @Override
    public int connection(Avatar av, Integer position) throws RemoteException {
        if(available==0)
            return available;
        List<Avatar> user = positionMap.get(position);
        user.add(av);
        listAvatar.add(av);
        return available;
    }

    public Zone getZ() {
        return z;
    }

    public static void main(String args[]) throws Exception {
        // Démarre le rmiregistry
        //LocateRegistry.createRegistry(1099);
        ChatServer obj = new ChatServer();
        IServerController mainServer = (IServerController) Naming.lookup("//localhost/Dungeon");
        Pair<Grid,Zone> res = mainServer.chatServerConnection(obj);
        obj.setChatServer(res.getKey(),res.getKey().size,res.getValue());
        System.out.println(obj.getZ().getKey()+ " " + obj.getZ().getValue());

    }
}

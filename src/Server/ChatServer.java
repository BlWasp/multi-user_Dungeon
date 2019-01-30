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

    /**
     * Constructeur du ChatServer
     *
     * @throws RemoteException
     */
    public ChatServer() throws RemoteException{
        super();
        gGrid=new Grid(0);
        this.size = 0;
        positionMap = new LinkedHashMap<>();
        listAvatar = new LinkedList<>();
        available=0;
        this.z = new Zone(0,0);
    }

    /**
     * Constructeur utilisé pour créer un ChatServer configuré
     * @param grid
     * Grille utilisé par le chat server
     * @param size
     * Taille de la grille
     * @param z
     * Zone géré
     * @throws RemoteException
     */
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

    /**
     * Permet de mettre à jour le ChatServer
     * @param grid
     * nouvelle grille du chat server
     * @param size
     * Nombre de case à gérer
     * @param z
     * Zone de case à gérer
     */
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


    @Override
    public void speak(String uid, String text) throws RemoteException {

    }

    /**
     * Mets à jour la zone à gérer.
     * Survient lors de l'ajout ou la supression d'un serverchat dans la partie
     * @param z
     * Nouvelle zone à gérée
     * @throws RemoteException
     */
    @Override
    public void updateZone(Zone z) throws RemoteException {
        this.z=z;
        System.out.println(this.getZ());
    }

    /**
     * Permet à un joueur de se connecter au serveur de chat
     * @param av
     * avatar qui se connecte
     * @param position
     * position de l'avatar
     * @return
     * si le serveur est disponible ou non
     * @throws RemoteException
     */
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

    /**
     * création du chatServer,
     * Ajout du chat sur le registre rmi,
     * connexion au serverManager
     * gestion deconnexion
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        // Démarre le rmiregistry
        //LocateRegistry.createRegistry(1099);
        ChatServer obj = new ChatServer();
        IServerController mainServer = (IServerController) Naming.lookup("//localhost/Dungeon");
        Pair<Grid,Zone> res = mainServer.chatServerConnection(obj);
        obj.setChatServer(res.getKey(),res.getKey().size,res.getValue());
        System.out.println(obj.getZ().getKey()+ " " + obj.getZ().getValue());
        System.out.println("Q pour arrêter le serveur.");
        Scanner scan = new Scanner(System.in);

        String answer=scan.nextLine();
        System.out.println(answer);
        //if(answer=="Q"){
            mainServer.chatServerDisconnection(obj.getZ());
            System.out.println(answer);
            return;
        //}

    }
}

package Server;

import Client.Avatar;
import Client.IPlayer;
import javafx.util.Pair;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import static javafx.application.Platform.exit;

public class GameServerImpl extends UnicastRemoteObject implements IGameServer{

    private static GameServerSimple gs;

    protected GameServerImpl() throws RemoteException {
        super();
    }

    @Override
    public int connection(Avatar avUsed, Integer position, IPlayer player) throws RemoteException{
        return gs.connection(avUsed, position, player);
    }

    @Override
    public int escape(Avatar avUsed, String goTo) throws RemoteException{
        return gs.escape(avUsed, gs.getPosition(avUsed), goTo);
    }

    @Override
    public int move(Avatar avUsed, String goTo) throws RemoteException{
        int res = gs.move(avUsed, goTo);
        if(res!=-1){
            System.out.println(res);
        }
        return res;
    }

    @Override
    public int attackAvatar(Entity target, Avatar ifAvatar, Integer position, int lifeLosed) throws RemoteException {
        return gs.attackAvatar(target, ifAvatar, position, lifeLosed);
    }

    @Override
    public void displayGameInfo() throws RemoteException {
        gs.displayGameInfo();
    }

    @Override
    public void updateZone(Zone z) throws RemoteException {
        gs.setZ(z);
        System.out.println(gs.getZ());
    }

    public static void main(String args[]) throws Exception {
        // Démarre le rmiregistry
        //LocateRegistry.createRegistry(1099);
        GameServerImpl obj = new GameServerImpl();
        IServerController mainServer = (IServerController) Naming.lookup("//localhost/Dungeon");
        Pair<Grid,Zone> res = mainServer.gameServerConnection(obj);
        gs = new GameServerSimple(res.getKey(),res.getKey().size,res.getValue());
        System.out.println(gs.getZ().getKey()+ " " + gs.getZ().getValue());
        //Naming.rebind("Dungeon", obj);
       // System.out.println("Server.GameServerImpl launched");
        Scanner scan = new Scanner(System.in);
        String answer=scan.nextLine();
        //if(answer=="Q"){
        mainServer.gameServerDisconnection(gs.getZ());
        System.out.println(answer);
        exit();
        //}
    }

}

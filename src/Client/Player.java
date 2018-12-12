package Client;

import java.rmi.Naming;
import java.rmi.RemoteException;

import Server.IGameServer;

public class Player {
    private static String uid = "Rmi31";

    private static void moveAvatar(Avatar av, String way, IGameServer gameServer) throws RemoteException {
        int res = gameServer.move(av, way);
        while(res==-1){
            System.out.println("Il y a pas moyen de passer par là");
            System.out.println("Dites-nous vers où vous voulez aller.");
            res = gameServer.move(av, way);
        }
        av.setPosition(res);
        System.out.println("Vous êtes arrivé sure la case n°" + av.getPosition());
    }

    public static void main(String args[]) {
        try {
            Avatar avTest = new Avatar("Ping");
            // Récupération d'un proxy sur l'objet
            IGameServer obj = (IGameServer) Naming.lookup("//localhost/Dungeon");
            if(obj.connection(avTest.getName(), 0)==1) {
                avTest.setPosition(0);
                System.out.println("Connected");
            }
            else
                System.out.println("Connection failed");
            moveAvatar(avTest,"E", obj);
            moveAvatar(avTest,"S", obj);
            moveAvatar(avTest,"W", obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

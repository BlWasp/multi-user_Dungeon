package Client;

import java.rmi.Naming;
import java.rmi.RemoteException;

import Server.IGameServer;
import Server.IServerController;

public class Player {
    private static String uid = "Rmi31";
    private static IServerController mainServer;
    private static IGameServer obj;

    private static void moveAvatar(Avatar av, String way, IGameServer gameServer) throws RemoteException {
        int res = gameServer.move(av, way);
        while(res==-1){
            System.out.println("Il y a pas moyen de passer par là");
            System.out.println("Dites-nous vers où vous voulez aller.");
            res = gameServer.move(av, way);
        }
        if(res==-2){
            System.out.println("Case non géré par le serveur");
            obj = mainServer.findGameServer(av.getPosition());
        }
        av.setPosition(res);
        System.out.println("Vous êtes arrivé sur la case n°" + av.getPosition());
    }


    //Permet au joueur de s'échapper pendant un combat
    //Pareil que moveAvatar mais affecte un malus de -2 pt à l'avatar
    private int escapeAvatar (Avatar av, String way, IGameServer gameServer) throws RemoteException {
        this.moveAvatar(av,way,gameServer);
        gameServer.escape(av,way);
        av.setLifePoint(av.getLifePoint() - 2);
        System.out.println("Votre vie est maintenant de : " + av.getLifePoint());

        return av.getLifePoint();
    }


    public static void main(String args[]) {
        try {
            Avatar avTest = new Avatar("Ping");
            // Récupération d'un proxy sur l'objet
            //IGameServer obj = (IGameServer) Naming.lookup("//localhost/Dungeon");
            mainServer = (IServerController) Naming.lookup("//localhost/Dungeon");
            obj = mainServer.findGameServer(0);
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

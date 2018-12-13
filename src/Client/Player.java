package Client;

import java.rmi.Naming;
import java.rmi.RemoteException;

import Server.IGameServer;
import Server.IServerController;

public class Player {
    private static String uid = "Rmi31";
    private static IServerController mainServer;
    private static IGameServer obj;

    private static int moveAvatar(Avatar av, String way, IGameServer gameServer) throws RemoteException {
        int res = gameServer.move(av, way);
        while(res==-1){
            System.out.println("Il y a pas moyen de passer par là");
            System.out.println("Dites-nous vers où vous voulez aller.");
            res = gameServer.move(av, way);
        }
        if(res==-2){
            System.out.println("Case non géré par le serveur");
            obj = mainServer.findGameServer(av.getPosition(),way);
            if(obj==null){
                System.out.println("aucun serveur trouvé");
                return -1;
            }
            moveAvatar(av, way, obj);
            return -1;
        }
        av.setPosition(res);
        System.out.println("Vous êtes arrivé sur la case n°" + av.getPosition());
        return 0;
    }


    //Permet au joueur de s'échapper pendant un combat
    //Pareil que moveAvatar mais affecte un malus de -2 pt à l'avatar
    private static int escapeAvatar (Avatar av, String way, IGameServer gameServer) throws RemoteException {
        moveAvatar(av,way,gameServer);
        av.setLifePoint(av.getLifePoint() - 2);
        gameServer.escape(av,way);
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
            if(obj==null){
                System.out.println("Aucun serveur trouvé");
                return;
            }
            if(obj.connection(avTest.getName(), 0)==1) {
                avTest.setPosition(0);
                System.out.println("Connected");
            }
            else
                System.out.println("Connection failed");
            moveAvatar(avTest,"S", obj);
            moveAvatar(avTest,"S", obj);
            moveAvatar(avTest,"S", obj);
            moveAvatar(avTest,"S", obj);
            moveAvatar(avTest,"S", obj);
            escapeAvatar(avTest, "S", obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

import java.rmi.Naming;

public class Player {
    private static String uid = "Rmi31";

    public static void main(String args[]) {
        try {
            Avatar avTest = new Avatar("Ping");
            // Récupération d'un proxy sur l'objet
            IGameServer obj = (IGameServer) Naming.lookup("//localhost/Dungeon");
            if(obj.connection(uid, avTest.getName()))
                System.out.println("Connected");
            else
                System.out.println("Connection failed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package Server;

import Client.Avatar;
import Client.Player;

import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Classe d'un gestionnaire d'ordre
 */
public class OrderProcessor {
    public final Player p;
    private IGameServer gameserver;
    private IChatServer chatserver;
    private IServerController serverController;


    public OrderProcessor(Player p) {
        this.p = p;
        gameserver = p.getObj();
        chatserver = p.getCs();
        serverController = p.getMainServer();
    }
    public void setChatServer(IChatServer cs){
        chatserver=cs;
    }
    /**
     *
     * @return
     */
    public IGameServer getGameserver() {
        return gameserver;
    }

    /**
     *
     * @param gameserver
     */
    public void setGameserver(IGameServer gameserver) {
        this.gameserver = gameserver;
    }

    /**
     *
     * @return
     */
    public IChatServer getChatserver() {
        return chatserver;
    }

    /**
     *
     * @param chatserver
     */
    public void setChatserver(IChatServer chatserver) {
        this.chatserver = chatserver;
    }

    /**
     *
     * @return
     */
    public IServerController getServerController() {
        return serverController;
    }

    /**
     *
     * @param serverController
     */
    public void setServerController(ServerController serverController) {
        this.serverController = serverController;
    }

    public String[] spliter(String s) {
        return s.split(" ");
    }

    public String catArray(String[] tab){
        String res="";
        for( String str : tab){
            res = res + str +" ";
        }
        return res;
    }

    /**
     * Gère l'ordre
     * @param order
     * @param av
     * @return
     * @throws RemoteException
     */
    public int process (String[] order, Avatar av) throws RemoteException, InterruptedException {
        switch (order[0]) {
            case "M":
            case "m":
            case "Move":
                return p.moveAvatar(av, order[1], gameserver, chatserver,p);
            /*case "Attack":
                if (order[1])
                p.attackAvatar(order[2], Avatar ifAvatar, av, pos, gameserver, pow);
                break;*/
            case "/":
                order[0]=" ";
                String message = catArray(order);
                chatserver.speak(av, message);
                break;
            case "E":
            case "e":
            case "Escape":
                p.escapeAvatar(av, order[1], gameserver, chatserver,p);
                break;
            case "N":
            case "n":
            case "Neighbour":
                p.getDm().displayNeighbour(p.getCs());
                break;
            case "R":
            case "r":
            case "Room":
                p.getDm().displayPosition(p.getObj(), p.getCs());
                break;
            case "Exit":
               // serverController.serverDisconnection();
                return 0;
            default:
                System.out.println("Unknown order, please enter one of the following orders : Move, Attack, Escape, Exit or start your order with / to chat");
                return -1;
        }

        /**
         * Appelle le serveur de chat
         */
        if(order[0].charAt(0) == '\"') {
            // Vérifier s'il y a d'autres joueurs dans la pièce
            // Si oui -> lancer le serveur de chat
        }
        return 1;

    }

}

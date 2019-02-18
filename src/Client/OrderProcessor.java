package Client;

import Client.Avatar;
import Client.Player;
import Server.ServerController;
import Server.Server_Interface.IChatServer;
import Server.Server_Interface.IGameServer;
import Server.Server_Interface.IServerController;

import java.rmi.RemoteException;

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

    /** Getter d'un serveur de jeu
     *
     * @return
     */
    public IGameServer getGameserver() {
        return gameserver;
    }

    /** Setter d'un serveur de jeu
     *
     * @param gameserver
     *              Serveur de jeu sur lequel on veut agir
     */
    public void setGameserver(IGameServer gameserver) {
        this.gameserver = gameserver;
    }

    /** Getter d'un serveur de chat
     *
     * @return
     */
    public IChatServer getChatserver() {
        return chatserver;
    }

    /** Setter d'un serveur de chat
     *
     * @param chatserver
     *              Serveur de chat sur lequel on veut agir
     */
    public void setChatserver(IChatServer chatserver) {
        this.chatserver = chatserver;
    }

    /** Getter d'un controleur de serveur
     *
     * @return
     */
    public IServerController getServerController() {
        return serverController;
    }

    /** Setter d'un controleur de serveur
     *
     * @param serverController
     *                  Controleur de serveur sur lequel on veut agir
     */
    public void setServerController(ServerController serverController) {
        this.serverController = serverController;
    }

    /** Récupère tous les mots d'une phrase sous la forme d'un tableau de String
     *
     * @param s
     *          Phrase à diviser en chacun des mots qu'elle contient
     * @return
     */
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
     * Gère l'ordre envoyé par le joueur
     * @param order
     *          Ordre entré par le joueur
     * @param av
     *          Avatar du joueur, concerné par l'ordre
     * @return
     * @throws RemoteException
     */
    public int process (String[] order, Avatar av) throws RemoteException, InterruptedException {
        switch (order[0]) {
            case "M":
            case "m":
            case "Move":
                return p.moveAvatar(av, order[1].toUpperCase(), p.getObj(), p.getCs(),p);
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
                p.escapeAvatar(av, order[1], p.getObj(), p.getCs(),p);
                break;
            case "h":
            case "H":
            case "Help":
                p.getDm().help();
                break;
            case "N":
            case "n":
            case "Neighbour":
                p.getDm().displayNeighbour(p.getCs());
                break;
            case "I":
            case "i":
            case "Info":
                p.getDm().displayPosition(p.getObj(), p.getCs());
                break;
            case "Exit":
               // serverController.serverDisconnection();
                return 0;
            default:
                System.out.println("Unknown order, please enter one of the following orders : Move, Attack, Escape, Exit or start your order with / to chat");
                return -1;
        }
        return -1;
    }

}

package Server;

import Client.Avatar;
import Client.Player;

import java.rmi.RemoteException;

/**
 * Classe d'un gestionnaire d'ordre
 */
public class OrderProcessor {
    private final Player p;
    private IGameServer gameserver;
    private IChatServer chatserver;
    private ServerController serverController;

    public OrderProcessor(Player p, IGameServer gameserver, IChatServer chatserver, ServerController serverController) {
        this.p = p;
        this.gameserver = gameserver;
        this.chatserver = chatserver;
        this.serverController = serverController;
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
    public ServerController getServerController() {
        return serverController;
    }

    /**
     *
     * @param serverController
     */
    public void setServerController(ServerController serverController) {
        this.serverController = serverController;
    }

    /**
     * Gère l'ordre
     * @param order
     * @param av
     * @return
     * @throws RemoteException
     */
    public int process (String[] order, Avatar av) throws RemoteException {
        switch (order[0]) {
            case "Move":
                p.moveAvatar(av, order[1], gameserver);
                break;
            /*case "Attack":
                if (order[1])
                p.attackAvatar(order[2], Avatar ifAvatar, av, pos, gameserver, pow);
                break;*/
            case "Escape":
                p.escapeAvatar(av, order[1], gameserver);
                break;
            case "Exit":
               // serverController.serverDisconnection();
                return 0;
            default:
                System.out.println("Unknown order, please enter one of the following orders : Move, Attack, Escape, Exit or start your order with \" to chat");
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

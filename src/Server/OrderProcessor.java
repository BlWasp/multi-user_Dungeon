package Server;

import Client.Avatar;
import Client.Player;

import java.rmi.RemoteException;

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

    public IGameServer getGameserver() {
        return gameserver;
    }

    public void setGameserver(IGameServer gameserver) {
        this.gameserver = gameserver;
    }

    public IChatServer getChatserver() {
        return chatserver;
    }

    public void setChatserver(IChatServer chatserver) {
        this.chatserver = chatserver;
    }

    public ServerController getServerController() {
        return serverController;
    }

    public void setServerController(ServerController serverController) {
        this.serverController = serverController;
    }

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

        if(order[0].charAt(0) == '\"') {
            // Vérifier s'il y a d'autres joueurs dans la pièce
            // Si oui -> lancer le serveur de chat
        }
        return 1;

    }

}

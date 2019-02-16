package Server.Server_Interface;

import Server.Zone;

import java.rmi.RemoteException;

/**
 * Interface du serveur de jeu utilisé par le serverController
 * Mais inutilisable par les players
 */
public interface IGameServerManagement extends IGameServer {
    void updateZone(Zone z) throws RemoteException;
}

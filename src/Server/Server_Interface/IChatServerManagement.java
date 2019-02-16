package Server.Server_Interface;

import Server.Zone;

import java.rmi.RemoteException;

/**
 * Interface du Chat server utilisé par le serverController
 * Mais inutilisable par les players
 */
public interface IChatServerManagement extends IChatServer{
    void updateZone(Zone z) throws RemoteException;
}

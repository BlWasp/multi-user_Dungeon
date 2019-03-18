package Client;

import Server.Entity;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface pour faire fonctionner le RMI entre client et serveur
 */
public interface IPlayer extends Remote {
    public String getUid() throws RemoteException;
    public void updateAvatar(Avatar av) throws RemoteException;
    public void underAttack(Avatar attacked, Entity attacker) throws RemoteException;
    public void receiveMessage(Avatar sender, String message) throws  RemoteException;
    public int ping() throws RemoteException;
    public void fightMessage(Integer id, Integer damage) throws RemoteException;
}

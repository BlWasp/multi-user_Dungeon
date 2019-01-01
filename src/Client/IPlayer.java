package Client;

import Server.Entity;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayer extends Remote {
    public void updateAvatar(Avatar av) throws RemoteException;
    public void underAttack(Avatar attacked, Entity attacker) throws RemoteException;
}

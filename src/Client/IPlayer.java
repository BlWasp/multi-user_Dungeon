package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayer extends Remote {
    public void updateAvatar(Avatar av) throws RemoteException;
}

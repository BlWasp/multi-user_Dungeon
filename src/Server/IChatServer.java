package Server;

import Client.Avatar;

import java.rmi.RemoteException;

public interface IChatServer extends java.rmi.Remote {
    public void speak(String uid, String text) throws RemoteException;
    void updateZone(Zone z) throws RemoteException;
    int connection(Avatar av, Integer position) throws RemoteException;
}

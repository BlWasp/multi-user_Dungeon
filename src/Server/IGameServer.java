package Server;

import Client.Avatar;

import java.rmi.RemoteException;

public interface IGameServer extends java.rmi.Remote{
    //permet la connection du joueur au server de jeu et de spécifier le personnage utilisé
    int connection(String avName, Integer position) throws RemoteException;
    //permet au joueur de s'échaper d'une case
    void escape(Avatar avUsed, String goTo) throws RemoteException;
    void move(Avatar avUsed, String goTo) throws RemoteException;
    void displayGameInfo() throws RemoteException;
}

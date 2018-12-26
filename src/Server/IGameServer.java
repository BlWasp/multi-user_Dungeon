package Server;

import Client.Avatar;

import java.rmi.RemoteException;

public interface IGameServer extends java.rmi.Remote{
    //permet la connection du joueur au server de jeu et de spécifier le personnage utilisé
    int connection(Avatar avUsed, Integer position) throws RemoteException;
    //permet au joueur de s'échaper d'une case
    void escape(Avatar avUsed, String goTo) throws RemoteException;
    int move(Avatar avUsed, String goTo) throws RemoteException;
    //permet de gérer les attaques des entités
    int attackAvatar(Entity target, Avatar ifAvatar, Integer position, int lifeLosed) throws RemoteException;
    void displayGameInfo() throws RemoteException;
    void updateZone(Zone z) throws RemoteException;
}

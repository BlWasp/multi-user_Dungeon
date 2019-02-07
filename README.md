# multi-user_Dungeon
A student project in Java

Done
    -génération dynamique de la grille
    -création des entités avatar et monstre
    -création du serveur de jeu
    -création du serveur de chat
    -création du client
    -connexion du client au serveurs
    -déplacement du joueur
    -attaque monstre
    -attaque entre joueur
    -création du serveur manager
    -gestion connexion serveur de jeu et de chat au serveur pricipal
    -répartition de zone en fonction du nombre de serveur
    -répartition des clients sur les serveurs de jeux
    -gestion de la déconnexion d'un serveur
    -gestion de la connexion d'un nouveau serveur en cours de partie
    -gestion du changement de serveur d'un client
    -mise en place des tours
    -information de changement de position côté client
    -information de dégât côté client

To Do
    -interface utilisateur
    -chat fonctionnel
    -meilleur gestion des monstres
    -éclatement des classes
    -attaque en continu
    
    
BDD
===

Installation
------------
sudo apt-get install mysql-server

Lancement
---------
sudo systemctl start mysql

sudo mysql

> CREATE DATABASE projetMUD;

> USE projetMUD;

> SOURCE fichier.sql

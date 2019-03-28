# multi-user_Dungeon
A student project in Java

Presentation
============

It's a multiplayer board game. The objective is to cover the entire board by killing the monster from each square to reach the final boss. Also, it's possible to fight between players.


Gameplay
========

Introduction
------------
Since this project consisted in setting up a video game, we considered it necessary to have a game design phase to set up an interesting gameplay for the player, thus adding additional constraints but also and especially allowing us to be as close as possible to a real design and development project.

Connexion and move
------------------
In his first game, a player enters a Player name that he must remember and a name for his avatar. He then finds himself on the 0 box. This box is the entry box on the board and does not have a monster. The player can then decide to move in the directions with doors (S and E for square 0). Heading east the monsters gain 5 life points per square; they gain 10 per line going south.
When arriving on a new room, the player can then send a message through the chat to the other players in the room with the symbol \, attack the monster, attack another player or escape. As long as a monster is present on the square it is not possible to simply move, the only way to move is to escape which results in a penalty of two life points for the avatar.

Fights
------
Monsters are peaceful at first, but they block your way and know how to defend themselves. If the player chooses the attack there is a 50/50 chance that the hit will hit the opponent (whether the attack is on an avatar or on the monster). If the attack hits, the target loses one life point. Otherwise it is countered and the original avatar loses one point of life.
When the monster in the hut dies, all the avatars present on the hut recover all their health points, move up to the next level and earn a permanent bonus of 5 health points. They can then kill each other if they wish or simply change boxes.
The player must continuously pay attention to his health points since he is never safe from a critical strike inflicting 3 damage points. The probability of a critical strike or receiving a critical strike is 20%. To encourage the multiplayer aspect and add tension to the game we added the possibility of a fatal blow inflicting 1000 hp. The probability for such a move is 0.2%.

Heals
-----
A heal method allows the player to regenerate the life of his avatar. For it to be usable, the avatar must be on a square without a monster. The method restores two life points to each call. However, be careful that the monster killed on this square does not reappear.
Indeed, every 100 laps all the monsters killed on the set automatically rise again.

Death
-----
If unfortunately the player's avatar dies in front of a monster, he will have to wait for an ally to come and defeat the monster to save him. Once the monster is dead, the player will be able to heal his avatar again so that he can come back to life. It is therefore strategically advisable to always keep a hut without a living monster behind you to retreat to in case of a hard blow.

Reconnexion
-----------
Finally, if the player already has a profile registered in the database when he logs in, he will have the possibility to choose one of his avatars or create a new one.


To Do
=====

- continue attack
- develop avatar classes
- real password with hash


BDD
===

Installation
------------
sudo apt-get install mysql-server

sudo apt-get install mysql-client

Launch
---------
sudo systemctl start mysql

sudo mysql

> CREATE DATABASE projetMUD;

> USE projetMUD;

> SOURCE baseProjet.sql

> CREATE USER 'userProjetMUD'@'localhost' IDENTIFIED WITH mysql_native_password BY '1234';

> GRANT ALL ON projetMUD.* TO 'userProjetMUD'@'localhost';

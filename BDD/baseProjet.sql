#------------------------------------------------------------
#        Script MySQL.
#------------------------------------------------------------


#------------------------------------------------------------
# Table: Player
#------------------------------------------------------------

CREATE TABLE Player(
        Username Varchar (50) NOT NULL ,
        Position Integer NOT NULL ,
        Life     Integer NOT NULL ,
        Mdp      Varchar (50) NOT NULL
	,CONSTRAINT Player_AK UNIQUE (Mdp)
	,CONSTRAINT Player_PK PRIMARY KEY (Username)
);


#------------------------------------------------------------
# Table: Monstre
#------------------------------------------------------------

CREATE TABLE Monstre(
        Place Integer NOT NULL ,
        Vie  Integer NOT NULL
	,CONSTRAINT Monstre_PK PRIMARY KEY (Place)
);
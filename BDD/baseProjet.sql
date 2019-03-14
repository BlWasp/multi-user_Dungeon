#------------------------------------------------------------
#        Script MySQL.
#------------------------------------------------------------


#------------------------------------------------------------
# Table: Player
#------------------------------------------------------------

CREATE TABLE Player(
        UsernamePl Varchar (50) NOT NULL ,
        Mdp      Varchar (50) NOT NULL
	,CONSTRAINT Player_PK PRIMARY KEY (UsernamePl)
);


#------------------------------------------------------------
# Table: Avatar
#------------------------------------------------------------

CREATE TABLE Avatar(
		UsernameAv Varchar (50) NOT NULL ,
        UsernamePl Varchar (50) NOT NULL ,
		Position Integer NOT NULL ,
        Life     Integer NOT NULL ,
        MaxLifePoint Integer NOT NULL
    ,CONSTRAINT Avatar_PK PRIMARY KEY (UsernameAv)
    ,CONSTRAINT Avatar_Player_FK FOREIGN KEY (UsernamePl) REFERENCES Player(UsernamePl)
);


#------------------------------------------------------------
# Table: Monstre
#------------------------------------------------------------

CREATE TABLE Monster(
        Place Integer NOT NULL ,
        Life  Integer NOT NULL ,
        MaxLifePoint Integer NOT NULL
	,CONSTRAINT Monster_PK PRIMARY KEY (Place)
);
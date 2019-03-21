package DataBase;

import java.sql.*;

/**
 * Classe permettant la connexion et les interactions avec la BD
 */
public class DataBaseLink {
    Connection conn;

    /**
     * Méthode permettant de se connecter au serveur MySQL
     */
    public void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:mysql://localhost:3306/projetMUD";
            String user = "userProjetMUD";
            String passwd = "1234";

            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de faire une recherche dans la BD
     * @param datas
     *          La donnée recherchée
     * @param table
     *          La table dans laquelle cherchée
     * @param option
     *          Valeur à positionner après le WHERE pour savoir quelle valeur prendre
     */
    public String searchDB(String datas, String table, String option) {
        try {
            Statement state = conn.createStatement();

            String query = "SELECT " + datas + " FROM " + table + " WHERE " + option;
            ResultSet result = state.executeQuery(query);
            if(result.next()){
                return result.getObject(1).toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Erreur lecture base de données";
    }

    /**
     * Permet de renvoyer plusieurs élément d'une recherche
     * @param datas
     *          Colonne visée
     * @param table
     *          Nom de la table sql
     * @param option
     * @return
     */
    public String multiSearchDB(String datas, String table, String option) {
        String res="";
        try {
            Statement state = conn.createStatement();

            String query = "SELECT " + datas + " FROM " + table + " WHERE " + option;
            ResultSet result = state.executeQuery(query);
            while(result.next()){
                res = result.getObject(1).toString()+ " " + res;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(res=="") return "Erreur lecture base de données";
        return res;
    }


    /**
     * Affiche tous les avatars que possède un player
     * @param playerName
     *          Nom du player dont on veut les avatars
     */
    public void searchAvatarDB(String playerName) {
        try {
            Statement state = conn.createStatement();

            String query = "SELECT UsernameAv" + " FROM Avatar" + " WHERE UsernamePl=" + playerName;
            ResultSet result = state.executeQuery(query);
            ResultSetMetaData resultMeta = result.getMetaData();

            System.out.println("\n**********************************");

            //On affiche le nom des colonnes
            for(int i = 1; i <= resultMeta.getColumnCount(); i++)
                System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");

            System.out.println("\n**********************************");

            while(result.next()){
                for(int i = 1; i <= resultMeta.getColumnCount(); i++)
                    System.out.print("\t" + result.getObject(i).toString() + "\t |");

                System.out.println("\n---------------------------------");
            }

            result.close();
            state.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet d'insérer une nouvelle donnée dans la BD
     * @param datas
     *          Donnée que l'on veut insérer
     * @param table
     *          Table où l'on veut insérer
     */
    public void insertDB(String datas, String table) {
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            String query = "INSERT INTO " + table + " VALUES " + datas;
            state.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de mettre à jour une donnée présente dans la BD
     * @param datas
     *          Donnée à mettre à jour
     * @param table
     *          Table où se trouve la donnée
     * @param options
     *          Valeur à mettre après le WHERE pour savoir où modifier dans la BD
     */
    public void updateDB(String datas, String table, String options) {
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String query = "UPDATE " + table + " SET " + datas + " WHERE " + options;
            state.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*public static void main(String[] args) {

        DataBaseLink dbl = new DataBaseLink();
        dbl.connectDB();

        dbl.insertDB("("+"\""+"Black"+"\""+","+"\""+"mdp"+"\""+")","Player");
        dbl.insertDB("(6,4)","Monstre");
        dbl.insertDB("(7,3)","Monstre");

        dbl.searchDB("UsernamePl","Player","UsernamePl="+"\""+"Black"+"\"");
        dbl.updateDB("Vie=2","Monstre","Place=6");
        dbl.searchDB("Vie","Monstre","Place=6");

    }*/
}

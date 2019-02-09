package BaseDeDonnees;

import java.sql.*;

public class DataBaseLink {
    Connection conn;

    public void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:mysql://localhost:3306/projetMUD";
            String user = "guillaume";
            String passwd = "1234";

            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchDB(String datas, String table, String option) {
        try {
            Statement state = conn.createStatement();

            String query = "SELECT " + datas + " FROM " + table + " WHERE " + option;
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

    public void insertDB(String datas, String table) {
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            String query = "INSERT INTO " + table + " VALUES " + datas;
            state.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        DataBaseLink dbl = new DataBaseLink();
        dbl.connectDB();

        dbl.insertDB("(5,2)","Monstre");
        dbl.insertDB("(6,4)","Monstre");
        dbl.insertDB("(7,3)","Monstre");

        dbl.searchDB("Vie","Monstre","Place=6");

    }
}

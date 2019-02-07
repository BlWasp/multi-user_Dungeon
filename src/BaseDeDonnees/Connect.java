package BaseDeDonnees;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:mysql://localhost:3306/projetMUD";
            String user = "guillaume";
            String passwd = "Yolo1234";

            Connection conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

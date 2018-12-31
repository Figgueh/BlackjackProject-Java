package blackJack;

import java.sql.*;
import java.sql.Connection;

public class DatabaseConnection {

        private Connection conn;

        public DatabaseConnection(String url) {
            try {
                // db parameters
                url = "jdbc:sqlite:" + url;

                // create a connection to the database
                conn = DriverManager.getConnection(url);

                System.out.println("Connection to SQLite has been established.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        public String getPlayer(String username, String password){
            StringBuilder newPlayer = new StringBuilder();

            try {
                Statement query = conn.createStatement();

                ResultSet result = query.executeQuery("SELECT * FROM Players WHERE username='" + username + "'");

                System.out.println(result.getInt(1));
                System.out.println(result.getString(4));
                System.out.println(result.getString(5));



                if(result.getString(3).equals(password)){
                    newPlayer.append(result.getInt(1) + ",");
                    newPlayer.append(result.getString(4) + ",");
                    newPlayer.append(result.getDouble(5));
                }
            }
            catch (Exception ex){
                System.out.println("EXECUTION ERROR : couldn't find this username " + ex.getMessage());
            }

            return newPlayer.toString();
        }


}

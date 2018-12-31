import enums.Location;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Player extends CardPlayer {
    private int id;
    private String name;
    private BigDecimal balance;
    private Location playerLocation;

    public Player(){

    }
    public Player(int id, String name, BigDecimal balance){
        this.id = id;
        this.name = name;
        this.balance = balance;
    }
    public Player(int id, String name, BigDecimal balance, Location location){
        this.id = id;
        this.name = name;
        this.balance = balance;
        playerLocation = location;
    }

    public int getId(){
        return id;
    }

    public BigDecimal getBalance(){
        return balance;
    }

    public void setBalance(BigDecimal value){
        balance = value;
    }

    public String getName(){
        return name;
    }

    public void setName(String value){
        name = value;
    }

    public Location getLocation(){
        return playerLocation;
    }

    public void setLocation(Location pos){
        playerLocation = pos;
    }

    @Override
    public String toString(){
        return "The player with the ID: " + this.id + " is named " + this.name + " and has a balance of: " + this.balance + " currently sitting at: " + this.playerLocation;
    }


//    public void save(Connection database){
//        try{
//            //record can be new
//            Statement query = database.createStatement();
//            ResultSet result = query.executeQuery("SELECT MAX(id + 1) FROM Players");
//
//            int latestId = result.getInt(1);
//
//            //Save to the database with the latest id
//            query.executeUpdate("INSERT INTO Players (id, name, balance, date_of_birth) " +
//                    "values (" + latestId + "," + this.name + "," + this.balance + ")");
//
//
//            //record can be edited.
//
//
//        }catch (Exception ex){
//            System.out.println(ex.getMessage());
//        }
//
//    }

//    public static ArrayList<Player> list(Connection database){
//        ArrayList<Player> players = new ArrayList<>();
//
//        SimpleDateFormat formatDate = new SimpleDateFormat("yyy-MM-dd");
//
//        //get all the players in the database.
//        try{
//            Statement query = database.createStatement();
//
//            ResultSet result = query.executeQuery("SELECT * FROM Players");
//
//            while (result.next()){
//                players.add(new Player(
//                        result.getInt(1),
//                        result.getString(2),
//                        result.getBigDecimal(3),
//                        formatDate.parse(result.getString(4))));
//            }
//
//            for (Player player : players) {
//                System.out.println(player);
//            }
//
//        }catch (Exception ex){
//            System.out.println(ex.getMessage());
//        }
//
//        return players;
//
//    }
}

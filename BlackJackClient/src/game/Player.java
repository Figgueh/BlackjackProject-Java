package game;


public class Player {
    public int id;
    public String playerName;
    public double playerBalance;

    public Player(int id, String name, double balance){
        this.id = id;
        this.playerName = name;
        this.playerBalance = balance;
    }

    @Override
    public String toString(){
        return id + "," + playerName + "," + playerBalance;
    }
}

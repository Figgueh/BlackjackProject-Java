import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Lobby implements Runnable{

    private ArrayList<Socket> playerConnections;
    private ServerSocket serverSocket;
    private int numberOfSeats = 5;
    private int port;


    public Lobby(ArrayList<Socket> playerConnections, int port){
        this.port = port;
        this.playerConnections = playerConnections;
    }

    @Override
    public void run() {
        if(numberOfSeats==0) {
            System.out.println("The lobby is full");
        }

        try {
            serverSocket = new ServerSocket(port, numberOfSeats);
        }
        catch(Exception ex){
            System.out.println("ERROR: creating the lobby");
        }

        //While there is still room in the server socket
        while (numberOfSeats > 0) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                playerConnections.add(clientSocket);
                numberOfSeats--;
                System.out.println("Client connected successfully from: " + clientSocket.getInetAddress().getHostAddress() + ", number of active connections: " + playerConnections.size());
                System.out.println("Number of seats that remain: " + numberOfSeats);

            } catch (Exception ex) {
                System.out.println("Lobby registration closed.");
                break;
            }
        }
        try{
            serverSocket.close();
        }
        catch (Exception e){
            System.out.println("Unable to close server socket");
            return;
        }

    }

    public void closeLobby(){
        try{
            serverSocket.close();
        }catch (Exception ex){
            System.out.println("ERROR CLOSING LOBBY");
        }
    }
}
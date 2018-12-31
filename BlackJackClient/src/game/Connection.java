package game;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Connection {

    private int port;
    private String host;
    private Socket connection;
    private PrintStream out;
    private BufferedReader in;
    private String lastMessage;

    public Connection(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void connect(){
        try{
            connection = new Socket(host, port);
            out = new PrintStream(connection.getOutputStream());
            in = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public void sendToServer(String input){
        //Pass to server
        out.println(input);
    }

    public String observe(){
        return lastMessage;
    }

    public String getFromServer() {
        try{
            return lastMessage = in.readLine();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }
}

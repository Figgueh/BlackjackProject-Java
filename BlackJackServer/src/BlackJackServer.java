import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class BlackJackServer implements Runnable {

    private int port;
    private Thread serverThread;
    private static final int countDown = 30;
    private ArrayList<Socket> playerConnections;

    BlackJackServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        synchronized (this) {
            serverThread = Thread.currentThread();
        }

        while (true){
            System.out.println("Waiting for client connections...");

            //Fill lobby with players.
            playerConnections = new ArrayList<>();
            Lobby clientLobby = new Lobby(playerConnections, port); //client, playerConnections, connectedPlayers
            Thread running = new Thread(clientLobby);
            running.start();

            //Sleep the blackjack server to allow the lobby to fill up.
            try{
                serverThread.sleep(countDown * 300);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            System.out.println("Players joined: " + playerConnections.size());
            clientLobby.closeLobby();
            if(playerConnections.size() == 0){
                System.out.println("Nobody connected.");
            }
            else {
                //synchronizations
                CyclicBarrier bettingStage = new CyclicBarrier(playerConnections.size());
                CyclicBarrier choiceStage = new CyclicBarrier(playerConnections.size());

                //Create a deck to pass to each player and dealer
                Deck deck = new Deck();

                //Create the dealer to be passed to each player
                Dealer dealer = new Dealer();
                dealer.initializeDealer(deck);

                //create a new thread for each player:
                for (Socket clientSocket:playerConnections) {
                    try {
                        ClientHandler player = new ClientHandler(clientSocket, playerConnections, deck, dealer, bettingStage, choiceStage);
                        new Thread(player).start();
                    }catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        BlackJackServer server = new BlackJackServer(4302);
        Thread BlackJackServer = new Thread(server);
        BlackJackServer.start();

    }
}




//        BlackJackServer blackJack = new BlackJackServer(4302);
//
//        try {
//            blackJack.start();
//        }catch (Exception ex){
//            System.out.println("BlackJackServer unable to start.");
//        }


//        while (true){
//            try{
//                //BlackJackServer socket.
//                ServerSocket server = new ServerSocket(4302);
//
//                System.out.println("BlackJackServer running");
//
//                //Socket the talk to client.
//                Socket ss = server.accept();
//
//                //Read from the client
//                Scanner clientScanner = new Scanner(ss.getInputStream());
//
//                //Get the number from the client
//                String clientInput = clientScanner.nextLine();
//
//                String[] parsedInput = clientInput.split(",");
//
//                int cardTypeVal = Integer.parseInt(parsedInput[0]);
//                int cardSuitVal = Integer.parseInt(parsedInput[1]);
//                enums.CardType cardType = enums.CardType.values()[cardTypeVal];
//                enums.CardSuits cardSuit = enums.CardSuits.values()[cardSuitVal];
//
//                Card requestedCard = new Card(cardType, cardSuit);
//
//                System.out.println("Requested : " + requestedCard.toString());
//
//                //Passing back to the client
//                PrintStream p = new PrintStream(ss.getOutputStream());
//                p.println(requestedCard.getCardImage());
//            }
//            catch (Exception ex){
//                System.out.println(ex.getMessage());
//            }
//        }
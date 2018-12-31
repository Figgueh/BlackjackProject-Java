import enums.CardSuits;
import enums.CardType;
import enums.Location;

import java.io.*;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static java.lang.Double.parseDouble;
    public class ClientHandler implements Runnable{

        private final Socket clientSocket;
        private PrintStream out;
        private BufferedReader in;

        private ArrayList<Socket> playerConnections;
        private ArrayList<Player> players = new ArrayList<>();
        private static Location[] locations = new Location[5];

        private Player activePlayer;
        private Location playerLocation;
        private Deck deck;
        private Dealer dealer;

        private CyclicBarrier bettingStage;
        private CyclicBarrier choiceStage;

        public ClientHandler(Socket socket, ArrayList<Socket> playerConnections, Deck deck, Dealer dealer, CyclicBarrier bettingStage, CyclicBarrier choiceStage)throws IOException{
            this.clientSocket = socket;
            this.playerConnections = playerConnections;
            this.deck = deck;
            this.dealer = dealer;
            this.bettingStage = bettingStage;
            this.choiceStage = choiceStage;
            out = new PrintStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));
        }

        public void sendToClient(String input){
            out.println(input);
            System.out.println("SERVER -> CLIENT: " + input);
        }

        public String receiveFromClient() throws IOException{
            String input = in.readLine();
            System.out.println("FROM CLIENT: " + input);
            return input;
        }

        @Override
        public void run() {
            String message = "";

            try {
                do {
                    message = receiveFromClient();

                    //SEND THE AVAILABLE SEATS
                    if (message.equals("Hello")) {
                        String openSeats = "TS";       //Taken Seats

                        //If there aren't any players in locations.
                        if (locations[0] == null) openSeats += ",LEFTMOST";
                        if (locations[1] == null) openSeats += ",LEFT";
                        if (locations[2] == null) openSeats += ",CENTER";
                        if (locations[3] == null) openSeats += ",RIGHT";
                        if (locations[4] == null) openSeats += ",RIGHTMOST";

                        //Send all open locations back to clients
                        sendToClient(openSeats);
                    }

                    if(message.startsWith("DI")){
                        //Send the dealers card information to clients
                        sendToClient("DI" + dealer.toString());
                    }


                    //MAKE PLAYER
                    if (message.startsWith("NP")) {     //NP = New Player.

                        String[] playerInfo = message.split(",");
                        synchronized (this) {
                            activePlayer = new Player(Integer.parseInt(playerInfo[1]), playerInfo[2], new BigDecimal(playerInfo[3]));
                        }
                    }

                    //SEAT PLAYER
                    if (message.startsWith("PP")) {     //PP = Player Position
                        String[] splitMessage = message.split(",");

                        //Parse the player location from the message
                        playerLocation = Location.valueOf(splitMessage[1]);
                        activePlayer.setLocation(playerLocation);

                        //Add the player to the list of all players
                        players.add(activePlayer);

                        //Show that the location is full
                        locations[playerLocation.ordinal()] = activePlayer.getLocation();

                        //START ACCEPTING BETS
                        sendToClient("READY FOR BETS | SAY HELLO");

                        //Tell clients to say hello!


                    }

                    //PLACE BET
                    if (message.startsWith("PB")) {
                        String[] playerResponse = message.split(",");

                        BigDecimal playerBalance = activePlayer.getBalance();
                        BigDecimal playerBet = new BigDecimal(parseDouble(playerResponse[1]));

                        //Check if the player has enough money
                        if (playerBalance.compareTo(playerBet) >= 0) {
                            activePlayer.setBalance(playerBalance.subtract(playerBet));
                            System.out.println(activePlayer.getName() + " just placed a " + playerBet + "$ bet!");
                            sendToClient("PB,ACCEPTED");

                            //Add two cards to the players hand
                            activePlayer.addCardToHand(deck);
                            activePlayer.addCardToHand(deck);
                        } else {
                            //Show the client we deny the request
                            sendToClient("PB,DENY");
                            //Ask the client for another
                            sendToClient("READY FOR BETS");
                            //Show to console we rejected the bet
                            System.out.println(activePlayer.getName() + " has insufficient funds!");
                        }

                        //Wait for the other players to place their bets
                        try{
                            bettingStage.await();
                        }catch (Exception e){
                            System.out.println("ERROR WITH :" + e.getMessage());
                        }

                    }

                    //When a player asks for the hand
                    if (message.startsWith("PL")) {
                        String response = "PL,";
                        ArrayList<Card> playerHand = activePlayer.getHand();

                        //Send the players position
                        response += activePlayer.getLocation();

                        //Send the player ID
                        response += "," + activePlayer.getId();

                        //Add the value of the hand to the response.
                        response += "," + activePlayer.handValue;

                        //For each of the cards in the players hand
                        for (Card inHand : playerHand) {
                            //Add the image url to the response.
                            response += "," + inHand.getCardImage();
                        }

                        //Send all the cards back to the client.
                        sendToClient(response);

                        //Ask for an action
                        sendToClient("ACTION REQUESTED");
                    }

                    //ACTION REQUESTED
                    if (message.contains("RA")) {
                        String[] messageRequest = message.split(",");

                        if (messageRequest[1].equals("hit")) {
                            activePlayer.addCardToHand(deck);
                        } else if (messageRequest[1].equals("stand")) {
                            //Wait for all the other players to click stand.
                            try{
                                choiceStage.await();
                            }catch (Exception ex){
                                System.out.println(ex.getMessage());
                            }

                            //Dealers turn
                            dealer.dealersTurn(deck);

                            for (Card card: dealer.getHand()) {
                                card.setVisible(true);
                                card.getCardImage();
                            }

                            //Send the new cards to all the clients.
                            sendToClient("DI" + dealer.toString());
                        }
                    }

                    //I need something that is continuously checking for updates from the server in the client.
                    //Something that can check when a new player joined or for when all the players have clicked stand.




                    /*
                    CountDownLatch
                    counts down how many threads reach this point, and waits at this point till it reaches 0
                    then fires another type of thread

                    Cyclic barrier
                    also has a count
                    Class will execute as normal until it reaches an await, for similar threads

                    Mutex
                    Only one thread will have access to a variable, this thread can only pass it on to another when it has finished processing (Locks and unlocks)
                    This is known as ownership of the variable.

                    Semaphore
                    Only a limited number of threads can access whats stored in the semaphore
                    Signals when it has finished, There is no ownership of the variable.
                    Therefor it isn't passed on to the next thread, it is instead shared.
                     */


                    //TESTING
                    //Preform actions:
                    //FC = Find Card.
                    if (message.startsWith("FC")) {
                        String[] parsedInput = message.split(",");
                        int cardTypeVal = Integer.parseInt(parsedInput[1]);
                        int cardSuitVal = Integer.parseInt(parsedInput[2]);
                        CardType cardType = CardType.values()[cardTypeVal];
                        CardSuits cardSuit = CardSuits.values()[cardSuitVal];
                        Card requestedCard = new Card(cardType, cardSuit);
                        out.println(requestedCard.getCardImage());
                    }
                } while (!message.equals("QUIT"));

            } catch (IOException ex) {
                System.out.println("unable to continue reading from client.");
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            } finally {
                closeConnection();
            }
        }

        public void closeConnection(){
            try{
                //Close the input and output stream to the client
                in.close();
                out.close();

                //Show the seat as available
                locations[playerLocation.ordinal()] = null;

                //Remove from the list of active players
                players.remove(activePlayer);

                //Remove from the list of active connections
                playerConnections.remove(clientSocket);

                //Close connection
                clientSocket.close();

                System.out.println("Connection successfully closed! number of active connections: " + playerConnections.size());
            }catch (IOException ex){
                System.out.println("Unable to close connection!");
            }catch (Exception ex){
                System.out.println("ERROR: " + ex.getMessage());
            }
        }


    }

package blackJack;


import game.Connection;
import game.Location;
import game.Player;
import javafx.collections.ObservableArray;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class gameController implements Initializable {

    @FXML

    //Seat buttons
    public Button LEFTMOSTbtn;
    public Button LEFTbtn;
    public Button CENTERbtn;
    public Button RIGHTbtn;
    public Button RIGHTMOSTbtn;

    //Poker chips:
    public Pane chips;
    public Button chip5;
    public Button chip10;
    public Button chip25;
    public Button chip100;
    public Button remove;
    public Label lblPot;
    public Label lblBalance;

    //Player panes
    public GridPane LEFTMOSTplayer;
    public Label LEFTMOSTplayerName;
    public ImageView LEFTMOSTplayerCards;
    public Label LEFTMOSTplayerBalance;
    public Label LEFTMOSTcardValue;

    public GridPane LEFTplayer;
    public Label LEFTplayerName;
    public ImageView LEFTplayerCards;
    public Label LEFTplayerBalance;
    public Label LEFTcardValue;

    public GridPane CENTERplayer;
    public Label CENTERplayerName;
    public ImageView CENTERplayerCards;
    public Label CENTERplayerBalance;
    public Label CENTERcardValue;

    public GridPane RIGHTplayer;
    public Label RIGHTplayerName;
    public ImageView RIGHTplayerCards;
    public Label RIGHTplayerBalance;
    public Label RIGHTcardValue;

    public GridPane RIGHTMOSTplayer;
    public Label RIGHTMOSTplayerName;
    public ImageView RIGHTMOSTplayerCards;
    public Label RIGHTMOSTplayerBalance;
    public Label RIGHTMOSTcardValue;

    public ImageView dealerCards;

    public GridPane actionGrid;
    public Button hit;
    public Button stand;





    Connection serverConnection;
    public Player player;
    private double pot = 0;
    private double balance = 100.00;
    private int port = 4302;
    boolean operation = true;  //True = addition, False = subtraction

    public void setPlayer(String playerString){
        String[] playerDetails = playerString.split(",");
        player = new Player(Integer.parseInt(playerDetails[0]), playerDetails[1], Double.parseDouble(playerDetails[2]));

        //If the user changed port
        if(playerDetails.length >= 3)
            port = Integer.parseInt(playerDetails[3]);

        serverConnection.sendToServer("NP," + player.toString());
    }

    public void getDealer(){
        serverConnection.sendToServer("DI");

        String message = serverConnection.getFromServer();
        if (message.startsWith("DI")){
            String[] dealerInformation = message.split(",");
            Image dealerHand = joinBufferedImages(dealerInformation);
            dealerCards.setImage(dealerHand);
        }
    }

    @FXML
    public void seatPlayer(ActionEvent e){
        String position = "";

        //Catch the button that was pressed to send it to the server
        if(e.getSource() == LEFTMOSTbtn){
            position = "LEFTMOST";
            LEFTMOSTplayer.setVisible(true);
            LEFTMOSTplayerName.setText(LEFTMOSTplayerName.getText() + player.playerName);
            LEFTMOSTplayerBalance.setText(Double.toString(player.playerBalance));
        }
        else if(e.getSource() == LEFTbtn){
            position = "LEFT";
            LEFTplayer.setVisible(true);
            LEFTplayerName.setText(LEFTplayerName.getText() + player.playerName);
            LEFTplayerBalance.setText(Double.toString(player.playerBalance));
        }

        else if(e.getSource() == CENTERbtn) {
            position = "CENTER";
            CENTERplayer.setVisible(true);
            CENTERplayerName.setText(CENTERplayerName.getText() + player.playerName);
            CENTERplayerBalance.setText(Double.toString(player.playerBalance));
        }
        else if(e.getSource() == RIGHTbtn) {
            position = "RIGHT";
            RIGHTplayer.setVisible(true);
            RIGHTplayerName.setText(RIGHTplayerName.getText() + player.playerName);
            RIGHTplayerBalance.setText(Double.toString(player.playerBalance));
        }
        else if(e.getSource() == RIGHTMOSTbtn) {
            position = "RIGHTMOST";
            RIGHTMOSTplayer.setVisible(true);
            RIGHTMOSTplayerName.setText(RIGHTMOSTplayerName.getText() + player.playerName);
            RIGHTMOSTplayerBalance.setText(Double.toString(player.playerBalance));
        }
        serverConnection.sendToServer("PP," + position);

        //Remove the buttons
        LEFTMOSTbtn.setVisible(false);
        LEFTbtn.setVisible(false);
        CENTERbtn.setVisible(false);
        RIGHTbtn.setVisible(false);
        RIGHTMOSTbtn.setVisible(false);

        //Show the betting chips
        chips.setVisible(true);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverConnection = new Connection("localhost", port);
        serverConnection.connect();

        //Send hello to the server
        serverConnection.sendToServer("Hello");

        //Get the available seats from the server
        String message = serverConnection.getFromServer();
        if(message.contains("TS")){
            String[] openSeats = message.split(",");


            //Change visibility status on the seats that are taken
            for (String seat: openSeats) {

                if(seat.equals("LEFTMOST"))
                    LEFTMOSTbtn.setVisible(true);
                else if(seat.equals("LEFT"))
                    LEFTbtn.setVisible(true);
                else if(seat.equals("CENTER"))
                    CENTERbtn.setVisible(true);
                else if(seat.equals("RIGHT"))
                    RIGHTbtn.setVisible(true);
                else if(seat.equals("RIGHTMOST"))
                    RIGHTMOSTbtn.setVisible(true);
            }
        }

        //Show the user the balance that's left.
        lblBalance.setText(Double.toString(balance));

//        Thread t = new Thread(() -> {
//            while (true){
//                if (serverConnection.observe().contains("SAY HELLO")){
//                    serverConnection.sendToServer("Hello");
//                    String introduction = serverConnection.getFromServer();
//                    if(introduction.contains("TS")){
//                        String[] openSeats = introduction.split(",");
//
//
//                        //Change visibility status on the seats that are taken
//                        for (String seat: openSeats) {
//
//                            if(seat.equals("LEFTMOST"))
//                                LEFTMOSTbtn.setVisible(true);
//                            else if(seat.equals("LEFT"))
//                                LEFTbtn.setVisible(true);
//                            else if(seat.equals("CENTER"))
//                                CENTERbtn.setVisible(true);
//                            else if(seat.equals("RIGHT"))
//                                RIGHTbtn.setVisible(true);
//                            else if(seat.equals("RIGHTMOST"))
//                                RIGHTMOSTbtn.setVisible(true);
//                        }
//                    }
//
//
//                }
//            }
//        });
//
//        t.start();

    }

    public void editBet(ActionEvent actionEvent) {
        //Check if the remove chip was clicked
        if(actionEvent.getSource() == remove && operation)
            operation = false;
        else if(actionEvent.getSource() == remove && operation == false)
            operation = true;

        if(actionEvent.getSource() == chip5)
            performOperation(5, operation);

        else if(actionEvent.getSource() == chip10)
            performOperation(10, operation);

        else if (actionEvent.getSource() == chip25)
            performOperation(25, operation);

        else if(actionEvent.getSource() == chip100)
            performOperation(100, operation);

        lblPot.setText(Double.toString(pot));
        lblBalance.setText(Double.toString(balance));

    }

    public void performOperation(double bet, boolean operation){
        //If operation = addition
        if(operation){
            //If the balance is bigger or equal to the pot
            if(balance >= bet){
                //Add to the pot and remove from balance.
                this.pot += bet;
                this.balance -= bet;
            }
            else{
                System.out.println("You don't have anything else to bet!");
            }
        }
        //If operation = subtraction
        else
        {
            //If the pot is bigger or equal to the bet
            if(pot >= bet){
                //Remove from pot and add back to the balance.
                this.pot -= bet;
                this.balance += bet;
            }
            else{
                System.out.println("There isn't anything to take!");
            }
        }
    }

    public void placeBet(ActionEvent actionEvent) {
        //Check if the server is asking for bets
        if(serverConnection.getFromServer().contains("READY FOR BETS"))
            serverConnection.sendToServer("PB," + Double.toString(pot));

        //Check if the bet was accepted
        if(serverConnection.getFromServer().equals("PB,ACCEPTED")){
            System.out.println("Your bet was placed!");

            //Remove the panel for betting
            chips.setVisible(false);

            //Show the possible actions
            actionGrid.setVisible(true);

            //Get the hand from the server
            getHand();

            //Show what the dealer has
            dealerCards.setVisible(true);
        }
        else
            System.out.println("The server rejected your request!");
    }

    public void getHand(){
        //Ask the server for the cards
        serverConnection.sendToServer("PL," + player.id + ",GETHAND");
        String serverResponse = serverConnection.getFromServer();

        if(serverResponse.startsWith("PL")){
            String[] playerArray = serverResponse.split(",");
            String playerLocation = playerArray[1];

            //Print what the client received from the server
            System.out.println(playerLocation + " player received :");

            //Get the hand value
            String handValue = playerArray[3];

            //Send the array of individual card images to be joined together
            Image cardsInHand = joinBufferedImages(playerArray);

            //Decide which player to give the cards to
            if(playerLocation.equals(Location.LEFTMOST.toString())){
                LEFTMOSTplayerCards.setImage(cardsInHand);
                LEFTMOSTcardValue.setText(handValue);
            }
            if(playerLocation.equals(Location.LEFT.toString())){
                LEFTplayerCards.setImage(cardsInHand);
                LEFTcardValue.setText(handValue);
            }

            if(playerLocation.equals(Location.CENTER.toString())){
                CENTERplayerCards.setImage(cardsInHand);
                CENTERcardValue.setText(handValue);
            }

            if(playerLocation.equals(Location.RIGHT.toString())){
                RIGHTplayerCards.setImage(cardsInHand);
                RIGHTcardValue.setText(handValue);
            }

            if(playerLocation.equals(Location.RIGHTMOST.toString())){
                RIGHTMOSTplayerCards.setImage(cardsInHand);
                RIGHTMOSTcardValue.setText(handValue);
            }

        }
        else
        {
            System.out.println("INVALID SERVER RESPONSE.");
        }
    }

    public static Image joinBufferedImages(String[] cardsFromSever) {
        //Variables for our new image
        int offset = 0;
        int width = 200;
        int height = 200;

        //Setup an array to receive each card
        ArrayList<BufferedImage> individualCards = new ArrayList<>();

        for (String cardUrl: cardsFromSever) {
            //Only use the url values from the response.
            if(cardUrl.contains(".png")){
                System.out.println(cardUrl);

                try{
                    //Read each of the new cards from the server.
                    BufferedImage newCard = SwingFXUtils.fromFXImage(new Image(cardUrl), null);

                    //Add them to an array to be buffered together.
                    individualCards.add(newCard);

                }catch (Exception ex){
                    System.out.println("Unable to read card information.");
                }
            }
        }

        //Prepare application for new image generation
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();

        //For each of the items in our array, Draw and shift to the right
        for (BufferedImage card: individualCards) {
            g2.drawImage(card, 0 + offset,0, width, height,null);
            offset += 30;
        }

        //Clean and return
        g2.dispose();

        //Convert the BufferedImage back to the regular image
        Image cards = SwingFXUtils.toFXImage(newImage,null);

        return cards;
    }

    public void sendAction(ActionEvent actionEvent) {
        //Check if the server is asking for an action
        if(serverConnection.getFromServer().equals("ACTION REQUESTED")){
            if(actionEvent.getSource() == hit){
                serverConnection.sendToServer("RA,hit");
                getHand();
            }

            else if(actionEvent.getSource() == stand){
                serverConnection.sendToServer("RA,stand");

                if(serverConnection.getFromServer().equals("WAITING"))
                    System.out.println("Waiting for other");
            }


        }
    }
}

package blackJack;


import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class loginController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    public ComboBox cboServer;

    private String newPlayer;

    public String[] portList = {
            "4302",
            "1337",
            "8008",
            "911"
    };



    public void login(ActionEvent actionEvent) {
        DatabaseConnection newConn = new DatabaseConnection("C:\\Users\\figue\\Desktop\\BlackJackClient\\src\\dependencies\\database\\blackjackDB.db");

        /*
        serverConnection = new Connection("localhost", port);
        serverConnection.connect();
         */

        //Get the player information
        newPlayer = newConn.getPlayer(username.getText(), password.getText());

        if (newPlayer != null && newPlayer != ""){
            //Get the server information
            newPlayer += "," + cboServer.getSelectionModel().getSelectedItem().toString();

            loadGame(actionEvent);
        }

    }

    //FOR STARTING GAME!
    public void loadGame(ActionEvent event) {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("game.fxml"));
            Parent root = loader.load();
            gameController gc = loader.getController();
            gc.setPlayer(newPlayer);
            gc.getDealer();
            Stage stage = new Stage();
            stage.setTitle("Blackjack!");
            stage.setScene(new Scene(root));
            stage.show();

            //Hide the current window.
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList observablePorts = FXCollections.observableArrayList(portList);

        cboServer.setItems(observablePorts);
        cboServer.getSelectionModel().selectFirst();
    }
}

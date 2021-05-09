package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import it.polimi.ingsw.server.controller.SessionController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectToServer extends ConnectToServerViewBuilder implements GUIView {
    @FXML
    private StackPane connectionPane;
    @FXML
    private Button connectionButton;
    @FXML
    private TextField addressText;
    @FXML
    private TextField portText;
    private Text errortext;

    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ConnectToServerScene.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }



        Scene scene = new Scene(root);

        getClient().getStage().setScene(scene);

        getClient().getStage().show();
    }

    //Add buttons here that call client.changeViewBuilder(new *****, this);

    public void handleButton()
    {
        if(isIPAddr(addressText.getCharacters().toString()))
            if(!portText.getCharacters().toString().isEmpty())
            {
                Client.getInstance().changeViewBuilder(new CreateJoinLoadMatch(), null);
                return;
            }



        errortext.setOpacity(200);


        Client.getInstance().getStage().show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        errortext=new Text("INSERISCI I DATI CORRETTI!");
        errortext.setFont(Font.font(null, FontWeight.BOLD,10));
        StackPane.setAlignment(errortext, Pos.TOP_CENTER);
        StackPane.setMargin(errortext,new Insets(40,10,10,10));

        errortext.setOpacity(0);

        connectionPane.getChildren().add(errortext);

        StackPane.setAlignment(errortext, Pos.TOP_CENTER);
        StackPane.setMargin(errortext,new Insets(40,10,10,10));
        //connectionButton.setOnAction(e -> Client.getInstance().changeViewBuilder(new CreateJoinLoadMatch(), null));
    }
}

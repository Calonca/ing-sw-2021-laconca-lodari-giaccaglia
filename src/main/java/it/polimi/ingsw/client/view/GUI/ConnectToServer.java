package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import javafx.application.Platform;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

/**
 * During this phase, the initial pane will be created. All following scenes will attach themselves to the connectionPane
 */
public class ConnectToServer extends ConnectToServerViewBuilder implements GUIView {

    public Text error;
    @FXML
    private AnchorPane connectionAnchor;
    @FXML
    private StackPane connectionPane;

    private StackPane single_instance = null;

    private TextField addressText;
    private TextField portText;
    private TextField nickText;

    @Override
    public void run() {

        single_instance = new StackPane();
        single_instance.setPrefWidth(1000);
        single_instance.setPrefHeight(700);
        single_instance.getChildren().add(getRoot());

        Scene scene = new Scene(single_instance);

        getClient().getStage().setScene(scene);
        getClient().getStage().show();
        System.out.println(((Pane)Client.getInstance().getStage().getScene().getRoot()).getChildren());

    }



    public Parent getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ConnectToServerScene.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;

    }
    //Add buttons here that call client.changeViewBuilder(new *****, this);

    public void handleButton()
    {

        if(!nickText.getCharacters().toString().isEmpty())
            if(isIPAddr(addressText.getCharacters().toString()))
                if(Integer.parseInt(portText.getCharacters().toString())<65536)
                {
                    Client.getInstance().getCommonData().setCurrentnick(nickText.getCharacters().toString());
                    Client.getInstance().setServerConnection(addressText.getCharacters().toString(),Integer.parseInt(portText.getCharacters().toString()));
                    Client.getInstance().run();
                    return;
                }


        error.setOpacity(200);


        Client.getInstance().getStage().show();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        ImageView logoView=new ImageView(new Image("assets/logo.png", true));
        StackPane.setAlignment(logoView,Pos.TOP_CENTER);

        nickText=new TextField();
        nickText.setLayoutX(461);
        nickText.setLayoutY(419);
        nickText.setPromptText("nickname");


        portText=new TextField();
        portText.setLayoutX(461);
        portText.setLayoutY(471);
        portText.setPromptText("port");
        portText.setText(Integer.toString(Client.getInstance().getPort()));

        addressText=new TextField();
        String ip = Client.getInstance().getIp();
        if (ip!=null)
            addressText.setText(ip);
        addressText.setPromptText("ip address");
        addressText.setLayoutX(461);
        addressText.setLayoutY(445);


        logoView.setFitWidth(800);
        logoView.setFitHeight(300);

        connectionAnchor.getChildren().add(nickText);
        connectionAnchor.getChildren().add(addressText);
        connectionAnchor.getChildren().add(portText);

        connectionPane.getChildren().add(logoView);
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.matchesDataString))
            Platform.runLater(()->
            {
                Client.getInstance().changeViewBuilder(new CreateJoinLoadMatch());
            });

    }
}

package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * During this phase, the player will be asked to connect to a game server. It's composed by three text forms
 * and a confirmation button
 */
public class ConnectToServer extends ConnectToServerViewBuilder implements GUIView {

    public Text error;
    public AnchorPane connectionPane;
    @FXML
    private AnchorPane connectionAnchor;


    private StackPane firstPane = null;

    private TextField addressText;
    private TextField portText;
    private TextField nickText;

    @Override
    public void run() {

        firstPane = new StackPane();
        firstPane.setPrefWidth(1000);
        firstPane.setPrefHeight(700);
        firstPane.getChildren().add(getRoot());

        Scene scene = new Scene(firstPane);
        getClient().getStage().setScene(scene);

        getClient().getStage().getScene().getStylesheets().add("assets/application.css");
        getClient().getStage().show();

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

    public Button validationButton()
    {
        Button button=new Button();
        button.setLayoutX(500);
        button.setLayoutY(350);
        button.setText("CONNECT");
        button.setOnAction(p->
        {
            if(!nickText.getCharacters().toString().isEmpty())
                if(isIPAddr(addressText.getCharacters().toString()))
                    if(Integer.parseInt(portText.getCharacters().toString())<65536)
                    {
                        getClient().getCommonData().setCurrentnick(nickText.getCharacters().toString());
                        getClient().setServerConnection(addressText.getCharacters().toString(),Integer.parseInt(portText.getCharacters().toString()));
                        getClient().run();
                        return;
                    }
            error.setOpacity(200);

        });
        return button;
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
        portText.setText(Integer.toString(getClient().getPort()));

        addressText=new TextField();
        String ip = getClient().getIp();
        if (ip!=null)
            addressText.setText(ip);
        addressText.setPromptText("ip address");
        addressText.setLayoutX(461);
        addressText.setLayoutY(445);


        logoView.setFitWidth(800);
        logoView.setFitHeight(300);
        logoView.setLayoutX(100);

        connectionPane.getChildren().add(nickText);
        connectionPane.getChildren().add(addressText);
        connectionPane.getChildren().add(portText);
        connectionPane.getChildren().add(validationButton());
        connectionPane.setId("pane");

        connectionPane.getChildren().add(logoView);

    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.matchesDataString))
            Platform.runLater(()->
                    getClient().changeViewBuilder(new CreateJoinLoadMatch()));

    }
}

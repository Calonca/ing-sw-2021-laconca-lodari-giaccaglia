package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
public class StartingScreen extends ConnectToServerViewBuilder implements GUIView {


    public AnchorPane startingPane;

    @Override
    public void run() {



        getClient().getStage().getScene().getStylesheets().add("assets/application.css");
        getClient().getStage().show();

    }


    /**
     * The first scene needs a parent, starting from the second this method will only return SubScene
     * @return the first game scene
     */
    public StackPane getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/StartingScreen.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
        StackPane firstPane;
        firstPane = new StackPane();
        firstPane.setPrefWidth(1000);
        firstPane.setPrefHeight(700);
        firstPane.getChildren().add(root);
        firstPane.setId("START");
        return firstPane;

    }
    //Add buttons here that call client.changeViewBuilder(new *****, this);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ImageView imageView=new ImageView(new Image("assets/logo.png"));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(1000);
        startingPane.getChildren().add(imageView);
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.matchesDataString))
            Platform.runLater(()->
                    getClient().changeViewBuilder(new CreateJoinLoadMatch()));

    }
}

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


        GUI.getRealPane().getChildren().add(getRoot());
    }


    /**
     * The first scene needs a parent, starting from the second this method will only return SubScene
     * @return the first game scene
     */
    public StackPane getRoot() {

        StackPane firstPane;
        firstPane = new StackPane();
        firstPane.setPrefWidth(1800);
        firstPane.setPrefHeight(1000);
        firstPane.setId("start");

        return firstPane;

    }
    //Add buttons here that call client.changeViewBuilder(new *****, this);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}

package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * During this phase, the player will be asked to connect to a game server. It's composed by three text forms
 * and a confirmation button
 */
public class StartingScreen extends ConnectToServerViewBuilder implements GUIView {


    public AnchorPane startingPane;

    /**
     * This method is used to BootsTrap the GUI
     */
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
        firstPane.setPrefWidth(GUI.GUIwidth);
        firstPane.setPrefHeight(GUI.GUIlen);
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

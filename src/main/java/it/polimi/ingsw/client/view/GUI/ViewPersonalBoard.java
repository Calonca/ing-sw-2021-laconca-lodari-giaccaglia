package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewPersonalBoard extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView
{
    public StackPane menuPane;
    public ImageView boardView;

    @Override
    public void run() {
        ((Pane)Client.getInstance().getStage().getScene().getRoot()).getChildren().remove(0);
        ((Pane)Client.getInstance().getStage().getScene().getRoot()).getChildren().add(getRoot());
        System.out.println(((Pane)Client.getInstance().getStage().getScene().getRoot()).getChildren());
    }

    public Parent getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ViewPersonalBoard.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ImageView tempo;
        tempo=new ImageView(new Image("assets/board/smallerboard.png", true));

        tempo.setFitHeight(511);
        tempo.setFitWidth(530);
        menuPane.getChildren().add(new Label("si nu topy"));

       /* boardView.setImage(new Image("assets/board/Masters of Renaissance_PlayerBoard (11_2020)-1.png", true));
        boardView.setRotate(270);
        boardView.setFitHeight(600);
        boardView.setFitWidth(9000);*/
        StackPane.setAlignment(tempo, Pos.BOTTOM_CENTER);
        menuPane.getChildren().add(0,tempo);
        Client.getInstance().getStage().show();
    }
}

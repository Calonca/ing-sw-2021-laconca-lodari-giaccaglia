package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ViewPersonalBoard.fxml"));
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ImageView tempo;
        tempo=new ImageView(new Image("assets/board/biggerboard.png", true));

        tempo.setFitHeight(700);
        tempo.setFitWidth(1000);
       /* boardView.setImage(new Image("assets/board/Masters of Renaissance_PlayerBoard (11_2020)-1.png", true));
        boardView.setRotate(270);
        boardView.setFitHeight(600);
        boardView.setFitWidth(9000);*/
        StackPane.setAlignment(tempo, Pos.CENTER);
        menuPane.getChildren().add(tempo);
        Client.getInstance().getStage().show();
    }
}

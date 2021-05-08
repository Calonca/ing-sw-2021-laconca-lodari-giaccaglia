package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.server.controller.SessionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateJoinLoadMatch extends CreateJoinLoadMatchViewBuilder implements GUIView {


    public Button joinLoadButton;
    public Button createButton;
    public StackPane cjlPane;

    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CreateJoinLoadMatchScene.fxml"));
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
    public void handleButton1()
    {
        ////TODO ADD OBSERVER FOR CONNECTION
        getClient().changeViewBuilder(new CreateMatch(), null);
    }

    public void handleButton2()
    {
        ////TODO ADD OBSERVER FOR CONNECTION
        getClient().changeViewBuilder(new JoinLoadMatch(), null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateMatch extends CreateJoinLoadMatchViewBuilder implements GUIView {

    public StackPane connectionPane;
    public Button connectionButton;
    public TextField addressText;
    public TextField portText;

    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CreateMatchScene.fxml"));
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
        ////TODO ADD OBSERVER FOR CONNECTION
        getClient().changeViewBuilder(new it.polimi.ingsw.client.view.GUI.CreateJoinLoadMatch(), null);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
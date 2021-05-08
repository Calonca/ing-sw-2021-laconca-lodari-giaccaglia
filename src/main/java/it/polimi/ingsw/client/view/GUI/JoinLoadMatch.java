package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The user will be asked to join a match of their choosing.
 */
public class JoinLoadMatch extends CreateJoinLoadMatchViewBuilder implements GUIView {

    @FXML
    private TableView guiMatchesData;

    @Override
    public void run() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/JoinLoadMatchScreenScene.fxml"));
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

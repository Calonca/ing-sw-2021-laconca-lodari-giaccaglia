package it.polimi.ingsw.client.view.GUI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * The user will be asked if they want to join a match of their choosing or create one.
 */
public class CreateJoinLoadMatch extends CreateJoinLoadMatchViewBuilder implements GUIView {

    @FXML
    private Button joinLoadButton;
    @FXML
    private Button createButton;
    @FXML
    private StackPane cjlPane;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        joinLoadButton.setOnAction(e -> Client.getInstance().changeViewBuilder(new JoinLoadMatch()));
        createButton.setOnAction(e -> Client.getInstance().changeViewBuilder(new CreateMatch()));
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(
                Client.getInstance().getCommonData().getMatchesData().orElse(null)));

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}

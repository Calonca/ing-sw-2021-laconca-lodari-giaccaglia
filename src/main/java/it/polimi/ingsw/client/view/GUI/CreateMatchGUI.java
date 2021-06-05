package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateMatchGUI extends CreateJoinLoadMatchViewBuilder implements GUIView {


    public AnchorPane createPane;
    public javafx.scene.control.Button a;
    public javafx.scene.control.Button b;
    public javafx.scene.control.Button c;
    public Button d;

    public void run()
    {
        Node toAdd=getRoot();

        toAdd.setId("CREATE");
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toAdd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CreateMatch.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,500,400);

    }

    public void bindCreateButton(Button b, int playerCount) {

        b.setText(Integer.toString(playerCount));
        b.setOnAction(p ->
        {

            getClient().getServerHandler().sendCommandMessage(new CreateMatchRequest(playerCount,getCommonData().getCurrentnick()));
            getClient().changeViewBuilder(new MatchToStart());
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(1);

        });


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindCreateButton(a,1);
        bindCreateButton(b,2);
        bindCreateButton(c,3);
        bindCreateButton(d,4);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}

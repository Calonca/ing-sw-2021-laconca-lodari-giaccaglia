package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.InitialOrFinalPhaseViewBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InitialOrFinalPhaseGUI extends InitialOrFinalPhaseViewBuilder implements GUIView {
    public AnchorPane leaderPane;
    public Button c;

    public InitialOrFinalPhaseGUI(boolean isInitial) {
        super(isInitial);
    }

    public InitialOrFinalPhaseGUI() {
        super();
    }
    @Override
    public void run()
    {
        Node toAdd=getRoot();

        toAdd.setId("LEADER");
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(toAdd);
        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/InitialOrFinalPhase.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,500,400);

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        c.setOnAction(p ->  ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(4) );

    }
}

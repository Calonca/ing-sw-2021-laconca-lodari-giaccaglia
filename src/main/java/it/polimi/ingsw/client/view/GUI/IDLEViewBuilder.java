package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.GUI.GUIelem.ButtonSelectionModel;
import it.polimi.ingsw.client.view.GUI.GUIelem.ResourceButton;
import it.polimi.ingsw.server.model.Resource;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class IDLEViewBuilder extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView
{

    @Override
    public void run() {
        getClient().getStage().setResizable(true);
        getClient().getStage().setHeight(800);
        ViewPersonalBoard temp=new ViewPersonalBoard();
        Platform.runLater(temp);
        getClient().getStage().setWidth(1200);
        getClient().getStage().setResizable(false);

        System.out.println(((Pane)getClient().getStage().getScene().getRoot()).getChildren());

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(State.INITIAL_PHASE.name()))
            getClient().changeViewBuilder(new InitialOrFinalPhaseGUI());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}

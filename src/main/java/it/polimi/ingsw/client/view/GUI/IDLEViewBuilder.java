package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.State;

import javafx.application.Platform;

import javafx.scene.layout.Pane;


import java.beans.PropertyChangeEvent;
import java.net.URL;

import java.util.ResourceBundle;

public class IDLEViewBuilder extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView
{

    /**
     * While waiting to receive the first state, the personalBoard gets displayed
     */
    @Override
    public void run() {

        BoardView3D temp=new BoardView3D();
        Platform.runLater(temp);


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

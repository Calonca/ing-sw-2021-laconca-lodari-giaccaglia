package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.abstractview.IDLEViewBuilder;
import javafx.scene.layout.Pane;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class IDLEViewBuilderGUI extends IDLEViewBuilder implements GUIView
{

    /**
     * While waiting to receive the first state, the personalBoard gets displayed
     */
    @Override
    public void run() {

        BoardView3D temp=SetupPhase.getBoard();
        if(!temp.active)
            temp.runforStart();

        System.out.println(GUI.getRealPane().getChildren());
        SetupPhase.getBoard().setMode(BoardView3D.Mode.BACKGROUND);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}

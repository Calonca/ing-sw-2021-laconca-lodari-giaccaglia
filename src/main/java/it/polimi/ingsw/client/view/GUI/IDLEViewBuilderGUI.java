package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.IDLEViewBuilder;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller associated to IDLE, in which the player can look around and move resources freely on their board
 */
public class IDLEViewBuilderGUI extends IDLEViewBuilder implements GUIView
{

    /**
     * While waiting to receive the first state, the personalBoard gets displayed
     */
    @Override
    public void run() {
        Playground.getThisPlayerBoard().setMode(BoardView3D.Mode.MOVING_RES);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}

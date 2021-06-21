package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.IDLEViewBuilder;

import java.net.URL;
import java.util.ResourceBundle;

public class IDLEViewBuilderGUI extends IDLEViewBuilder implements GUIView
{

    /**
     * While waiting to receive the first state, the personalBoard gets displayed
     */
    @Override
    public void run() {
        BoardView3D.getBoard().runforStart();
        BoardView3D.getBoard().refreshMarket();
        BoardView3D.getBoard().refreshCardShop();
        BoardView3D.getBoard().refreshLeaders();

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}

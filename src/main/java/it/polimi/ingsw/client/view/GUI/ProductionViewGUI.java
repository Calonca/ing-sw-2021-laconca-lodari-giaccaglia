package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import javafx.application.Platform;

public class ProductionViewGUI extends ProductionViewBuilder {

    /**
     * This method is called during CHOOSING_PRODUCTION
     */
    @Override
    public void run() {
        Platform.runLater(()->Playground.getThisPlayerBoard().setMode(BoardView3D.Mode.CHOOSE_PRODUCTION));
    }

    /**
     * This method is called during CHOOSING_RESOURCE_FOR_PRODUCTION
     */
    @Override
    public void choosingResForProduction() {
        Platform.runLater(()->Playground.getThisPlayerBoard().setMode(BoardView3D.Mode.SELECT_RESOURCE_FOR_PROD));
        //enable resource selection
    }
}

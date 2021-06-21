package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import javafx.application.Platform;

public class ProductionViewGUI extends ProductionViewBuilder {
    @Override
    public void run() {
        Platform.runLater(()->BoardView3D.getBoard().setMode(BoardView3D.Mode.CHOOSE_PRODUCTION));
    }

    @Override
    public void choosingResForProduction() {
        Platform.runLater(()->BoardView3D.getBoard().setMode(BoardView3D.Mode.SELECT_RESOURCE_FOR_PROD));
        //enable resource selection
    }
}

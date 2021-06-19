package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import javafx.application.Platform;

public class ProductionViewGUI extends ProductionViewBuilder {
    @Override
    public void choosingResForProduction() {

        //enable resource selection
    }

    @Override
    public void run() {

        Platform.runLater(()->SetupPhase.getBoard().setMode(BoardView3D.Mode.CHOOSE_PRODUCTION));

    }
}

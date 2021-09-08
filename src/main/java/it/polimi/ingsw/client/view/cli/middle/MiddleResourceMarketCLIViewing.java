package it.polimi.ingsw.client.view.cli.middle;

import it.polimi.ingsw.client.view.cli.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.cli.commonViews.CLIResourceMarket;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Row;
import it.polimi.ingsw.network.simplemodel.SimpleMarketBoard;

public class MiddleResourceMarketCLIViewing extends MiddlePhaseCLI{

    public void run(){

        Row root = new Row();
        Column grid = root.addAndGetColumn();
        SimpleMarketBoard marketBoard = getSimpleModel().getElem(SimpleMarketBoard.class).orElseThrow();
        CLIResourceMarket.buildMarketGrid(grid, marketBoard);
        getCLIView().setBody(CanvasBody.centered(root));
        getCLIView().runOnInput("Press enter to go back", () -> getClient().changeViewBuilder(new MiddlePhaseCLI()));
        getCLIView().show();

    }


}

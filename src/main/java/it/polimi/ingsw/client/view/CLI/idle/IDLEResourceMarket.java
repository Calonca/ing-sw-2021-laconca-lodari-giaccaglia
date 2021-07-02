package it.polimi.ingsw.client.view.CLI.idle;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIResourceMarket;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.network.simplemodel.SimpleMarketBoard;

public class IDLEResourceMarket extends IDLEViewBuilderCLI{

    public void run(){

        getCLIView().setTitle("This is the resource market");
        Row root = new Row();
        Column grid = root.addAndGetColumn();
        SimpleMarketBoard marketBoard = getSimpleModel().getElem(SimpleMarketBoard.class).orElseThrow();
        CLIResourceMarket.buildMarketGrid(grid, marketBoard);
        getCLIView().setBody(CanvasBody.centered(root));
        getCLIView().runOnInput("Press enter to go back", () -> getClient().changeViewBuilder(new IDLEViewBuilderCLI()));
        getCLIView().show();

    }
}

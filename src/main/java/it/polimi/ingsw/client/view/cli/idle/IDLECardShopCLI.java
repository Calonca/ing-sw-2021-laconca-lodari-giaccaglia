package it.polimi.ingsw.client.view.cli.idle;

import it.polimi.ingsw.client.view.cli.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.cli.commonViews.CLICardShop;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Column;

public class IDLECardShopCLI extends IDLEViewBuilderCLI {

        @Override
        public void run(){

            getCLIView().setTitle("Card Shop");
            Column grid = new Column();
            CLICardShop.buildCardShop(grid, true);
            getCLIView().setBody(CanvasBody.centered(grid));
            getCLIView().runOnInput("Press enter to go back", () -> getClient().changeViewBuilder(new IDLEViewBuilderCLI()));
            getCLIView().show();

        }

}

package it.polimi.ingsw.client.view.CLI.idle;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.commonViews.CLICardShop;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;

public class IDLECardShopCLI extends IDLEViewBuilderCLI {

        public void run(){

            getCLIView().setTitle("Card Shop");
            Column grid = new Column();
            CLICardShop cliCardShop = new CLICardShop();
            cliCardShop.buildCardShop(grid, true);
            getCLIView().setBody(CanvasBody.centered(grid));
            getCLIView().runOnInput("Press enter to go back", () -> getClient().changeViewBuilder(new IDLEViewBuilderCLI()));
            getCLIView().show();

        }

}

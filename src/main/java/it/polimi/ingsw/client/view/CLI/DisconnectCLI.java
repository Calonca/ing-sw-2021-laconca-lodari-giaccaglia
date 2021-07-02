package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.abstractview.DisconnectViewBuilder;

public class DisconnectCLI extends DisconnectViewBuilder implements CLIBuilder {
    Column grid;

    @Override
    public void run() {
        getCLIView().setTitle("Disconnecting");
        grid = new Column();
        Option o = Option.noNumber(disconnectedString+DisconnectViewBuilder.seconds+ " seconds");
        grid.addElem(o);
        getCLIView().runOnInput("",()->{});
        getCLIView().setBody(CanvasBody.centered(grid));
        getCLIView().show();
        startWaitingThread();
    }


    @Override
    public void updateCountDown(int remaining) {
        Option o = Option.noNumber(disconnectedString+ remaining+" seconds");
        grid.updateElem(0,o);
        getCLIView().setBody(CanvasBody.centered(grid));
        getCLIView().show();
    }

    @Override
    public void exit() {
        getClient().terminate();
    }

}

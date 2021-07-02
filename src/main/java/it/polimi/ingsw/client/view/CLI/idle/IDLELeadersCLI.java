package it.polimi.ingsw.client.view.CLI.idle;

import it.polimi.ingsw.client.view.CLI.commonViews.CLILeadersBuilder;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

public class IDLELeadersCLI extends IDLEViewBuilderCLI{

    private ViewBuilder nextViewBuilder;
    private int playerIndex;

    public IDLELeadersCLI(ViewBuilder nextViewBuilder, int playerIndex){
        this.nextViewBuilder = nextViewBuilder;
        this.playerIndex = playerIndex;
    }

    @Override
    public void run() {
        showWarningIfLastTurn();
        CLILeadersBuilder.buildView(nextViewBuilder, playerIndex);
    }
}

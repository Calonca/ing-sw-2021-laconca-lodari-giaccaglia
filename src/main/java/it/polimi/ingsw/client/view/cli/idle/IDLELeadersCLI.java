package it.polimi.ingsw.client.view.cli.idle;

import it.polimi.ingsw.client.view.cli.commonViews.CLILeadersBuilder;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

public class IDLELeadersCLI extends IDLEViewBuilderCLI{

    private final ViewBuilder nextViewBuilder;
    private final int playerIndex;

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

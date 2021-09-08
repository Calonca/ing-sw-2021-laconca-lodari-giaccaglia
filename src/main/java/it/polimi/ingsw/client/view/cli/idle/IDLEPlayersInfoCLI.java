package it.polimi.ingsw.client.view.cli.idle;

import it.polimi.ingsw.client.view.cli.commonViews.CLIPlayersInfoBuilder;
import it.polimi.ingsw.client.view.cli.commonViews.CLIReportInfoBuilder;

public class IDLEPlayersInfoCLI extends IDLEViewBuilderCLI {

    @Override
    public void run() {
        showWarningIfLastTurn();
        getClient().saveViewBuilder(this);
        CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new IDLEReportInfoCLI());
        CLIPlayersInfoBuilder.buildView(new IDLEViewBuilderCLI());
    }

}

package it.polimi.ingsw.client.view.CLI.idle;

import it.polimi.ingsw.client.view.CLI.commonViews.CLIPlayersInfoBuilder;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIReportInfoBuilder;

public class IDLEPlayersInfoCLI extends IDLEViewBuilderCLI {

    @Override
    public void run() {
        showWarningIfLastTurn();
        getClient().saveViewBuilder(this);
        CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new IDLEReportInfoCLI());
        CLIPlayersInfoBuilder.buildView(new IDLEViewBuilderCLI());
    }

}

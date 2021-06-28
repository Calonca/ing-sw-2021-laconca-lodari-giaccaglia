package it.polimi.ingsw.client.view.CLI.idle;

import it.polimi.ingsw.client.view.CLI.commonViews.CLIPlayersInfoBuilder;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIReportInfoBuilder;

public class PlayersInfoCLI extends IDLEViewBuilderCLI {

    @Override
    public void run() {
        showWarningIfLastTurn();
        getClient().saveViewBuilder(this);
        CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new ReportInfoCLI());
        CLIPlayersInfoBuilder.buildView(new IDLEViewBuilderCLI());
    }

}

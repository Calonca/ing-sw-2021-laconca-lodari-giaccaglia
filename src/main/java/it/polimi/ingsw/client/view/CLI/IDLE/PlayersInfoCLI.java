package it.polimi.ingsw.client.view.CLI.idle;

import it.polimi.ingsw.client.view.CLI.commonViews.CLIPlayersInfoBuilder;

public class PlayersInfoCLI extends IDLEViewBuilderCLI {

    @Override
    public void run() {
        showVaticanReportInfoDuringIDLE();
        CLIPlayersInfoBuilder.buildView(new IDLEViewBuilderCLI());
    }

}

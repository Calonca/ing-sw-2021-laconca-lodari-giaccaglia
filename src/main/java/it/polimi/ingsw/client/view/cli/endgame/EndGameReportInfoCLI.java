package it.polimi.ingsw.client.view.cli.endgame;

import it.polimi.ingsw.client.view.cli.commonViews.CLIReportInfoBuilder;

public class EndGameReportInfoCLI extends WinLooseCLI {

    @Override
    public void run() {
            CLIReportInfoBuilder.showReportWithTimer();
    }


}

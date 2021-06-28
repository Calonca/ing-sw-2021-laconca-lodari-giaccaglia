package it.polimi.ingsw.client.view.CLI.endgame;

import it.polimi.ingsw.client.view.CLI.commonViews.CLIReportInfoBuilder;

public class EndGameReportInfoCLI extends WinLooseCLI {

    @Override
    public void run() {
            CLIReportInfoBuilder.showReportWithTimer();
    }


}

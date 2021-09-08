package it.polimi.ingsw.client.view.cli.middle;

import it.polimi.ingsw.client.view.cli.commonViews.CLIReportInfoBuilder;

public class MiddleReportInfoCLI extends MiddlePhaseCLI {

    @Override
    public void run() {
            CLIReportInfoBuilder.showReportWithTimer();
    }


}



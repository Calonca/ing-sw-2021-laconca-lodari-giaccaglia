package it.polimi.ingsw.client.view.CLI.middle;

import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIReportInfoBuilder;

public class MiddleReportInfoCLI extends MiddlePhaseCLI {

    private ViewBuilder nextViewBuilder;
    private boolean isAfterReport;

    public MiddleReportInfoCLI(){
        this.isAfterReport = true;
    }

    public MiddleReportInfoCLI(ViewBuilder nextViewBuilder){
        this.isAfterReport = false;
        this.nextViewBuilder = nextViewBuilder;

    }

    @Override
    public void run() {
        if(isAfterReport)
            CLIReportInfoBuilder.showReportWithTimer();
        else
            CLIReportInfoBuilder.showReport(nextViewBuilder);
    }


}



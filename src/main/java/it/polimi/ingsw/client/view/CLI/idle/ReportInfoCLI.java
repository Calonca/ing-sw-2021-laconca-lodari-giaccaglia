package it.polimi.ingsw.client.view.CLI.idle;

import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIReportInfoBuilder;

public class ReportInfoCLI extends IDLEViewBuilderCLI {

    private ViewBuilder nextViewBuilder;
    private final boolean isAfterReport;

    public ReportInfoCLI(){
        this.isAfterReport = true;
    }

    public ReportInfoCLI(ViewBuilder nextViewBuilder){
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



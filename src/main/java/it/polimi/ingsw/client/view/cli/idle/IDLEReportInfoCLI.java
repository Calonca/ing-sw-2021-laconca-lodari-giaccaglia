package it.polimi.ingsw.client.view.cli.idle;

import it.polimi.ingsw.client.view.cli.commonViews.CLIReportInfoBuilder;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

public class IDLEReportInfoCLI extends IDLEViewBuilderCLI {

    private ViewBuilder nextViewBuilder;
    private final boolean isAfterReport;

    public IDLEReportInfoCLI(){
        this.isAfterReport = true;
    }

    public IDLEReportInfoCLI(ViewBuilder nextViewBuilder){
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



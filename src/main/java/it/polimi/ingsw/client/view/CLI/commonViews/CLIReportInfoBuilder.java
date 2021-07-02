package it.polimi.ingsw.client.view.CLI.commonViews;

import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Timer;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.simplemodel.VaticanReportInfo;

import static it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableReportInfo.buildReports;
import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.*;

public final class CLIReportInfoBuilder {


    /**
     * Shows vatican report when it occurs
     * @param nextViewBuilder is the next state's ViewBuilder
     */
    public static void showReport(ViewBuilder nextViewBuilder){

        getCLIView().setTitle("Vatican Report info");

        Column grid = buildReports(getSimpleModel());
        CanvasBody body = CanvasBody.centered(grid);

        getCLIView().setBody(body);
        getCLIView().runOnInput("Press enter to go back", () -> getClient().changeViewBuilder(nextViewBuilder));
        getCLIView().show();

    }



    public static void showReportWithTimer(){

        Title title = new Title("Vatican Report occurred! . Seconds left for viewing : 4", Color.GREEN);
        getCLIView().setTitle(title.toString());

        Column grid = buildReports(getSimpleModel());
        CanvasBody body = CanvasBody.centered(grid);

        getCLIView().setBody(body);
        getCLIView().show();

        Timer.showSecondsOnCLI(getCLIView(), "Vatican Report occurred! . Seconds left for viewing : ", 3);
        getClient().changeViewBuilder(getClient().getSavedViewBuilder());

    }

    public static void showVaticanReportInfoBeforeTransition(ViewBuilder nextViewBuilder){

        VaticanReportInfo reportInfo = getSimpleModel().getElem(VaticanReportInfo.class).get();

        if(reportInfo.hasReportOccurred() && !reportInfo.hasReportBeenShown()){
            reportInfo.reportWillBeShown();
            if(getClient().isCLI()) {
                getClient().changeViewBuilder(nextViewBuilder);
            }

        }
    }
}

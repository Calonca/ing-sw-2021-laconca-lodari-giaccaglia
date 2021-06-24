package it.polimi.ingsw.client.view.CLI.commonViews;

import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Timer;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableReportInfo.buildReports;

public final class CLIReportInfoBuilder extends ViewBuilder {

    public static void showReport(ViewBuilder nextViewBuilder){

        getCLIView().setTitle("Vatican Report info");

        Column grid = buildReports(getSimpleModel());
        CanvasBody body = CanvasBody.centered(grid);

        getCLIView().setBody(body);
        getCLIView().runOnInput("Press enter to go back", () -> getClient().changeViewBuilder(nextViewBuilder));
        getCLIView().show();

    }


    public static void showReportWithTimer(){

        String coloredTitle = Color.colorString("Vatican Report occurred! . Seconds left for viewing : 6", Color.GREEN);
        Title title = new Title("Vatican Report occurred! . Seconds left for viewing : 6", Color.GREEN);
        getCLIView().setTitle(title.toString());

        Column grid = buildReports(getSimpleModel());
        CanvasBody body = CanvasBody.centered(grid);

        getCLIView().setBody(body);
        getCLIView().show();

        Timer.showSecondsOnCLI(getCLIView(), "Vatican Report occurred! . Seconds left for viewing : ");
        getClient().changeViewBuilder(getClient().getSavedViewBuilder());

    }



    @Override
    public void run() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}

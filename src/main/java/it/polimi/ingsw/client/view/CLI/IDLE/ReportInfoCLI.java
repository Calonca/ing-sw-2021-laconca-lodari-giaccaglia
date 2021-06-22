package it.polimi.ingsw.client.view.CLI.IDLE;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Timer;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;

import static it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableReportInfo.buildReports;

public class ReportInfoCLI extends IDLEViewBuilderCLI {

    public enum ViewMode{

        IDLE{

            @Override
            public void view(){
                getCLIView().setTitle("Vatican Report info");

                Column grid = buildReports(getSimpleModel());
                CanvasBody body = CanvasBody.centered(grid);

                getCLIView().setBody(body);
                getCLIView().runOnInput("Press enter to go back", () -> getClient().changeViewBuilder(new IDLEViewBuilderCLI()));
                getCLIView().show();
            }

        },

        AFTER_REPORT{

            @Override
            public void view(){

                String coloredTitle = Color.colorString("Vatican Report occurred! . Seconds left for viewing : 6", Color.GREEN);
                getCLIView().setTitle(coloredTitle);

                Column grid = buildReports(getSimpleModel());
                CanvasBody body = CanvasBody.centered(grid);

                getCLIView().setBody(body);
                getCLIView().show();

                Timer.showSecondsOnCLI(getCLIView(), "Vatican Report occurred! . Seconds left for viewing : ");
                getClient().changeViewBuilder(getClient().getSavedViewBuilder());

            }

        };

        public abstract void view();

    }


    private ViewMode mode;

    public ReportInfoCLI(ViewMode mode){
        this.mode = mode;
    }

    @Override
    public void run() {
        buildView();
    }


    @Override
    protected void buildView() {

        mode.view();
    }


}



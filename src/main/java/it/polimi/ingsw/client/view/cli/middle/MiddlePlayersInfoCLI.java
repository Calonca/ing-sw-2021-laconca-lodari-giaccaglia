package it.polimi.ingsw.client.view.cli.middle;
import it.polimi.ingsw.client.view.cli.commonViews.CLIPlayersInfoBuilder;
import it.polimi.ingsw.client.view.cli.commonViews.CLIReportInfoBuilder;

public class MiddlePlayersInfoCLI extends MiddlePhaseCLI {


    /**
     * This method is called upon a Vatican event during a normal turn
     */
    @Override
    public void run() {
        getClient().saveViewBuilder(this);
        CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new MiddleReportInfoCLI());
        CLIPlayersInfoBuilder.buildView(new MiddlePhaseCLI());
    }


}

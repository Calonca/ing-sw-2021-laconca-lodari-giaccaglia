package it.polimi.ingsw.client.view.CLI.middle;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIPlayersInfoBuilder;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIReportInfoBuilder;

public class MiddlePlayersInfoCLI extends MiddlePhaseCLI {

    @Override
    public void run() {
        getClient().saveViewBuilder(this);
        CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new MiddleReportInfoCLI());
        CLIPlayersInfoBuilder.buildView(new MiddlePhaseCLI());
    }


}

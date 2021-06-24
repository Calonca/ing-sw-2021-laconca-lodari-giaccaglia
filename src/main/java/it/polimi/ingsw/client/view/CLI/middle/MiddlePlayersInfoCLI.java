package it.polimi.ingsw.client.view.CLI.middle;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIPlayersInfoBuilder;

public class MiddlePlayersInfoCLI extends MiddlePhaseCLI {

    @Override
    public void run() {
        showVaticanReportInfoBeforeTransition();
        CLIPlayersInfoBuilder.buildView(new MiddlePhaseCLI());
    }


}

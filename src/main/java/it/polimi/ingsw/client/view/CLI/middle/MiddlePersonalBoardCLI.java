package it.polimi.ingsw.client.view.CLI.middle;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIPersonalBoardBuilder;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIReportInfoBuilder;

public class MiddlePersonalBoardCLI extends MiddlePhaseCLI {

    int playerIndex;
    boolean isMoving;
    PersonalBoardBody.ViewMode viewMode;

    public MiddlePersonalBoardCLI(int playerIndex, boolean isMoving){
        this.playerIndex = playerIndex;
        this.isMoving = isMoving;
    }

    public MiddlePersonalBoardCLI(int playerIndex, boolean isMoving, PersonalBoardBody.ViewMode viewMode){
        this.playerIndex = playerIndex;
        this.isMoving = isMoving;
        this.viewMode = viewMode;
    }

    @Override
    public void run() {
        if (isMoving) {
            CLIPersonalBoardBuilder.buildViewForMoving();
        } else {
            getClient().saveViewBuilder(this);
            CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new MiddleReportInfoCLI());
            CLIPersonalBoardBuilder.buildViewForViewing(playerIndex, viewMode);
        }
    }
}

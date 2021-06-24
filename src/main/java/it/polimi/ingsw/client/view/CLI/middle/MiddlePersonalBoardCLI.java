package it.polimi.ingsw.client.view.CLI.middle;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIPersonalBoardBuilder;

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
            showVaticanReportInfoBeforeTransition();
            CLIPersonalBoardBuilder.buildViewForViewing(playerIndex, viewMode);
        }
    }
}

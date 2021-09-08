package it.polimi.ingsw.client.view.cli.middle;

import it.polimi.ingsw.client.view.cli.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.cli.commonViews.CLIPersonalBoardBuilder;
import it.polimi.ingsw.client.view.cli.commonViews.CLIReportInfoBuilder;

public class MiddlePersonalBoardCLI extends MiddlePhaseCLI {

    private final int playerIndex;
    private final boolean isMoving;
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

    /**
     * This method is to disambiguate between viewing or moving the personal board
     */
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

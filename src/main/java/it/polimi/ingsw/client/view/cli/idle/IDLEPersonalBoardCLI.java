package it.polimi.ingsw.client.view.cli.idle;

import it.polimi.ingsw.client.view.cli.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.cli.commonViews.CLIPersonalBoardBuilder;
import it.polimi.ingsw.client.view.cli.commonViews.CLIReportInfoBuilder;

public class IDLEPersonalBoardCLI extends IDLEViewBuilderCLI{

    private final int playerIndex;
    private final boolean isMoving;
    PersonalBoardBody.ViewMode viewMode;

    public IDLEPersonalBoardCLI(int playerIndex, boolean isMoving){
        this.playerIndex = playerIndex;
        this.isMoving = isMoving;
    }

    public IDLEPersonalBoardCLI(int playerIndex, boolean isMoving, PersonalBoardBody.ViewMode viewMode){
        this.playerIndex = playerIndex;
        this.isMoving = isMoving;
        this.viewMode = viewMode;
    }


    @Override
    public void run() {
        showWarningIfLastTurn();
        getClient().saveViewBuilder(this);
        CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new IDLEReportInfoCLI());
        if (isMoving)
            CLIPersonalBoardBuilder.buildViewForMoving();
        else
            CLIPersonalBoardBuilder.buildViewForViewing(playerIndex, viewMode);
    }

}

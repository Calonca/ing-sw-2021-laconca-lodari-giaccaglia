package it.polimi.ingsw.client.view.CLI.idle;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIPersonalBoardBuilder;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIReportInfoBuilder;

public class PersonalBoardCLI extends IDLEViewBuilderCLI{

    int playerIndex;
    boolean isMoving;
    PersonalBoardBody.ViewMode viewMode;

    public PersonalBoardCLI(int playerIndex, boolean isMoving){
        this.playerIndex = playerIndex;
        this.isMoving = isMoving;
    }

    public PersonalBoardCLI(int playerIndex, boolean isMoving, PersonalBoardBody.ViewMode viewMode){
        this.playerIndex = playerIndex;
        this.isMoving = isMoving;
        this.viewMode = viewMode;
    }


    @Override
    public void run() {
        showWarningIfLastTurn();
        getClient().saveViewBuilder(this);
        CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new ReportInfoCLI());
        if (isMoving) {
            CLIPersonalBoardBuilder.buildViewForMoving();
        } else {
         //   showVaticanReportInfoDuringIDLE();
            CLIPersonalBoardBuilder.buildViewForViewing(playerIndex, viewMode);
        }
    }

}

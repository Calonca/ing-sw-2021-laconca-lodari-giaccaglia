package it.polimi.ingsw.client.view.CLI.IDLE;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;

public class PersonalBoardCLI extends IDLEViewBuilderCLI{

    int playerIndex;

    public PersonalBoardCLI(int playerIndex){
        this.playerIndex = playerIndex;
    }

    @Override
    public void run() {
        buildView();
    }

    @Override
    protected void buildView() {

        showVaticanReportInfoDuringIDLE();
        PersonalBoardBody.seePersonalBoard(playerIndex, PersonalBoardBody.ViewMode.PLAYERS_INFO);
        PlayersInfo playersInfo = (getSimpleModel().getElem(PlayersInfo.class).orElseThrow());
        PlayersInfo.SimplePlayerInfo playerInfo = playersInfo.getSimplePlayerInfoMap().get(playerIndex);
        getCLIView().setTitle(playerInfo.getNickname() + "'s" + " Personal Board");
        getCLIView().show();

    }

}

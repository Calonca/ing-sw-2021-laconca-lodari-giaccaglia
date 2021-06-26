package it.polimi.ingsw.client.view.CLI.commonViews;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.*;

public final class CLIPersonalBoardBuilder{

    public static void buildViewForMoving(){
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.MOVING_RES);
        board.preparePersonalBoard("Select move starting position or press ENTER to go back");
    }

    public static void buildViewForViewing(int playerIndex, PersonalBoardBody.ViewMode viewMode){

        PersonalBoardBody.seePersonalBoard(playerIndex, viewMode);
        PlayersInfo playersInfo = (getSimpleModel().getElem(PlayersInfo.class).orElseThrow());
        PlayersInfo.SimplePlayerInfo playerInfo = playersInfo.getSimplePlayerInfoMap().get(playerIndex);

        String title;
        if(playerIndex == getCommonData().getThisPlayerIndex())
            title = "This is your Personal Board";
        else
            title = playerInfo.getNickname() + "'s" + " Personal Board";

        getCLIView().setTitle(title);
        getCLIView().show();
    }


}

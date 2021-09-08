package it.polimi.ingsw.client.view.cli.commonViews;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.cli.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerInfo;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.*;

public final class CLIPersonalBoardBuilder{

    private CLIPersonalBoardBuilder(){}

    public static void buildViewForMoving(){
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.MOVING_RES);
        board.preparePersonalBoard("Select move starting position or press ENTER to go back");
    }

    public static void buildViewForViewing(int playerIndex, PersonalBoardBody.ViewMode viewMode){

        PersonalBoardBody.seePersonalBoard(playerIndex, viewMode);
        PlayersInfo playersInfo = (getSimpleModel().getElem(PlayersInfo.class).orElseThrow());
        SimplePlayerInfo playerInfo = playersInfo.getSimplePlayerInfoMap().get(playerIndex);

        String title;
        if(playerIndex == CommonData.getThisPlayerIndex())
            title = "This is your Personal Board";
        else
            title = playerInfo.getNickname() + "'s" + " Personal Board";

        getCLIView().setTitle(title);
        getCLIView().show();
    }


}

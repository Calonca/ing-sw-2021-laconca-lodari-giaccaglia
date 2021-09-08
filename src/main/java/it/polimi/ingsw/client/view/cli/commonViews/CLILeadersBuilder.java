package it.polimi.ingsw.client.view.cli.commonViews;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.cli.CLIelem.body.LeaderBody;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.*;

public final class CLILeadersBuilder{

    private CLILeadersBuilder(){}

    /**
     * This method displays the leader after acquiring them from the updated network
     * @param nextViewBuilder is the game phase to go to after selection
     * @param playerIndex is this player's index
     */
    public static void buildView(ViewBuilder nextViewBuilder, int playerIndex) {

        PlayersInfo playersInfo = (getSimpleModel().getElem(PlayersInfo.class).orElseThrow());
        SimplePlayerInfo playerInfo = playersInfo.getSimplePlayerInfoMap().get(playerIndex);
        SimplePlayerLeaders simplePlayerLeaders = getSimpleModel().getPlayerCache(playerIndex).getElem(SimplePlayerLeaders.class).orElseThrow();

        getCLIView().clearScreen();

        String title;
        if(playerIndex == CommonData.getThisPlayerIndex())
            title = "These are your Leaders";
        else
            title = playerInfo.getNickname() + "'s" + " Leaders";

        getCLIView().setTitle(title);
        getCLIView().setBody(new LeaderBody(simplePlayerLeaders.getPlayerLeaders(), getClient(), nextViewBuilder));
        getCLIView().show();

    }

}

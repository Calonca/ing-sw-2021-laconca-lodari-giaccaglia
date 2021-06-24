package it.polimi.ingsw.client.view.CLI.commonViews;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.LeaderBody;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;

import java.beans.PropertyChangeEvent;

public final class CLILeadersBuilder extends ViewBuilder {

    public static void buildView(ViewBuilder nextViewBuilder, int playerIndex) {

        PlayersInfo playersInfo = (getSimpleModel().getElem(PlayersInfo.class).orElseThrow());
        PlayersInfo.SimplePlayerInfo playerInfo = playersInfo.getSimplePlayerInfoMap().get(playerIndex);
        SimplePlayerLeaders simplePlayerLeaders = getSimpleModel().getPlayerCache(playerIndex).getElem(SimplePlayerLeaders.class).orElseThrow();

        getCLIView().clearScreen();

        String title;
        if(playerIndex == getCommonData().getThisPlayerIndex())
            title = "These are your Leaders";
        else
            title = playerInfo.getNickname() + "'s" + " Leaders";

        getCLIView().setTitle(title);
        getCLIView().setBody(new LeaderBody(simplePlayerLeaders.getPlayerLeaders(), getClient(), nextViewBuilder));
        getCLIView().show();

    }


    @Override
    public void run() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}

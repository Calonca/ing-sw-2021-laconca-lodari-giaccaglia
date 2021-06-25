package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.match.LobbyViewBuilderCLI;
import it.polimi.ingsw.client.view.GUI.LobbyViewBuilderGUI;

import java.util.UUID;

public abstract class LobbyViewBuilder extends ViewBuilder {

    protected UUID matchId;

    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new LobbyViewBuilderCLI();
        else return new LobbyViewBuilderGUI();
    }



}

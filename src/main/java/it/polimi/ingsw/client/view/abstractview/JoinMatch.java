package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.ConnectToServer;

import java.util.UUID;

public abstract class JoinMatch extends ViewBuilder implements CLIBuilder {

    protected UUID matchId;

    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new it.polimi.ingsw.client.view.CLI.JoinMatch();
        else return new it.polimi.ingsw.client.view.GUI.JoinLoadMatch();
    }

}

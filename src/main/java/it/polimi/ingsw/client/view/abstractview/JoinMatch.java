package it.polimi.ingsw.client.view.abstractview;

import java.util.UUID;

public abstract class JoinMatch extends ViewBuilder {

    protected UUID matchId;

    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new it.polimi.ingsw.client.view.CLI.match.JoinMatch();
        else return new it.polimi.ingsw.client.view.GUI.MatchToStart();
    }

}

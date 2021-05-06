package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;

import java.util.UUID;

public abstract class JoinMatch extends ViewBuilder implements CLIBuilder {

    protected UUID matchId;

    public JoinMatch(UUID matchId) {
        this.matchId = matchId;
    }

}

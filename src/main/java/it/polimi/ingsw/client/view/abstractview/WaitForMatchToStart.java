package it.polimi.ingsw.client.view.abstractview;

import java.util.UUID;

public abstract class WaitForMatchToStart extends View {
    protected UUID matchId;
    public WaitForMatchToStart(UUID matchId){
        this.matchId = matchId;
    }
}

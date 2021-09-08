package it.polimi.ingsw.network.messages.servertoclient;

import java.util.UUID;

public class CreatedMatchStatus extends ServerToClientMessage {
    protected final UUID matchId;
    public enum motive {NUMOFPLAYERS,NICKNAME,OTHER}
    protected final motive m;

    public CreatedMatchStatus(UUID matchId,motive m) {
        this.matchId = matchId;
        this.m = m;
    }

}

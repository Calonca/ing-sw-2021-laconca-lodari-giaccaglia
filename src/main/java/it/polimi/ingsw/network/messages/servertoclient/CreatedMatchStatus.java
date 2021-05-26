package it.polimi.ingsw.network.messages.servertoclient;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.util.UUID;

public class CreatedMatchStatus extends ServerToClientMessage {
    protected UUID matchId;
    public enum motive {NUMOFPLAYERS,NICKNAME,OTHER}
    protected motive m;


    public CreatedMatchStatus(){}

    public CreatedMatchStatus(ClientToServerMessage parent, UUID matchId,motive m) {
        super(parent);
        this.matchId = matchId;
        this.m = m;
    }

}

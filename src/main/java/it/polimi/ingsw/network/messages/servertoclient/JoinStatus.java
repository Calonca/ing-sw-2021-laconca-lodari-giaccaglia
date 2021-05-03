package it.polimi.ingsw.network.messages.servertoclient;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.util.UUID;

public class JoinStatus extends ServerToClientMessage {
    protected final UUID joinedMatchUUID;
    public enum motive {NUM_PLAYERS_NOT_VALID,ALREADY_FULL,OTHER}
    protected motive m;


    public JoinStatus(ClientToServerMessage parent, UUID joinedMatchUUID, motive m) {
        super(parent);
        this.joinedMatchUUID = joinedMatchUUID;
        this.m = m;
    }

}

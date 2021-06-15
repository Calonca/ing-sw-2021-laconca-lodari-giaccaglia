package it.polimi.ingsw.network.messages.servertoclient;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.util.UUID;

public class JoinStatus extends ServerToClientMessage {
    protected UUID joinedMatchUUID;
    public enum motive {SUCCESS, NUM_PLAYERS_NOT_VALID, ALREADY_FULL, NOT_EXISTS, NICKNAME_ALREADY_EXISTING, OTHER}
    protected motive m;
    protected int playerIndex;

    public JoinStatus(){}

    public JoinStatus(ClientToServerMessage parent, UUID joinedMatchUUID, motive m, int playerIndex) {
        super(parent);
        this.joinedMatchUUID = joinedMatchUUID;
        this.m = m;
        this.playerIndex = playerIndex;
    }

}

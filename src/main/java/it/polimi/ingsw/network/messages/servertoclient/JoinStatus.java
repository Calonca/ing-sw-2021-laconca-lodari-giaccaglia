package it.polimi.ingsw.network.messages.servertoclient;

import java.util.UUID;

public class JoinStatus extends ServerToClientMessage {
    protected UUID joinedMatchUUID;
    public enum motive {SUCCESS, NUM_PLAYERS_NOT_VALID, ALREADY_FULL, NOT_EXISTS, NICKNAME_ALREADY_EXISTING, OTHER}
    protected motive m;
    protected int playerIndex;

    public JoinStatus(){}

    public JoinStatus(UUID joinedMatchUUID, motive m, int playerIndex) {
        this.joinedMatchUUID = joinedMatchUUID;
        this.m = m;
        this.playerIndex = playerIndex;
    }

}

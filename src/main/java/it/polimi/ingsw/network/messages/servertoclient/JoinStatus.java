package it.polimi.ingsw.network.messages.servertoclient;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

public class JoinStatus extends ServerToClientMessage {
    protected boolean joined;
    public enum motive {NUM_PLAYERS_NOT_VALID,ALREADY_FULL,OTHER}
    protected motive m;


    public JoinStatus(ClientToServerMessage parent, boolean joined, motive m) {
        super(parent);
        this.joined = joined;
        this.m = m;
    }

}

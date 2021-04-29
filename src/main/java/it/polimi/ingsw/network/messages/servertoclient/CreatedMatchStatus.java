package it.polimi.ingsw.network.messages.servertoclient;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

public class CreatedMatchStatus extends ServerToClientMessage {
    protected boolean created;
    public enum motive {NUMOFPLAYERS,NICKNAME,OTHER}
    protected motive m;


    public CreatedMatchStatus(ClientToServerMessage parent, boolean created,motive m) {
        super(parent);
        this.created = created;
        this.m = m;
    }

}

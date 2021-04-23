package it.polimi.ingsw.network.messages.servertoclient;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

public class CreatedMatchStatus extends ServerToClientMessage {
    protected boolean created;

    public CreatedMatchStatus(ClientToServerMessage parent, boolean created) {
        super(parent);
        this.created = created;
    }

}

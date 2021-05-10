package it.polimi.ingsw.network.messages.servertoclient;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.server.messages.clienttoserver.EventMessage;

public class EventNotValid extends ServerToClientMessage {
    public EventNotValid(ClientToServerMessage clientToServerMessage) {
        super(clientToServerMessage);
    }
}

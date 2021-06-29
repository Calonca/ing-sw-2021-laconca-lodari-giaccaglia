package it.polimi.ingsw.network.messages.servertoclient;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

public class EventNotValid extends ServerToClientMessage {

    public EventNotValid(){}

    public EventNotValid(ClientToServerMessage clientToServerMessage) {
        super(clientToServerMessage);
    }
}

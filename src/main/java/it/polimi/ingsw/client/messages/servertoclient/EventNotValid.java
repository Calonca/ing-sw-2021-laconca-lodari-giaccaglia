package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.io.IOException;

public class EventNotValid extends it.polimi.ingsw.network.messages.servertoclient.EventNotValid implements ClientMessage {
    public EventNotValid(ClientToServerMessage clientToServerMessage) {
        super(clientToServerMessage);
    }

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        System.out.println("Event not valid");
    }
}

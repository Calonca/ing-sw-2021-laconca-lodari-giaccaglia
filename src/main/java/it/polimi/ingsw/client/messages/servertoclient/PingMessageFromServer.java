package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;

import java.io.IOException;

public class PingMessageFromServer extends it.polimi.ingsw.network.messages.servertoclient.PingMessageFromServer implements ClientMessage{

    public PingMessageFromServer(String message) {
        super(message);
    }

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {

    }
}

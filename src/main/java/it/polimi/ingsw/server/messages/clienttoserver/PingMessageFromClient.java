package it.polimi.ingsw.server.messages.clienttoserver;

import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;

public class PingMessageFromClient extends it.polimi.ingsw.network.messages.clienttoserver.PingMessageFromClient implements ServerMessage {


    public PingMessageFromClient(String message) {
        super(message);
    }

    @Override
    public void processMessage(ClientHandler clientHandler) throws IOException {

    }
}

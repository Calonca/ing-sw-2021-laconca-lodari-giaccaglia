package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;

import java.io.IOException;

public class ElementsInNetwork extends it.polimi.ingsw.network.messages.servertoclient.state.ElementsInNetwork implements ClientMessage {

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        serverHandler.getClient().getSimpleModel().initializeSimpleModelWhenJoining(this);
    }
}
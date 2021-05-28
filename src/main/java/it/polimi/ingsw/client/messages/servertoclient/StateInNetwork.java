package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;

import java.io.IOException;
import java.util.List;

public class StateInNetwork extends it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork implements ClientMessage {
    public StateInNetwork(int playerNumber, String state, List<SimpleModelElement> playerSimpleModelElements, List<SimpleModelElement> commonSimpleModelElements) {
        super(playerNumber, state, playerSimpleModelElements, commonSimpleModelElements);
    }

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        serverHandler.getClient().setState(this);
    }
}

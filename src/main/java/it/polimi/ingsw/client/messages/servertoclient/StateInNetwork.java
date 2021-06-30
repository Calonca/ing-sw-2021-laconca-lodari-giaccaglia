package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;

import java.io.IOException;
import java.util.List;


/**
 * This is a message to set a state on the client, based on their last message and state
 */
public class StateInNetwork extends it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork implements ClientMessage {

    public StateInNetwork(int playerIndex, int currentPlayerIndex, String state, List<SimpleModelElement> playerSimpleModelElements, List<SimpleModelElement> commonSimpleModelElements) {

        super(playerIndex, currentPlayerIndex, state, playerSimpleModelElements, commonSimpleModelElements);
    }

    /**
     * This method forces the Client on a certain view, according to the server rules
     * @param serverHandler is the corresponding Client's ServerHandler
     * @throws IOException
     */

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        serverHandler.getClient().setState(this);
    }
}

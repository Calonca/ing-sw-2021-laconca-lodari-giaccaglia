package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;

/**
 * This is a message to initialize the SimpleModel
 */
public class ElementsInNetwork extends it.polimi.ingsw.network.messages.servertoclient.state.ElementsInNetwork implements ClientMessage {


    /**
     * This method initializes the basic network elements for a later update
     * @param serverHandler is the corresponding Client's ServerHandler
     */
    @Override
    public void processMessage(ServerHandler serverHandler) {
        serverHandler.getClient().getSimpleModel().initializeSimpleModelWhenJoining(this);
    }

}
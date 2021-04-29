package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.InitialPhaseView;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.servertoclient.State;

import java.io.IOException;

public class StateMessage extends it.polimi.ingsw.network.messages.servertoclient.StateMessage implements ClientMessage {


    /**
     * Initializes the answer message.
     *
     * @param command          The CommandMsg being answered.
     * @param player
     * @param state
     * @param serializedObject
     */
    public StateMessage(ClientToServerMessage command, int player, State state, String serializedObject) {
        super(command, player, state, serializedObject);
    }

    /**
     * Method invoked in the client to process the message.
     *
     * @param serverHandler
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        serverHandler.getClient().setState(player,state,serializedObject);
        //Todo the following line in PlayerCache
        serverHandler.getClient().transitionToView(new InitialPhaseView(serializedObject));
    }
}

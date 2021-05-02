package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.PlayerCache;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.SetupPhase;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;

import java.io.IOException;
import java.util.Map;

public class StateMessage extends it.polimi.ingsw.network.messages.servertoclient.StateMessage implements ClientMessage {


    /**
     * Initializes the answer message.
     *
     * @param command          The CommandMsg being answered.
     * @param player
     * @param state
     * @param serializedObject
     */
    public StateMessage(ClientToServerMessage command, int player, String state, Map<String, Object> serializedObject) {
        super(command, player, state, serializedObject);
    }

    /**
     * Initializes a ServerToClientMessage that doesn't answer any messages.
     *
     * @param player
     * @param state
     * @param serializedObject
     */
    public StateMessage(int player, String state, Map<String, Object> serializedObject) {
        super(player, state, serializedObject);
    }

    /**
     * Method invoked in the client to process the message.
     *
     * @param serverHandler
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        //serverHandler.getClient().setState(player,state,serializedObject);
        if (state.equals(PlayerCache.SETUP_PHASE))
            serverHandler.getClient().transitionToView(new SetupPhase());
    }
}

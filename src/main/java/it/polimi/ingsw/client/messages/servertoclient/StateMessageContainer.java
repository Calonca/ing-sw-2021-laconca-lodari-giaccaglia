package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.servertoclient.state.StateMessage;

import java.io.IOException;

public class StateMessageContainer extends it.polimi.ingsw.network.messages.servertoclient.state.StateMessageContainer implements ClientMessage{
    public StateMessageContainer(ClientToServerMessage command, StateMessage stateMessage) {
        super(command, stateMessage);
    }

    public StateMessageContainer(StateMessage stateMessage) {
        super(stateMessage);
    }

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        serverHandler.getClient().setState(stateMessage.getPlayer(),stateMessage.getClass().getSimpleName(),stateMessage);
    }
}

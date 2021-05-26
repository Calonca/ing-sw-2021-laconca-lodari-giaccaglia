package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;

import java.io.IOException;

public class StateMessage extends it.polimi.ingsw.network.messages.servertoclient.state.StateMessage implements ClientMessage{

    public StateMessage(ClientToServerMessage command, StateInNetwork stateInNetwork) {
        super(command, stateInNetwork);
    }

    public StateMessage(StateInNetwork stateInNetwork) {
        super(stateInNetwork);
    }

    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
     // todo restore code
        //  serverHandler.getClient().setState(stateInNetwork.getPlayer(), stateInNetwork.getClass().getSimpleName(), stateInNetwork);
    }
}

package it.polimi.ingsw.network.messages.servertoclient.state;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage;

public class StateMessageContainer extends ServerToClientMessage {
    protected StateMessage stateMessage;

    public StateMessageContainer(ClientToServerMessage command, StateMessage stateMessage) {
        super(command);
        this.stateMessage = stateMessage;
    }

    public StateMessageContainer(StateMessage stateMessage) {
        super();
        this.stateMessage = stateMessage;
    }
}

package it.polimi.ingsw.network.messages.servertoclient.state;

import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage;

public class StateMessage extends ServerToClientMessage {
    protected StateInNetwork stateInNetwork;

    public StateMessage(ClientToServerMessage command, StateInNetwork stateInNetwork) {
        super(command);
        this.stateInNetwork = stateInNetwork;
    }

    public StateMessage(StateInNetwork stateInNetwork) {
        super();
        this.stateInNetwork = stateInNetwork;
    }
}

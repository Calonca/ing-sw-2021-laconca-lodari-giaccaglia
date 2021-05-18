package it.polimi.ingsw.network.messages.servertoclient.state;

import it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage;


/**
 * A message containing the state of player and the objects associated with that state.<br>
 * Each message has the same name of the state it contains.
 */
public abstract class StateInNetwork extends ServerToClientMessage {

    private int player;

    public int getPlayer() {
        return player;
    }

    public static String getState(StateInNetwork stateInNetwork){
        return stateInNetwork.getClass().getSimpleName();
    }

    public StateInNetwork(){}

    public StateInNetwork(int player) {
        super();
        this.player = player;
    }

}

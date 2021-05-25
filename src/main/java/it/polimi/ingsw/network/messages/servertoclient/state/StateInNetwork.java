package it.polimi.ingsw.network.messages.servertoclient.state;

import it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;
import it.polimi.ingsw.server.model.states.State;

import java.util.List;

/**
 * A message containing the state of player and the objects associated with that state.<br>
 * Each message has the same name of the state it contains.
 */
public class StateInNetwork extends ServerToClientMessage {

    private int playerNumber;
    private State state;
    private List<SimpleModelElement> playerSimpleModelElements;
    private List<SimpleModelElement> commonSimpleModelElements;

    public int getPlayerNumber() {
        return playerNumber;
    }

    public static String getState(StateInNetwork stateInNetwork){
        return stateInNetwork.getClass().getSimpleName();
    }

    public StateInNetwork(){}


    public StateInNetwork(int playerNumber, State state, List<SimpleModelElement> playerSimpleModelElements, List<SimpleModelElement> commonSimpleModelElements) {

        super();
        this.playerNumber = playerNumber;
        this.state = state;
        this.playerSimpleModelElements = playerSimpleModelElements;
        this.commonSimpleModelElements = commonSimpleModelElements;

    }



}

package it.polimi.ingsw.network.messages.servertoclient.state;

import it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;

import java.util.List;

/**
 * A message containing the state of player and the objects associated with that state.<br>
 * Each message has the same name of the state it contains.
 */
public class StateInNetwork extends ServerToClientMessage {

    /**
     * The index in the players array of the player receiving a state update
     */
    private int playerNumber;
    private String state;
    private List<SimpleModelElement> playerSimpleModelElements;
    private List<SimpleModelElement> commonSimpleModelElements;


    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getState(){
        return state;
    }

    public StateInNetwork(){}


    public StateInNetwork(int playerNumber, String state, List<SimpleModelElement> playerSimpleModelElements, List<SimpleModelElement> commonSimpleModelElements) {

        super();
        this.playerNumber = playerNumber;
        this.state = state;
        this.playerSimpleModelElements = playerSimpleModelElements;
        this.commonSimpleModelElements = commonSimpleModelElements;

    }

    public List<SimpleModelElement> getPlayerSimpleModelElements(){
        return playerSimpleModelElements;
    }

    public List<SimpleModelElement> getCommonSimpleModelElements(){
        return commonSimpleModelElements;
    }

}

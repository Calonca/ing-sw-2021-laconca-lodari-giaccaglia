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
    protected int numberOfPlayerSendingEvent;
    protected int currentPlayerNumber;
    protected String state;
    protected List<SimpleModelElement> playerSimpleModelElements;
    protected List<SimpleModelElement> commonSimpleModelElements;


    public int getNumberOfPlayerSendingEvent() {
        return numberOfPlayerSendingEvent;
    }

    public int getCurrentPlayerNumber(){
        return currentPlayerNumber;
    }

    public String getState(){
        return state;
    }

    public StateInNetwork(){}


    public StateInNetwork(int numberOfPlayerSendingEvent, int currentPlayerNumber, String state, List<SimpleModelElement> playerSimpleModelElements, List<SimpleModelElement> commonSimpleModelElements) {

        super();
        this.numberOfPlayerSendingEvent = numberOfPlayerSendingEvent;
        this.currentPlayerNumber = currentPlayerNumber;
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

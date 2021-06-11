package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;

public class FinalProductionPhaseEvent extends Event {

    protected int actionToPerform; // 0 to produce, 1 to go back to middle phase deselecting all choices

    public FinalProductionPhaseEvent(){}

    public FinalProductionPhaseEvent(int actionToPerform){
        this.actionToPerform = actionToPerform;
    }
}

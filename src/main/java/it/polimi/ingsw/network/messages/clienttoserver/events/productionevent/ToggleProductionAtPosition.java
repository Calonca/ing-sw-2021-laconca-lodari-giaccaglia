package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;


public class ToggleProductionAtPosition extends Event {

    protected int productionPosition;

    public ToggleProductionAtPosition(int productionPosition){

       this.productionPosition = productionPosition;
    }

    public ToggleProductionAtPosition(){}


}

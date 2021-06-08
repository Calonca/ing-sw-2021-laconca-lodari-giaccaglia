package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

public class ChooseProductionAtPositionEvent extends ProductionEvent {

    public ChooseProductionAtPositionEvent(int productionPosition){
        super(productionPosition);
    }

    public ChooseProductionAtPositionEvent(){}


}

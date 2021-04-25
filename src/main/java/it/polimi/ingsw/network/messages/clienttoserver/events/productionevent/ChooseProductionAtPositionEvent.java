package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

public class ChooseProductionAtPositionEvent extends ProductionEvent {

    protected int productionPosition;

    public ChooseProductionAtPositionEvent(int position){
            this.productionPosition = position;
    }

    public ChooseProductionAtPositionEvent(){}


}

package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

public class ChooseProductionAtPositionEvent extends ProductionEvent {

    protected int cardPosition;

    public ChooseProductionAtPositionEvent(int position){
            this.cardPosition = position;
    }

    public ChooseProductionAtPositionEvent(){}


}

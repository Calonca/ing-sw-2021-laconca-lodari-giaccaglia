package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.MiddlePhaseEvent;
import it.polimi.ingsw.server.model.cards.production.Production;

public class ProductionEvent extends MiddlePhaseEvent {

    protected int productionPosition;

    public ProductionEvent(){}

    public ProductionEvent(int productionPosition){
        this.productionPosition = productionPosition;
    }

    public int getProductionPosition(){
        return productionPosition;
    }

}

package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ChooseResourcesForProductionEvent extends ProductionEvent {

    protected int chosenResource = -1;

    protected boolean isBasicProduction;

    protected Map<Integer,Integer> resourcesToDiscard; //key = posizione nei depositi , value = numero della risorsa

    public ChooseResourcesForProductionEvent(Map<Integer,Integer> resourcesToDiscard, int chosenResource, boolean isBasicProduction){
        this.resourcesToDiscard = resourcesToDiscard;
        this.chosenResource = chosenResource;
        this.isBasicProduction = isBasicProduction;
    }

    public ChooseResourcesForProductionEvent(){}

}

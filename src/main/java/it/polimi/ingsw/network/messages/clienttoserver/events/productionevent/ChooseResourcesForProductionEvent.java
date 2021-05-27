package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import javafx.util.Pair;

import java.util.List;

public class ChooseResourcesForProductionEvent extends ProductionEvent {

    protected int chosenResource;

    protected List<Pair<Integer,Integer>>resourcesToDiscard; //key = posizione nei depositi , value = numero della risorsa

    public ChooseResourcesForProductionEvent(List<Pair<Integer,Integer>> resourcesToDiscard, int chosenResource){
        this.resourcesToDiscard = resourcesToDiscard;
        this.chosenResource = chosenResource;
    }

    public ChooseResourcesForProductionEvent(){}

}

package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import java.util.List;

public class ChooseResourcesForProductionEvent extends ProductionEvent {

    protected int [] chosenResources;

    public ChooseResourcesForProductionEvent(List<Integer> resources){
        chosenResources = resources.stream().mapToInt(i->i).toArray();
    }

    public ChooseResourcesForProductionEvent(){}

}

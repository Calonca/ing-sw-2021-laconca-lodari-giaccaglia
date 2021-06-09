package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ChooseResourcesForProductionEvent extends ProductionEvent {

    protected List<Integer> positionsOfResourcesToConvert;
                                                                                                                        //set to -1 if not present
    public ChooseResourcesForProductionEvent(List<Integer> positionsOfResourcesToConvert, int productionPosition, int chosenResourceForSpecialProduction){
        super(productionPosition);
        this.positionsOfResourcesToConvert = positionsOfResourcesToConvert;
    }

    public ChooseResourcesForProductionEvent(){}

}

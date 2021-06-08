package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ChooseResourcesForProductionEvent extends ProductionEvent {

    protected int chosenResource = -1;
    protected List<Integer> positionsOfResourcesToConvert;

    public ChooseResourcesForProductionEvent(List<Integer> positionsOfResourcesToConvert, int productionPosition, int chosenResourceForBasicProduction){
        super(productionPosition);
        this.positionsOfResourcesToConvert = positionsOfResourcesToConvert;
        this.chosenResource = chosenResourceForBasicProduction;
    }

    public ChooseResourcesForProductionEvent(){}

}

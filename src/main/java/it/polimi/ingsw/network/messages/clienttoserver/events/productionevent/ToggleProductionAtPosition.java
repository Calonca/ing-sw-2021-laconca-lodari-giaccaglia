package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import java.util.List;

public class ToggleProductionAtPosition extends ProductionEvent {

    protected int actionToPerform = -1;  //0 to deselect, 1 to select, 2 to end selection

    protected int chosenResource = -1;
    protected List<Integer> positionsOfResourcesToConvertForSpecialProduction;


    public ToggleProductionAtPosition(int productionPosition, int actionToPerform){

        super(productionPosition);
        this.actionToPerform = actionToPerform;

    }

    public ToggleProductionAtPosition(){}

    // if chosen production has selection on input or output, resources integers are attached to the toggling event.

    public void setPositionsOfResourcesToConvertForSpecialProduction(List<Integer> positionsOfResourcesToConvertForSpecialProduction){
        this.positionsOfResourcesToConvertForSpecialProduction = positionsOfResourcesToConvertForSpecialProduction;
    }


    public void setChosenResourceToObtain(int chosenResource){
        this.chosenResource = chosenResource;
    }




}

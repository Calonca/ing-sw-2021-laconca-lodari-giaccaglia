package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;

import java.util.List;

public class ChooseResourcesForProductionEvent extends Event {

    protected List<Integer> inputPositionsToChoose;
    protected List<Integer> outputResourcesToChoose;

    public ChooseResourcesForProductionEvent(List<Integer> inputPositionsToChoose, List<Integer> outputResourceToChoose){
        this.inputPositionsToChoose = inputPositionsToChoose;
        this.outputResourcesToChoose = outputResourceToChoose;
    }



    public ChooseResourcesForProductionEvent(){}

}

package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ChooseResourcesForProductionEvent extends Event {

    protected List<Integer> inputPositionsToChoose;
    protected List<Integer> outputResourceToChoose;

    public ChooseResourcesForProductionEvent(List<Integer> inputPositionsToChoose, List<Integer> outputResourceToChoose){
        this.inputPositionsToChoose = inputPositionsToChoose;
        this.outputResourceToChoose = outputResourceToChoose;
    }



    public ChooseResourcesForProductionEvent(){}

}

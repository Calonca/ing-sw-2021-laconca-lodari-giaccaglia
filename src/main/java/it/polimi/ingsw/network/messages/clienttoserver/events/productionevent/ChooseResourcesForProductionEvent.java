package it.polimi.ingsw.network.messages.clienttoserver.events.productionevent;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ChooseResourcesForProductionEvent extends Event {

    protected List<Integer> inputPositionsToChoose;
    protected List<Integer> outputResourceToChoose;

    public ChooseResourcesForProductionEvent(List<Integer> inputPositionsToChoose){
       this.inputPositionsToChoose = inputPositionsToChoose;
    }

    public ChooseResourcesForProductionEvent(List<Integer> inputPositionsToChoose, List<Integer> outputPositionsToChoose){
        this.inputPositionsToChoose = inputPositionsToChoose;
    }



    public ChooseResourcesForProductionEvent(){}

}

package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class SimpleDiscardBox extends SimpleModelElement{

    Map<Integer, Pair<ResourceAsset, Integer>> simpleDiscardBox;

    private Map<Integer, List<Integer>> availableMovingPositions;

    private boolean isDiscardable;

    public SimpleDiscardBox(){}

    public SimpleDiscardBox(Map<Integer, Pair<ResourceAsset, Integer>> simpleStrongBox, Map<Integer, List<Integer>> availableMovingPositions, boolean isDiscardable){
        this.simpleDiscardBox = simpleStrongBox;
        this.isDiscardable = isDiscardable;
        this.availableMovingPositions = availableMovingPositions;
    }

    @Override
    public void update(SimpleModelElement element) {

        SimpleDiscardBox serverDiscardBox = (SimpleDiscardBox) element;
        this.simpleDiscardBox = serverDiscardBox.simpleDiscardBox;
        this.isDiscardable = serverDiscardBox.isDiscardable;
        this.availableMovingPositions = serverDiscardBox.availableMovingPositions;

    }

    public boolean isDiscardable() {
        return isDiscardable;
    }

    public Map<Integer, Pair<ResourceAsset, Integer>> getResourceMap() {
        return simpleDiscardBox;
    }

    public Map<Integer, List<Integer>> getAvailableMovingPositions(){
        return availableMovingPositions;
    }

}

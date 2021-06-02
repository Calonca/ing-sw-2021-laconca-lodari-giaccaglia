package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.Map;

public class SimpleDiscardBox extends SimpleModelElement{

    Map<Integer, Pair<ResourceAsset, Integer>> simpleDiscardBox;

    public SimpleDiscardBox(){}

    public SimpleDiscardBox(Map<Integer, Pair<ResourceAsset, Integer>> simpleStrongBox){
        this.simpleDiscardBox = simpleStrongBox;
    }

    public Map<Integer, Pair<ResourceAsset, Integer>> getResourceMap() {
        return simpleDiscardBox;
    }

    @Override
    public void update(SimpleModelElement element) {
        SimpleDiscardBox serverDiscardBox = (SimpleDiscardBox) element;
        this.simpleDiscardBox = serverDiscardBox.simpleDiscardBox;
    }

}

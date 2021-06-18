package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.Map;

public class SimpleStrongBox extends SimpleModelElement {

    private Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> simpleStrongBox;

    public SimpleStrongBox(){}

    public SimpleStrongBox(Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>>  simpleStrongBox){
        this.simpleStrongBox = simpleStrongBox;
    }

    //         Pos           Res                 number   selected
    public Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> getResourceMap() {
        return simpleStrongBox;
    }

    /**
     * Used to get the number of resources or number of selected resources in the strongbox
     */
    public static Pair<Integer, Integer> numAndSel(Map.Entry<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> e) {
        return e.getValue().getValue();
    }

    @Override
    public void update(SimpleModelElement element) {
        SimpleStrongBox serverStrongBox = (SimpleStrongBox) element;
        this.simpleStrongBox = serverStrongBox.simpleStrongBox;
    }

}

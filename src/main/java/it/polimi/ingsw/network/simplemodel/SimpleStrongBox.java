package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class SimpleStrongBox extends SimpleModelElement {

    private Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> simpleBox;

    public SimpleStrongBox(){}

    public SimpleStrongBox(Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>>  simpleStrongBox){
        this.simpleBox = simpleStrongBox;
    }

    //         Pos           Res                 number   selected
    public Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> getResourceMap() {
        if(Objects.isNull(simpleBox))
            return Collections.emptyMap();

        return simpleBox;
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
        this.simpleBox = serverStrongBox.simpleBox;
    }

}

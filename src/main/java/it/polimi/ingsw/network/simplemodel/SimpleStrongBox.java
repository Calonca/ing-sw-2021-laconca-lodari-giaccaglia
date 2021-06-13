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

    @Override
    public void update(SimpleModelElement element) {
        SimpleStrongBox serverStrongBox = (SimpleStrongBox) element;
        this.simpleStrongBox = serverStrongBox.simpleStrongBox;
    }

}

package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.Map;

public class SimpleStrongBox extends SimpleModelElement {

    Map<ResourceAsset, Integer> simpleStrongBox;

    public SimpleStrongBox(){}

    public SimpleStrongBox(Map<ResourceAsset, Integer> simpleStrongBox){
        this.simpleStrongBox = simpleStrongBox;
    }

    @Override
    public void update(SimpleModelElement element) {
        SimpleStrongBox serverStrongBox = (SimpleStrongBox) element;
        this.simpleStrongBox = serverStrongBox.simpleStrongBox;
    }


}

package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.Map;

public class SimpleStrongBox extends SimpleModelElement {

    Map<ResourceAsset, Integer> simpleStrongBox;

    @Override
    public Map<ResourceAsset, Integer> getElement(){
        return simpleStrongBox;
    }

    @Override
    public void update(SimpleModelElement element) {
        this.simpleStrongBox = (Map<ResourceAsset, Integer>) element.getElement();
    }

}

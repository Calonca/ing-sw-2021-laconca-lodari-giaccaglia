package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;

import java.util.List;
import java.util.Map;

public class SimpleCardCells extends SimpleModelElement{

    private Map<Integer, List<DevelopmentCardAsset>> visibleCardsOnCells;

    @Override
    public Map<Integer, List<DevelopmentCardAsset>> getElement(){
        return visibleCardsOnCells;
    }

    public void update(SimpleModelElement element){
        this.visibleCardsOnCells = (Map<Integer, List<DevelopmentCardAsset>>) element.getElement();
    }

}

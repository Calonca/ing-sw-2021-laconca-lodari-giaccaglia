package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;

import java.util.List;
import java.util.Map;

public class SimpleCardCells extends SimpleModelElement{

    private Map<Integer, List<DevelopmentCardAsset>> visibleCardsOnCells;


    public void update(SimpleModelElement element){
        SimpleCardCells serverCardCells = (SimpleCardCells) element;
        this.visibleCardsOnCells = serverCardCells.visibleCardsOnCells;
    }

}

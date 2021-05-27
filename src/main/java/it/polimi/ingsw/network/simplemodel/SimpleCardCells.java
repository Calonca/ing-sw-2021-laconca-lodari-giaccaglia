package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimpleCardCells extends SimpleModelElement{

    private Map<Integer, List<DevelopmentCardAsset>> visibleCardsOnCells;

    public SimpleCardCells(){}

    public SimpleCardCells(Map<Integer, List<UUID>> cards){
        this.visibleCardsOnCells = cards
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                position -> position,
                position -> cards.get(position).stream().map(Cards::getDevelopmentCardAsset).collect(Collectors.toList())
        ));
    }


    public void update(SimpleModelElement element){
        SimpleCardCells serverCardCells = (SimpleCardCells) element;
        this.visibleCardsOnCells = serverCardCells.visibleCardsOnCells;
    }

}

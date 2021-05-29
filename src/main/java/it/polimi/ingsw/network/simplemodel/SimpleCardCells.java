package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimpleCardCells extends SimpleModelElement{

    private Map<Integer, List<DevelopmentCardAsset>> visibleCardsOnCells;

    public SimpleCardCells(){}

    public SimpleCardCells(Map<Integer, List<Pair<UUID, Boolean>>> cards){

        this.visibleCardsOnCells = cards
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                position -> position,
                position -> cards.get(position).stream().map(cardPair -> {

                DevelopmentCardAsset card = Cards.getDevelopmentCardAsset(cardPair.getKey());
                card.getDevelopmentCard().setSelectable(cardPair.getValue());
                return card;

                }).collect(Collectors.toList())
        ));
    }


    public void update(SimpleModelElement element){
        SimpleCardCells serverCardCells = (SimpleCardCells) element;
        this.visibleCardsOnCells = serverCardCells.visibleCardsOnCells;
    }

}

package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimpleCardCells extends SimpleModelElement{

    private Map<Integer, List<DevelopmentCardAsset>> visibleCardsOnCells;

    private Map<Integer, Boolean> availableSpotsForCard;

    public SimpleCardCells(){}

    public SimpleCardCells(Map<Integer, List<Pair<UUID, Boolean>>> cards,  Map<Integer, Boolean> availableSpotsForCard){

        this.availableSpotsForCard = availableSpotsForCard;

        this.visibleCardsOnCells = cards
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                position -> position,
                position -> cards.get(position).stream().map(cardPair -> {

                DevelopmentCardAsset card = Cards.getDevelopmentCardAsset(cardPair.getKey()).get();
                card.getDevelopmentCard().setSelectable(cardPair.getValue());
                return card;

                }).collect(Collectors.toList())
        ));
    }


    public void update(SimpleModelElement element){
        SimpleCardCells serverCardCells = (SimpleCardCells) element;
        this.visibleCardsOnCells = serverCardCells.visibleCardsOnCells;
    }

    public Map<Integer, DevelopmentCardAsset> getVisibleCardsOnCells(){

        return visibleCardsOnCells.keySet().stream().collect(Collectors.toMap(

                position -> position,
                position -> visibleCardsOnCells.get(position).get(0)
        ));

    }

    public boolean isSpotAvailable(int position){

        if(availableSpotsForCard.get(position)!=null)
            return availableSpotsForCard.get(position);
        else
            return false;
    }

}

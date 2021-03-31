package it.polimi.ingsw.server.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CardShop {

    private Map<DevelopmentCardColor, List<DevelopmentCardDeck> > devDecks;

    public void discardCard(DevelopmentCardColor color, int amount){

        for(int i=0; i<amount; i++){

            for(int j=0; j<devDecks.get(color).size(); j++) {

                if (!devDecks.get(color).get(i).isDeckEmpty()) {
                    devDecks.get(color).get(i).getCard();
                    break;
                }
            }
        }


    }

    public boolean isSomeColourOutOfStock(){
        return Arrays.stream(DevelopmentCardColor.values())
                .map((color -> devDecks.get(color)))
                .flatMap(List::stream)
                .anyMatch(deck ->isColorOutOfStock(deck.getColor()));
    }

    public DevelopmentCardColor getColourOutOfStock(){
        return Arrays.stream(DevelopmentCardColor.values())
                .map((color -> devDecks.get(color)))
                .flatMap(List::stream)
                .filter(deck ->isColorOutOfStock(deck.getColor()))
                .findFirst()
                .get()
                .getColor();
    }

    public boolean isColorOutOfStock(DevelopmentCardColor color){
        return devDecks.get(color).stream().anyMatch(DevelopmentCardDeck::isDeckEmpty);
    }

}

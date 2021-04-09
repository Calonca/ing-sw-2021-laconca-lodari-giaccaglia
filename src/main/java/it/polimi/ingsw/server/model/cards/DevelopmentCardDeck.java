package it.polimi.ingsw.server.model.cards;
import java.util.List;

public class DevelopmentCardDeck {


    private int level;
    private DevelopmentCardColor color;
    private List<DevelopmentCard> deck;
    private int deckCapacity;
    private int deckSize;

    public DevelopmentCardDeck(int level, DevelopmentCardColor color, List<DevelopmentCard> deck, int deckCapacity, int deckSize) {
        this.level = level;
        this.color = color;
        this.deck = deck;
        this.deckCapacity = deckCapacity;
        this.deckSize = deckSize;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public boolean isDeckEmpty(){
        return deckSize == 0;
    }

    public DevelopmentCard getCard(){
        deckSize--;
        return deck.remove(deckSize);
    }


  /*  public DevelopmentCardColor getColor(){
        return color;
    } */


}

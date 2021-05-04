package it.polimi.ingsw.server.model.cards;
import java.util.List;

/**
 * {@link DevelopmentCard DevelopmentCards} container representing a {@link CardShop} deck, containing cards
 * sharing same {@link DevelopmentCardColor} and level. Modeled as a stack using a {@link List}, methods on deck
 * include getting {@link DevelopmentCardDeck#getDeckSize <em>DeckSize</em>}, removing and returning
 * top Card with {@link DevelopmentCardDeck#getCard()} and check whether deck is empty or not.
 */
public class DevelopmentCardDeck {
    private int level;
    private DevelopmentCardColor color;
    private List<DevelopmentCard> deck;
    private int deckCapacity;
    private int deckSize;

    // basic constructor

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

    public DevelopmentCard getCardCopy(){
        int position = deckSize;
        return new DevelopmentCard(deck.get(position-1));
    }


  /*  public DevelopmentCardColor getColor(){
        return color;
    } */


}

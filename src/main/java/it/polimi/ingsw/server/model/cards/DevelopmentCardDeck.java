package it.polimi.ingsw.server.model.cards;

import java.util.List;
import java.util.Optional;

/**
 * {@link DevelopmentCard DevelopmentCards} container representing a {@link CardShop} deck, containing cards
 * sharing same {@link DevelopmentCardColor} and level. Modeled as a stack using a {@link List}, methods on deck
 * include getting {@link DevelopmentCardDeck#getDeckSize <em>DeckSize</em>}, removing and returning
 * top Card with {@link DevelopmentCardDeck#getCard()} and check whether deck is empty or not.
 */
public class DevelopmentCardDeck {
    private final List<DevelopmentCard> deck;
    private int deckSize;

    // basic constructor

    public DevelopmentCardDeck(List<DevelopmentCard> deck, int deckSize) {
        this.deck = deck;
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

    public DevelopmentCard getCardCopyOnTop(){
        int position = deckSize;
        return new DevelopmentCard(deck.get(position-1));
    }

    public Optional<DevelopmentCard> getCardCopyFromPosition(int position){

        return (position<deckSize && position>-1) ? Optional.of(new DevelopmentCard((deck.get(position))) )
                : Optional.empty();

    }


}

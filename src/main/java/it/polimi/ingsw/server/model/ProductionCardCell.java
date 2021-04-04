package it.polimi.ingsw.server.model;

import java.util.List;

/**
 * Container class for Development Cards, similiar to a stack
 */

public class ProductionCardCell {
    private DevelopmentCard cardOnTop;
    private int maxsize;
    public List<DevelopmentCard> stackedCards;
    /**
     * Returns the card on top of the stack.
     * CANNOT be called on empty stack.
     */
    public DevelopmentCard getFrontCard()
    {
        return cardOnTop;
    }

    /**
     * Returns True if more cards can be added to the stack.
     *
     */
    public boolean isSpotAvailable()
    {
        return stackedCards.size()<maxsize;
    }

    /**
     * Adds one to the stack
     * @param card!=NULL
     * Ensures that the stack size increases by one and that "card" is the front card.
     */
    public void addToTop(DevelopmentCard card)
    {
        cardOnTop=card;
        stackedCards.add(card);
    }
}

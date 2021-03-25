package it.polimi.ingsw.server.model;

/**
 * Container class for Development Cards, similiar to a stack
 * @author Gianmarco Lodari
 */

public class ProductionCardCell {
    private DevelopmentCard[] cards;
    public int stackHeight;

    /**
     * Returns the card on top of the stack.
     */
    public DevelopmentCard getFrontCard()
    {
        return cards[stackHeight];
    }

    /**
     * Returns True if more cards can be added to the stack.
     */
    public boolean isSpotAvailable()
    {
        return stackHeight < 3;
    }

    /**
     * Adds one to the stack
     * @param card!=NULL
     * Ensures that the stack size increases by one and that "card" is the front card.
     */
    public void addToTop(DevelopmentCard card)
    {
        cards[stackHeight]=card;
        stackHeight++;
    }
}

package it.polimi.ingsw.server.model;

/**
 * Container class for Development Cards, similiar to a stack
 * @author Gianmarco Lodari
 */

public class ProductionCardCell {
    private DevelopmentCard[] cards;
    int stackHeight;

    /**
     * Returns the card on top of the stack
     */
    public DevelopmentCard getFrontCard()
    {
        return cards[stackHeight];
    }

    /**
     * Checks if the stack is full
     */
    public boolean isSpotAvailable()
    {
        return stackHeight < 3;
    }

    /**
     * Adds one to the stack
     * @param card!=NULL
     */
    public void addToTop(DevelopmentCard card)
    {
        cards[stackHeight]=card;
        stackHeight++;
    }
}

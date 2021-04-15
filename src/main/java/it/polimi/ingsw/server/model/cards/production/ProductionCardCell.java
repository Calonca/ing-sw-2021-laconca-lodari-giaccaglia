package it.polimi.ingsw.server.model.cards.production;

import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for Development Cards, similiar to a stack
 */

public class ProductionCardCell {
    private DevelopmentCard cardOnTop;
    private int maxsize=3;


    public List<DevelopmentCard> stackedCards;

    public ProductionCardCell()
    {
        this.stackedCards=new ArrayList<>();
    }


    public int getStackedCardsSize() {
        return stackedCards.size();
    }

    public int getMaxsize() {
        return maxsize;
    }

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
    public boolean isSpotAvailable(DevelopmentCard card)
    {
        return stackedCards.size()<maxsize&&card.getLevel()==stackedCards.size()+1;
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
    /**
     * Returns the number of cards of the requested color
     * @param color!=NULL
     */
    public int howManyOfColor(DevelopmentCardColor color, int level)
    {

        int temp=0;
        for (DevelopmentCard stackedCard : stackedCards) {
            if (stackedCard.getCardType() == color)
                if(stackedCard.getLevel() >= level)
                    temp++;
        }
        return temp;
    }

}

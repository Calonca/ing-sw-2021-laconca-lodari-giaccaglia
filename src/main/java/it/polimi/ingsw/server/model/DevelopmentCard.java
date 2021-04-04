package it.polimi.ingsw.server.model;

import java.util.List;

/**
 * Class for Development Cards. Since the color is not strictly tied to the input/output we totally decoupled that
 */
public class DevelopmentCard
{


    /**
     * Enum to indicate card color
     */
    private DevelopmentCardColor cardType;
    /**
     * Production is the class to effectively use the card, yet to be added
     */
    //private Production production;
    // private CostCell costcell;

    private int level;

    public DevelopmentCard(int level, DevelopmentCardColor cardType)
    {
        this.level = level;
        this.cardType = cardType;
    }

    public DevelopmentCardColor getCardType() {
        return cardType;
    }


}

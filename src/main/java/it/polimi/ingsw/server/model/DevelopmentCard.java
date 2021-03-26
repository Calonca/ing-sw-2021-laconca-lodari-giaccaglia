package it.polimi.ingsw.server.model;

/**
 * Class for Development Cards. Since the color is not strictly tied to the input/output we totally decoupled that
 */
public class DevelopmentCard
{
    public DevelopmentCard(int level, DevelopmentCardColor cardType)
    {
        this.level = level;
        this.cardType = cardType;
    }

    /**
     * Production is the class to effectively use the card, yet to be added
     */
    private int level;

    //private Production production;
    /**
     * Enum to indicate card color
     */
    private DevelopmentCardColor cardType;

    // private CostCell costcell;



}

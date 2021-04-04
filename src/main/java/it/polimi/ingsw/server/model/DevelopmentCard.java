package it.polimi.ingsw.server.model;

import javafx.util.Pair;

import java.util.List;

/**
 * Class for Development Cards. Since the color is not strictly tied to the input/output we totally decoupled that
 */
public class DevelopmentCard
{


    /**
     * Enum to indicate card color
     */
    private final DevelopmentCardColor cardType;
    /**
     * Production is the class to effectively use the card
     */
    private Production production;
    private List<Pair<Resource,Integer>> costList;
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

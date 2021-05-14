package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for Development Cards. Since the color is not strictly tied to the input/output we totally decoupled that
 */
public class DevelopmentCard
{
    /**
     * Production is the class to effectively use the card
     */
    protected Production production;

    /**
     * Enum to indicate card color
     */
    protected DevelopmentCardColor cardType;
    protected List<Pair<Resource,Integer>> costList;
    protected int victoryPoints;
    protected int level;
    public int getLevel() {
        return level;
    }

    public DevelopmentCard(){}

    public DevelopmentCard(int level, DevelopmentCardColor cardType)
    {
        this(level,cardType,Production.basicProduction(),1);
    }

    public DevelopmentCard(int level, DevelopmentCardColor cardType,Production production)
    {
        this(level,cardType,production,1);
    }

    public DevelopmentCard(int level, DevelopmentCardColor cardType,Production production, int victoryPoints)
    {
        this(level,cardType,production,victoryPoints,new ArrayList<>());
    }
    public DevelopmentCard(int level, DevelopmentCardColor cardType,Production production, int victoryPoints, List<Pair<Resource,Integer>> costList)
    {
        this.production = production;
        this.level = level;
        this.cardType = cardType;
        this.victoryPoints=victoryPoints;
        this.costList=costList;
    }

    public DevelopmentCard(DevelopmentCard another) {
        this.production = another.production;
        this.level = another.level;
        this.cardType = another.cardType;
        this.victoryPoints= another.victoryPoints;
        this.costList= another.costList;
    }

    public DevelopmentCardColor getCardType() {
        return cardType;
    }

    public List<Pair<Resource,Integer>> getCostList(){
        return costList;
    }

    public Production getProduction(){return production;}
    public int[] getCostAsArray() {
        int[] toar = new int[4];
        for (Pair<Resource, Integer> resourceIntegerPair : costList)
            toar[resourceIntegerPair.getKey().getResourceNumber()] += resourceIntegerPair.getValue();

        return toar;
    }

}

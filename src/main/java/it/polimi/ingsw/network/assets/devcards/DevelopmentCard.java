package it.polimi.ingsw.network.assets.devcards;

import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.production.Production;
import javafx.util.Pair;
import java.util.List;

public class DevelopmentCard {
    /**
     * Enum to indicate card color
     */
    protected DevelopmentCardColor cardType;
    /**
     * Production is the class to effectively use the card
     */
    protected Production production;
    protected List<Pair<Resource,Integer>> costList;
    protected int victoryPoints;
    protected int level;

    public DevelopmentCard(){}

}

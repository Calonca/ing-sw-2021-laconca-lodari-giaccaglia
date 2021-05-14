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
    protected List<Pair<Resource,Integer>> costList;
    protected int victoryPoints;
    protected int level;

    public DevelopmentCard(){}

    public int getLevel() {
        return level;
    }
    public DevelopmentCardColor getCardType() {
        return cardType;
    }
    public List<Pair<Resource,Integer>> getCostList(){
        return costList;
    }


}

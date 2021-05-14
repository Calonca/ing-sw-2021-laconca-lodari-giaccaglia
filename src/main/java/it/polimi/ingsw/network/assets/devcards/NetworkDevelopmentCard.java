package it.polimi.ingsw.network.assets.devcards;

import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;
import java.util.List;

public class NetworkDevelopmentCard {

    /**
     * Enum to indicate card color
     */
    protected NetworkDevelopmentCardColor cardType;
    protected List<Pair<Resource,Integer>> costList;
    protected int victoryPoints;
    protected int level;

    public NetworkDevelopmentCard(){}

    public int getLevel() {
        return level;
    }
    public NetworkDevelopmentCardColor getCardType() {
        return cardType;
    }

    public List<Pair<Resource,Integer>> getCostList(){
        return costList;
    }


}

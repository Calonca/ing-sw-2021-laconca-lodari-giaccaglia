package it.polimi.ingsw.network.assets.devcards;

import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;
import java.util.List;
import java.util.UUID;

public class NetworkDevelopmentCard {

    /**
     * Enum to indicate card color
     */
    private NetworkDevelopmentCardColor cardType;
    private List<Pair<Resource,Integer>> costList;
    private int victoryPoints;
    private int level;
    private UUID cardId;
    private boolean selectable;

    public NetworkDevelopmentCard(){}

    public void setSelectable(boolean isSelectable){
        this.selectable = isSelectable;
    }

    public UUID getCardId(){
        return cardId;
    }

    public int getLevel() {
        return level;
    }

    public boolean isSelectable(){
        return selectable;
    }
    public NetworkDevelopmentCardColor getCardType() {
        return cardType;
    }

    public List<Pair<Resource,Integer>> getCostList(){
        return costList;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }




}

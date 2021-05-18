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
    private boolean purchasable;


    public NetworkDevelopmentCard(){}

    public void setPurchasable(boolean isPurchasable){
        this.purchasable = isPurchasable;
    }
    public UUID getCardId(){
        return cardId;
    }

    public int getLevel() {
        return level;
    }

    public NetworkDevelopmentCardColor getCardType() {
        return cardType;
    }

    public List<Pair<Resource,Integer>> getCostList(){
        return costList;
    }

    public boolean isPurchasable(){
        return purchasable;
    }

}

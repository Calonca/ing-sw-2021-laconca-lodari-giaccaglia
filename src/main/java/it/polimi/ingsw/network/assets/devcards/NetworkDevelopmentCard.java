package it.polimi.ingsw.network.assets.devcards;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;
import java.util.List;
import java.util.UUID;

public class NetworkDevelopmentCard {

    /**
     * Enum to indicate card color
     */
    private NetworkDevelopmentCardColor cardType;
    private List<Pair<ResourceAsset,Integer>> costList;
    private List<Integer> inputResources;
    private List<Integer> outputResources;
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

    public List<Pair<ResourceAsset,Integer>> getCostList(){
        return costList;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public List<Integer> getInputResources(){
        return inputResources;
    }

    public List<Integer> getOutputResources(){
        return outputResources;
    }



}

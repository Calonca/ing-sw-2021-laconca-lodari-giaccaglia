package it.polimi.ingsw.network.assets.devcards;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class NetworkDevelopmentCard {

    /**
     * Enum to indicate card color
     */
    private NetworkDevelopmentCardColor cardType;
    private List<Pair<ResourceAsset,Integer>> costList;
    private List<Pair<ResourceAsset, Integer>> discountedCostList; //if there are no discounts, same as costList
    private Map<Integer, Integer> productionInputResources;
    private Map<Integer, Integer> productionOutputResources;
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

    public List<Pair<ResourceAsset, Integer>> getDiscountedCostList(){
        return discountedCostList;
    }

    public void setDiscountedCost(List<Pair<ResourceAsset, Integer>> discountedCostList){
        this.discountedCostList = discountedCostList;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public Map<ResourceAsset, Integer> getProductionInputResources() {
        return productionInputResources.entrySet().stream().collect(Collectors.toMap(
                entry -> ResourceAsset.fromInt(entry.getKey()),
                Map.Entry::getValue
        ));
    }

    public Map<ResourceAsset, Integer> getProductionOutputResources() {
        return productionOutputResources.entrySet().stream().collect(Collectors.toMap(
                entry -> ResourceAsset.fromInt(entry.getKey()),
                Map.Entry::getValue
        ));
    }
}

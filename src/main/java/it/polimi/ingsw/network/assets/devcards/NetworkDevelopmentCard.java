package it.polimi.ingsw.network.assets.devcards;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NetworkDevelopmentCard {

    /**
     * Enum to indicate card color
     */
    private NetworkDevelopmentCardColor cardType;
    private List<Pair<ResourceAsset,Integer>> costList;
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

package it.polimi.ingsw.network.messages.servertoclient.state;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FINAL_PHASE extends StateInNetwork{

    Map<Integer, List<ResourceAsset>> simpleWarehouseLeadersDepot;
    Map<ResourceAsset, Integer> simpleStrongBox;
    Map<Integer, List<UUID>> visibleCardsOnCells;
    String trackConfig;

    public FINAL_PHASE(Map<Integer, List<ResourceAsset>> simpleWarehouseLeadersDepot, Map<ResourceAsset, Integer> simpleStrongBox, Map<Integer, List<UUID>> visibleCardsOnCells, String trackConfig){

        this.simpleWarehouseLeadersDepot = simpleWarehouseLeadersDepot;
        this.simpleStrongBox = simpleStrongBox;
        this.visibleCardsOnCells = visibleCardsOnCells;
        this.trackConfig = trackConfig;
    }

    public Map<Integer, List<ResourceAsset>> getSimpleWarehouseLeadersDepot() {
        return simpleWarehouseLeadersDepot;
    }

    public Map<ResourceAsset, Integer> getSimpleStrongBox() {
        return simpleStrongBox;
    }

    public Map<Integer, List<UUID>> getVisibleCardsOnCells() {
        return visibleCardsOnCells;
    }

    public String getTrackConfig() {
        return trackConfig;
    }

}

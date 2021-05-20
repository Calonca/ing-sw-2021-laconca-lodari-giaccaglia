package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.List;
import java.util.Map;

public class SimpleDepots {

    Map<Integer, List<ResourceAsset>> simpleWarehouseLeadersDepot;
    Map<ResourceAsset, Integer> simpleStrongBox;

    public SimpleDepots(Map<Integer, List<ResourceAsset>> simpleWarehouseLeadersDepot, Map<ResourceAsset, Integer> simpleStrongBox){
        updateSimpleDepots(simpleWarehouseLeadersDepot, simpleStrongBox);
    }

    public void updateSimpleDepots(Map<Integer, List<ResourceAsset>> simpleWarehouseLeadersDepot, Map<ResourceAsset, Integer> simpleStrongBox){
        this.simpleStrongBox = simpleStrongBox;
        this.simpleWarehouseLeadersDepot = simpleWarehouseLeadersDepot;
    }
    public Map<Integer, List<ResourceAsset>> getSimpleWarehouseLeadersDepot() {
        return simpleWarehouseLeadersDepot;
    }

    public Map<ResourceAsset, Integer> getSimpleStrongBox() {
        return simpleStrongBox;
    }
}

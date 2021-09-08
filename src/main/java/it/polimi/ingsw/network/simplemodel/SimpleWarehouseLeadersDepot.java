package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SimpleWarehouseLeadersDepot extends SimpleModelElement{

    private Map<Integer, List<Pair<ResourceAsset, Boolean>>> simpleDepot;

    private Map<Integer, List<Integer>> availableMovingPositions;

    private Map<Integer, ResourceAsset> resourcesTypesOfLeaderDepots;  // key -> leader depot spot ; value -> resource types

    public SimpleWarehouseLeadersDepot(){}

    public SimpleWarehouseLeadersDepot(Map<Integer, List<Pair<ResourceAsset, Boolean>>> simpleWarehouseLeadersDepot, Map<Integer, List<Integer>> availableMovingPositions,  Map<Integer, ResourceAsset> resourcesTypesOfLeaderDepots){
        this.simpleDepot = simpleWarehouseLeadersDepot;
        this.availableMovingPositions = availableMovingPositions;
        this.resourcesTypesOfLeaderDepots = resourcesTypesOfLeaderDepots;
    }

    public Map<Integer, List<Pair<ResourceAsset, Boolean>>> getDepots() {
        if(Objects.isNull(simpleDepot))
            return Collections.emptyMap();
        return simpleDepot;
    }

    public boolean isPosAvailable(int pos){
        return availableMovingPositions.containsKey(pos)&&!availableMovingPositions.get(pos).isEmpty();
    }

    @Override
    public void update(SimpleModelElement element) {
        SimpleWarehouseLeadersDepot serverWarehouseLeadersDepot= (SimpleWarehouseLeadersDepot) element;
        this.simpleDepot = serverWarehouseLeadersDepot.simpleDepot;
        this.availableMovingPositions = serverWarehouseLeadersDepot.availableMovingPositions;
        this.resourcesTypesOfLeaderDepots = serverWarehouseLeadersDepot.resourcesTypesOfLeaderDepots;
    }

    public Map<Integer, List<Integer>> getAvailableMovingPositions(){
        return availableMovingPositions;
    }

    public Map<Integer, ResourceAsset> getResourcesTypesOfLeaderDepots(){
        return resourcesTypesOfLeaderDepots;
    }

}

package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SimpleWarehouseLeadersDepot extends SimpleModelElement{

    private Map<Integer, List<Pair<ResourceAsset, Boolean>>> simpleWarehouseLeadersDepot;

    private Map<Integer, List<Integer>> availableMovingPositions;



    private Map<Integer, ResourceAsset> resourcesTypesOfLeaderDepots;  // key -> leader depot spot ; value -> resource types

    public SimpleWarehouseLeadersDepot(){}

    public SimpleWarehouseLeadersDepot(Map<Integer, List<Pair<ResourceAsset, Boolean>>> simpleWarehouseLeadersDepot, Map<Integer, List<Integer>> availableMovingPositions,  Map<Integer, ResourceAsset> resourcesTypesOfLeaderDepots){
        this.simpleWarehouseLeadersDepot = simpleWarehouseLeadersDepot;
        this.availableMovingPositions = availableMovingPositions;
        this.resourcesTypesOfLeaderDepots = resourcesTypesOfLeaderDepots;
    }

    public Map<Integer, List<Pair<ResourceAsset, Boolean>>> getDepots() {
        return simpleWarehouseLeadersDepot;
    }

    public boolean isPosAvailable(int pos){
        return availableMovingPositions.containsKey(pos)&&availableMovingPositions.get(pos).size()>0;
    }

    @Override
    public void update(SimpleModelElement element) {
        SimpleWarehouseLeadersDepot serverWarehouseLeadersDepot= (SimpleWarehouseLeadersDepot) element;
        this.simpleWarehouseLeadersDepot = serverWarehouseLeadersDepot.simpleWarehouseLeadersDepot;
        this.availableMovingPositions = serverWarehouseLeadersDepot.availableMovingPositions;
        this.resourcesTypesOfLeaderDepots = serverWarehouseLeadersDepot.resourcesTypesOfLeaderDepots;
    }

    public Map<Integer, List<Integer>> getAvailableMovingPositions(){
        return availableMovingPositions;
    }

}

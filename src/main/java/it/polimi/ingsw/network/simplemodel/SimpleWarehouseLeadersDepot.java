package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class SimpleWarehouseLeadersDepot extends SimpleModelElement{

    private Map<Integer, List<Pair<ResourceAsset, Boolean>>> simpleWarehouseLeadersDepot;

    private Map<Integer, List<Integer>> availableMovingPositions;

    public SimpleWarehouseLeadersDepot(){}

    public SimpleWarehouseLeadersDepot(Map<Integer, List<Pair<ResourceAsset, Boolean>>> simpleWarehouseLeadersDepot, Map<Integer, List<Integer>> availableMovingPositions ){
        this.simpleWarehouseLeadersDepot = simpleWarehouseLeadersDepot;
        this.availableMovingPositions = availableMovingPositions;
    }

    public Map<Integer, List<Pair<ResourceAsset, Boolean>>> getDepots() {
        return simpleWarehouseLeadersDepot;
    }

    @Override
    public void update(SimpleModelElement element) {
        SimpleWarehouseLeadersDepot serverWarehouseLeadersDepot= (SimpleWarehouseLeadersDepot) element;
        this.simpleWarehouseLeadersDepot = serverWarehouseLeadersDepot.simpleWarehouseLeadersDepot;
        this.availableMovingPositions = serverWarehouseLeadersDepot.availableMovingPositions;
    }

    public Map<Integer, List<Integer>> getAvailableMovingPositions(){
        return availableMovingPositions;
    }

}

package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class SimpleWarehouseLeadersDepot extends SimpleModelElement{

    Map<Integer, List<Pair<ResourceAsset, Boolean>>> simpleWarehouseLeadersDepot;

    public SimpleWarehouseLeadersDepot(){}

    public SimpleWarehouseLeadersDepot(Map<Integer, List<Pair<ResourceAsset, Boolean>>> simpleWarehouseLeadersDepot){
        this.simpleWarehouseLeadersDepot = simpleWarehouseLeadersDepot;
    }

    public Map<Integer, List<Pair<ResourceAsset, Boolean>>> getDepots() {
        return simpleWarehouseLeadersDepot;
    }

    @Override
    public void update(SimpleModelElement element) {
        SimpleWarehouseLeadersDepot serverWarehouseLeadersDepot= (SimpleWarehouseLeadersDepot) element;
        this.simpleWarehouseLeadersDepot = serverWarehouseLeadersDepot.simpleWarehouseLeadersDepot;
    }


}

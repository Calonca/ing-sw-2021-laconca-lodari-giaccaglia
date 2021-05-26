package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.network.simplemodel.*;

import java.util.HashMap;
import java.util.Map;

public class PlayerCache {

    State currentState;

    Map<String , SimpleModelElement> playerSimpleModelElementsMap = new HashMap<>();

    public void updateSimpleModelElement(String name, SimpleModelElement element){
        playerSimpleModelElementsMap.get(name).update(element);
    }

    public PlayerCache(){

        playerSimpleModelElementsMap.put(SimplePlayerLeaders.class.getSimpleName(), new SimplePlayerLeaders());
        playerSimpleModelElementsMap.put(SimpleFaithTrack.class.getSimpleName(), new SimpleFaithTrack());
        playerSimpleModelElementsMap.put(SimpleCardCells.class.getSimpleName(), new SimpleCardCells());
        playerSimpleModelElementsMap.put(SimpleWarehouseLeadersDepot.class.getSimpleName(), new SimpleWarehouseLeadersDepot());
        playerSimpleModelElementsMap.put(SimpleStrongBox.class.getSimpleName(), new SimpleStrongBox());
    }


}

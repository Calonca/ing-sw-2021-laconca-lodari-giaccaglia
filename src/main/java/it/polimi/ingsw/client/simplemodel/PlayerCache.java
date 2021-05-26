package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.network.simplemodel.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerCache {

    State currentState;

    Map<String , SimpleModelElement> playerSimpleModelElementsMap = new HashMap<>();

    public void updateSimpleModelElement(String name, SimpleModelElement element){
        playerSimpleModelElementsMap.get(name).update(element);
    }

    public <T extends SimpleModelElement> Optional<T> getDataFromState(Type type){
        Optional<SimpleModelElement> notCasted= Optional.ofNullable(playerSimpleModelElementsMap.getOrDefault(type.getClass().getSimpleName(),null));
        try {
            return (Optional<T>) notCasted;
        } catch (Exception e){
            return Optional.empty();
        }
    }

    public PlayerCache(){

        playerSimpleModelElementsMap.put(SimplePlayerLeaders.class.getSimpleName(), new SimplePlayerLeaders());
        playerSimpleModelElementsMap.put(SimpleFaithTrack.class.getSimpleName(), new SimpleFaithTrack());
        playerSimpleModelElementsMap.put(SimpleCardCells.class.getSimpleName(), new SimpleCardCells());
        playerSimpleModelElementsMap.put(SimpleWarehouseLeadersDepot.class.getSimpleName(), new SimpleWarehouseLeadersDepot());
        playerSimpleModelElementsMap.put(SimpleStrongBox.class.getSimpleName(), new SimpleStrongBox());
    }


}

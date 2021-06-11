package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.network.simplemodel.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayerCache {

    String currentState;
    private final PropertyChangeSupport support;

    Map<String , SimpleModelElement> playerSimpleModelElementsMap = new HashMap<>();

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void updateState(String state, List<SimpleModelElement> elems) {
        elems.forEach(this::updateSimpleModelElement);
        String oldState = currentState;
        currentState = state;
        support.firePropertyChange(currentState,"old:"+oldState,currentState);
    }

    public SimpleModelElement getElem(String name){
        return playerSimpleModelElementsMap.get(name);
    }

    public void updateSimpleModelElement(SimpleModelElement element){
        String elemName = element.getClass().getSimpleName();
        playerSimpleModelElementsMap.get(elemName).update(element);
        support.firePropertyChange(elemName,null,getElem(elemName));
    }

    public <T extends SimpleModelElement> Optional<T> getElem(Class<T> s){
        Optional<T> result;
        try {
            result = Optional.ofNullable(s.cast(playerSimpleModelElementsMap.getOrDefault(s.getSimpleName(),null)));
        } catch (ClassCastException e){
            result = Optional.empty();
        }
        return result;
    }

    public PlayerCache(){

        this.support = new PropertyChangeSupport(this);

        playerSimpleModelElementsMap.put(SimplePlayerLeaders.class.getSimpleName(), new SimplePlayerLeaders());
        playerSimpleModelElementsMap.put(SimpleFaithTrack.class.getSimpleName(), new SimpleFaithTrack());
        playerSimpleModelElementsMap.put(SimpleCardCells.class.getSimpleName(), new SimpleCardCells());
        playerSimpleModelElementsMap.put(SimpleWarehouseLeadersDepot.class.getSimpleName(), new SimpleWarehouseLeadersDepot());
        playerSimpleModelElementsMap.put(SimpleStrongBox.class.getSimpleName(), new SimpleStrongBox());
        playerSimpleModelElementsMap.put(SimpleDiscardBox.class.getSimpleName(), new SimpleDiscardBox());
        playerSimpleModelElementsMap.put(ActiveLeaderBonusInfo.class.getSimpleName(), new ActiveLeaderBonusInfo());
        playerSimpleModelElementsMap.put(SimpleProductions.class.getSimpleName(), new SimpleProductions());

        SimplePlayerLeaders playerLeadersElement = ((SimplePlayerLeaders) playerSimpleModelElementsMap.get(SimplePlayerLeaders.class.getSimpleName()));
        SimpleCardCells simpleCardCells = ((SimpleCardCells) playerSimpleModelElementsMap.get(SimpleCardCells.class.getSimpleName()));

        simpleCardCells.setSimplePlayerLeaders(playerLeadersElement);

    }

    public String getCurrentState() {
        return currentState;
    }
}

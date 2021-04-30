package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.State;
import javafx.util.Pair;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerCache {

    static final String setup = "Setup";

    Map<String, Pair<String,Runnable>[]> stateToStateCode;

    Map<String, Map<String, Object>> stateData;

    private final PropertyChangeSupport support;
    public PlayerCache(int player,int totalPlayers){
        support = new PropertyChangeSupport(this);
        stateData = new HashMap<>();
        stateData.put(setup,new HashMap<>());
        stateData.get(setup).put("o1",false);
        stateToStateCode = new HashMap<>();
        //Todo initialize table

    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public <T> Optional<T> getFromStateAndKey(String state, String key){
        Optional<Object> notCasted= Optional.ofNullable(stateData.getOrDefault(state,null))
                .map(map->map.getOrDefault(key,null));
        try {
            return (Optional<T>) notCasted;
        } catch (Exception e){
            return Optional.empty();
        }

    }

    public void update(String state, Map<String, Object> serializedObject) {
        //support.firePropertyChange(state.toString(), this.oldValue,newValue);
        //this.oldValue = newValue;
    }
}

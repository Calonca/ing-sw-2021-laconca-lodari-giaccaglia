package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.InitialPhase;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Contains the data related to each game state and the transition function from state to state
 */
public class PlayerCache {

    public static final String SETUP_PHASE = "SETUP_PHASE";
    public static final String INITIAL_PHASE = "INITIAL_PHASE";

    private Map<String, Map<String,Runnable>> stateToStateTransition;
    private Map<String, Map<String, Object>> stateData;

    private final PropertyChangeSupport support;
    private final Client client;

    public PlayerCache(int player,int totalPlayers, Client client){
        this.client = client;
        support = new PropertyChangeSupport(this);
        stateData = new HashMap<>();
        stateData.put(SETUP_PHASE,new HashMap<>());

        stateToStateTransition = new HashMap<>();
        stateToStateTransition.put(SETUP_PHASE,new HashMap<>());
        stateToStateTransition.get(SETUP_PHASE).put(INITIAL_PHASE, ()->client.transitionToView(new InitialPhase()));
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
        support.firePropertyChange(state, stateData.get(state), serializedObject);
        stateData.put(state,serializedObject);
    }
}

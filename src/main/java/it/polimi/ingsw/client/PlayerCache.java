package it.polimi.ingsw.client;

import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Contains the data related to each game state and the transition function from state to state
 */
public class PlayerCache {

    private Map<String, StateInNetwork> stateData;

    private final PropertyChangeSupport support;
    private final Client client;

    public PlayerCache(int player,int totalPlayers, Client client){
        this.client = client;
        support = new PropertyChangeSupport(this);
        stateData = new HashMap<>();
        //Todo initialize table
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }


    public <T extends StateInNetwork> Optional<T> getDataFromState(String state){
        Optional<StateInNetwork> notCasted= Optional.ofNullable(stateData.getOrDefault(state,null));
        try {
            return (Optional<T>) notCasted;
        } catch (Exception e){
            return Optional.empty();
        }
    }

    public void update(String state, StateInNetwork stateInNetwork) {
        StateInNetwork oldState = stateData.get(state);
        stateData.put(state, stateInNetwork);
        support.firePropertyChange(state, oldState, stateInNetwork);
    }
}

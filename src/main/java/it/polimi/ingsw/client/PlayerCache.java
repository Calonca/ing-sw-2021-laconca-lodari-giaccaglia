package it.polimi.ingsw.client;

import it.polimi.ingsw.network.messages.servertoclient.State;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PlayerCache {

    private final PropertyChangeSupport support;
    public PlayerCache(){
        support = new PropertyChangeSupport(this);
        //Todo initialize table
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }


    public void update(State state, String serializedObject) {
        //support.firePropertyChange(state.toString(), this.oldValue,newValue);
        //this.oldValue = newValue;
    }
}

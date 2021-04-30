package it.polimi.ingsw.client;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;


/**
 * Useful data before the start of the game.
 * When changing the view observers are automatically added
 * To register a new observable property add a method like setMatchesData.<br>
 * To automatically update the view use the method:<br>
 * public void propertyChange(PropertyChangeEvent evt) {<br>
 *         if (evt.getPropertyName().equals("matchesData"))<br>
 *             System.out.println(Optional<Pair<UUID,String[]>[]>) evt.getNewValue(); //This is the new value, you need to cast it to use it<br>
 * }<br>
 */
public class CommonData {
    private Optional<Map<UUID,String[]>> matchesData = Optional.empty();

    public Optional<Map<UUID,String[]>> getMatchesData() {
        return matchesData;
    }

    private final PropertyChangeSupport support;
    public CommonData(){
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setMatchesData(Optional<Map<UUID,String[]>> newValue) {
        support.firePropertyChange("matchesData",this.matchesData,newValue);
        this.matchesData = newValue;
    }
}

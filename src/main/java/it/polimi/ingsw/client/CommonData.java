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
    public static String matchesDataString = "matchesData";
    public static String thisMatchData = "thisMatchData";
    public static String currentPlayerString = "currentPlayer";

    private Optional<Map<UUID,String[]>> matchesData = Optional.empty();
    private Integer thisPlayerIndex;
    private int currentPlayerIndex;
    private UUID matchId;


    public Optional<Integer> getThisPlayerIndex() {
        return Optional.ofNullable(thisPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Optional<UUID> getMatchId() {
        return Optional.ofNullable(matchId);
    }

    public Optional<Map<UUID,String[]>> getMatchesData() {
        return matchesData;
    }

    public Optional<String[]> playersOfMatch() {
        if (matchId==null) return Optional.empty();
        else return matchesData.map(data->data.getOrDefault(this.matchId,null));
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
        Optional<Map<UUID,String[]>> oldValue = matchesData;
        this.matchesData = newValue;
        support.firePropertyChange(matchesDataString,oldValue,newValue);
    }

    public void setCurrentPlayer(int currentPlayerIndex) {
        int oldPlayerIndex = this.currentPlayerIndex;
        this.currentPlayerIndex = currentPlayerIndex;
        support.firePropertyChange(currentPlayerString,oldPlayerIndex,currentPlayerIndex);
    }

    public void setStartData(UUID matchId,int thisPlayerIndex) {
        UUID oldMatchId = matchId;
        int oldPlayerIndex = thisPlayerIndex;

        this.matchId = matchId;
        this.thisPlayerIndex = thisPlayerIndex;

        support.firePropertyChange(thisMatchData,oldMatchId,matchId);
    }

}

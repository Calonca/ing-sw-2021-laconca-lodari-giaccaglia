package it.polimi.ingsw.client;

import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


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
    public static String connectionStatusString = "connectionStatus";
    public static boolean isSetupPhase = true;


    private Optional<Map<Pair<UUID,Boolean>,Pair<String[], String[]>>> matchesData = Optional.empty();

    private Map<UUID, Pair<String[], String[]>> availableMatchesData;
    private Map<UUID, Pair<String[], String[]>> savedMatchesData;

    private static Integer thisPlayerIndex;

    private int currentPlayerIndex = -1;
    private String currentPlayerNickname = "";
    private UUID matchId;
    private String currentNick;
    private boolean connectionStatus = false;
    private final PropertyChangeSupport support;


    /**
     * @return the callers' player index
     */
    public static Integer getThisPlayerIndex() {
        return thisPlayerIndex;
    }

    /**
     * @return the current player index
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * @return the match ID
     */
    public Optional<UUID> getMatchId() {
        return Optional.ofNullable(matchId);
    }

    public Optional<Map<Pair<UUID,Boolean>,Pair<String[], String[]>>> getMatchesData() {
        return matchesData;
    }

    public Optional<Map<UUID, Pair<String[], String[]>>> getAvailableMatchesData(){
        if(Objects.isNull( availableMatchesData) || availableMatchesData.isEmpty())
            return Optional.empty();

        return Optional.of(availableMatchesData);
    }

    public Optional<Map<UUID, Pair<String[], String[]>>> getSavedMatchesData(){
        if(Objects.isNull( savedMatchesData) || savedMatchesData.isEmpty())
            return Optional.empty();

        return Optional.of(savedMatchesData);
    }

    /**
     * @return the player already in the match, if any is present
     */
    public Optional<String[]> playersOfMatch() {

        if(Objects.nonNull(matchId) && matchesData.isPresent()){
            Pair<UUID, Boolean> key = matchesData.get().keySet().stream().filter(keyset -> keyset.getKey().equals(matchId)).findFirst().orElseGet(null);
            if(Objects.nonNull(key)){
                Pair<String[], String[]> matches = matchesData.get().get(key);
                return Optional.of(ArrayUtils.addAll(matches.getKey(), matches.getValue()));
            }
        }

        return Optional.empty();

    }


    public CommonData(){
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }


    /**
     * Method called when informations regarding the match are updated in any way
     */
    public void setMatchesData(Optional<Map<Pair<UUID,Boolean>,Pair<String[], String[]>>> newValue) {
        Optional<Map<Pair<UUID,Boolean>,Pair<String[], String[]>>> oldValue = matchesData;
        this.matchesData = newValue;
        buildMatchesDataMaps();
        support.firePropertyChange(matchesDataString,oldValue,newValue);
    }

    private void buildMatchesDataMaps(){

        if(matchesData.isPresent()) {
            availableMatchesData = matchesData.get().keySet().stream().filter(key -> !key.getValue()).collect(Collectors.toMap(
                    Pair::getKey,
                    key -> matchesData.get().get(key)
            ));

            savedMatchesData = matchesData.get().keySet().stream().filter(Pair::getValue).collect(Collectors.toMap(
                    Pair::getKey,
                    key -> matchesData.get().get(key)
            ));
        }
    }


    /**
     * Method used during transition
     * @param currentPlayerIndex is an existing and online player index
     */
    public void setCurrentPlayer(int currentPlayerIndex, String currentPlayerNickname) {
        int oldPlayerIndex = this.currentPlayerIndex;
        this.currentPlayerIndex = currentPlayerIndex;
        this.currentPlayerNickname = currentPlayerNickname;
        support.firePropertyChange(currentPlayerString,oldPlayerIndex,currentPlayerIndex);
    }

    /**
     * Method called to update a player's connection status
     * @param connectionStatus is true if online
     */
    public void setConnectionStatus(boolean connectionStatus){
        boolean oldConnectionStatus = this.connectionStatus;
        this.connectionStatus = connectionStatus;
        support.firePropertyChange(connectionStatusString, oldConnectionStatus, connectionStatus);
    }

    public void setStartData(UUID matchId,int thisPlayerIndex) {

        UUID oldMatchId = matchId;
        int oldPlayerIndex = thisPlayerIndex;

        this.matchId = matchId;
        this.thisPlayerIndex = thisPlayerIndex;

        support.firePropertyChange(thisMatchData,oldMatchId,matchId);
    }

    public void setThisPlayerNickname(String currentNick) {
        this.currentNick = currentNick;

    }

    public String getThisPlayerNickname() {
        return this.currentNick;
    }

    public String getCurrentPlayerNickname(){
        return currentPlayerNickname;
    }
}

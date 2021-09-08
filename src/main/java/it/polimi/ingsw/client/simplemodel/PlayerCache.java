package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.network.simplemodel.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;


/**
 * This class contains all the information regarding the players that the Client needs to play the game. It is updated by the Controller after each game phase.
 */
public class PlayerCache {

    private String currentState;
    private final PropertyChangeSupport support;

    private final Map<String , SimpleModelElement> playerSimpleModelElementsMap = new TreeMap<>();

    /**
     * Method to subscribe to the Property Change Support
     * @param pcl is a valid listener
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Method to unsubscribe to the Property Change Support
     * @param pcl is a valid listener
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    /**
     * This method is called to update some player-specific elements after each game stage
     */
    public void updatePlayerElements(List<SimpleModelElement> elements) {
        elements.forEach(this::updateSimpleModelElement);
    }


    /**
     * This method is called to update some elements after each game state
     * @param state is a string representing a valid state
     */
    public void updateState(String state){
        String oldState = currentState;
        currentState = state;
        if((!currentState.equals(State.SETUP_PHASE.toString()) && !currentState.equals(State.IDLE.toString())) && CommonData.isSetupPhase()){
            CommonData.setIsSetupPhase(false);
        }

        support.firePropertyChange(currentState,"old:"+oldState, currentState);
    }


    /**
     * Method used by the client to access player-specific informations
     * @param name element name
     * @return a valid SimpleModelElement
     */
    public SimpleModelElement getElem(String name){
        return playerSimpleModelElementsMap.get(name);
    }


    /**
     * This method is called to update some simple model elements after each game stage
     */
    public void updateSimpleModelElement(SimpleModelElement element){
        String elemName = element.getClass().getSimpleName();
        playerSimpleModelElementsMap.get(elemName).update(element);
        support.firePropertyChange(elemName,null,getElem(elemName));
    }


    /**
     * Method used by the client to access player-specific informations
     * @return a valid encapsulated SimpleModelElement, may be empty
     */
    public <T extends SimpleModelElement> Optional<T> getElem(Class<T> s){
        Optional<T> result;
        try {
            result = Optional.ofNullable(s.cast(playerSimpleModelElementsMap.getOrDefault(s.getSimpleName(),null)));
        } catch (ClassCastException e){
            result = Optional.empty();
        }
        return result;
    }


    /**
     * Method to initialize the Player Cache, containing all player-specific informations
     */
    public PlayerCache(){

        this.support = new PropertyChangeSupport(this);

        playerSimpleModelElementsMap.put(SimplePlayerLeaders.class.getSimpleName(), new SimplePlayerLeaders());
        playerSimpleModelElementsMap.put(SimpleFaithTrack.class.getSimpleName(), new SimpleFaithTrack());
        playerSimpleModelElementsMap.put(SimpleProductions.class.getSimpleName(), new SimpleProductions());
        playerSimpleModelElementsMap.put(SimpleCardCells.class.getSimpleName(), new SimpleCardCells());
        playerSimpleModelElementsMap.put(SimpleWarehouseLeadersDepot.class.getSimpleName(), new SimpleWarehouseLeadersDepot());
        playerSimpleModelElementsMap.put(SimpleStrongBox.class.getSimpleName(), new SimpleStrongBox());
        playerSimpleModelElementsMap.put(SimpleDiscardBox.class.getSimpleName(), new SimpleDiscardBox());
        playerSimpleModelElementsMap.put(ActiveLeaderBonusInfo.class.getSimpleName(), new ActiveLeaderBonusInfo());
        playerSimpleModelElementsMap.put(SelectablePositions.class.getSimpleName(), new SelectablePositions());
        playerSimpleModelElementsMap.put(SimpleSoloActionToken.class.getSimpleName(), new SimpleSoloActionToken());

        SimplePlayerLeaders playerLeadersElement = ((SimplePlayerLeaders) playerSimpleModelElementsMap.get(SimplePlayerLeaders.class.getSimpleName()));
        SimpleProductions simpleProductions = ((SimpleProductions) playerSimpleModelElementsMap.get(SimpleProductions.class.getSimpleName()));
        SimpleCardCells simpleCardCells = ((SimpleCardCells) playerSimpleModelElementsMap.get(SimpleCardCells.class.getSimpleName()));

        simpleCardCells.setSimplePlayerLeaders(playerLeadersElement);
        simpleCardCells.setSimpleProductions(simpleProductions);

        currentState = "BEFORE_SETUP";

    }

    /**
     * @return current player state
     */
    public String getCurrentState() {
        return currentState;
    }
}

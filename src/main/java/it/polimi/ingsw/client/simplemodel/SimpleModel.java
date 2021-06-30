package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.client.messages.servertoclient.ElementsInNetwork;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.network.simplemodel.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.IntStream;


/**
 * This class contains all the information regarding the state of the game that the Client needs to play. It is updated by the Controller after each game phase.
 */
public class SimpleModel {

    private static int numOfPlayers;

    private Map<String , SimpleModelElement> commonSimpleModelElementsMap = new HashMap<>();

    private final List<PlayerCache> playersCacheList;

    private final PropertyChangeSupport support;

    /**
     * Method to initializde the SimpleModel, containing all game-specific informations, shared by all players
     * @param numberOfPlayers is the number of players in the game
     */
    public SimpleModel(int numberOfPlayers){
        playersCacheList = new ArrayList<>(numberOfPlayers);
        support = new PropertyChangeSupport(this);
        numOfPlayers = numberOfPlayers;

        IntStream.range(0, numberOfPlayers).forEach(i -> playersCacheList.add(new PlayerCache()));

        commonSimpleModelElementsMap.put(SimpleCardShop.class.getSimpleName(), new SimpleCardShop());
        commonSimpleModelElementsMap.put(SimpleMarketBoard.class.getSimpleName(), new SimpleMarketBoard());
        commonSimpleModelElementsMap.put(EndGameInfo.class.getSimpleName(), new EndGameInfo());
        commonSimpleModelElementsMap.put(PlayersInfo.class.getSimpleName(), new PlayersInfo());
        commonSimpleModelElementsMap.put(VaticanReportInfo.class.getSimpleName(), new VaticanReportInfo());
    }


    public PlayerCache[] getPlayersCaches() {
        return playersCacheList.toArray(PlayerCache[]::new);
    }

    public PlayerCache getPlayerCache(int playerNumber){
        return playersCacheList.get(playerNumber);
    }
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
     * This method is called to update some simple model elements after each game stage
     */
    public void updateSimpleModelElement(String name, SimpleModelElement element){
        commonSimpleModelElementsMap.get(name).update(element);
    }

    public <T extends SimpleModelElement> Optional<T> getElem(Class<T> s){
        Optional<T> result;
        try {
            result = Optional.ofNullable(s.cast(commonSimpleModelElementsMap.getOrDefault(s.getSimpleName(),null)));
        } catch (ClassCastException e){
            result = Optional.empty();
        }
        return result;
    }

    private SimpleModelElement getElem(String name){
        return commonSimpleModelElementsMap.get(name);
    }

    /**
     * Method used by the client to access game-specific informations
     * @return a valid encapsulated SimpleModelElement, may be empty
     */
    public void updateSimpleModel(StateInNetwork stateInNetwork){

        PlayerCache playerCache = getPlayerCache(stateInNetwork.getNumberOfPlayerSendingEvent());
        playerCache.updatePlayerElements(stateInNetwork.getPlayerSimpleModelElements());

        for(SimpleModelElement element : stateInNetwork.getCommonSimpleModelElements()){
            String elemName = element.getClass().getSimpleName();
            updateSimpleModelElement(elemName, element);
            support.firePropertyChange(elemName,null,getElem(elemName));
        }

        playerCache.updateState(stateInNetwork.getState());


    }


    /**
     * Method used for the initialization on game start
     * @param elementsInNetwork represent the elements to update
     */
    public void initializeSimpleModelWhenJoining(ElementsInNetwork elementsInNetwork){

        for(SimpleModelElement element : elementsInNetwork.getCommonSimpleModelElements()){
            String elemName = element.getClass().getSimpleName();
            updateSimpleModelElement(elemName, element);
            support.firePropertyChange(elemName,null,getElem(elemName));
        }

        Map<Integer, List<SimpleModelElement>> playersElements = elementsInNetwork.getPlayerElements();

        playersElements.forEach((key, elements) -> {

            PlayerCache playerCache = getPlayerCache(key);

            for (SimpleModelElement element : elements)
                playerCache.updateSimpleModelElement(element);
        });
    }

    public int getNumOfPlayers(){
        return numOfPlayers;
    }

    public boolean isSinglePlayer(){
        return numOfPlayers==1;
    }

}

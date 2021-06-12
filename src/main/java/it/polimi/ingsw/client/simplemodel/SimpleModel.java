package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.network.simplemodel.*;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.IntStream;

public class SimpleModel {

    private static int numOfPlayers;

    private Map<String , SimpleModelElement> commonSimpleModelElementsMap = new HashMap<>();

    private List<PlayerCache> playersCacheList;

    private final PropertyChangeSupport support;

    public SimpleModel(int numberOfPlayers){
        playersCacheList = new ArrayList<>(numberOfPlayers);
        support = new PropertyChangeSupport(this);
        numOfPlayers = numberOfPlayers;

        IntStream.range(0, numberOfPlayers).forEach(i -> playersCacheList.add(new PlayerCache()));

        commonSimpleModelElementsMap.put(SimpleCardShop.class.getSimpleName(), new SimpleCardShop());
        commonSimpleModelElementsMap.put(SimpleMarketBoard.class.getSimpleName(), new SimpleMarketBoard());
        commonSimpleModelElementsMap.put(EndGameInfo.class.getSimpleName(), new EndGameInfo());
        commonSimpleModelElementsMap.put(PlayersInfo.class.getSimpleName(), new PlayersInfo());
    }

    public PlayerCache[] getPlayersCaches() {
        return playersCacheList.toArray(PlayerCache[]::new);
    }

    public PlayerCache getPlayerCache(int playerNumber){
        return playersCacheList.get(playerNumber);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

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


    public void updateSimpleModel(StateInNetwork stateInNetwork){
        for(SimpleModelElement element : stateInNetwork.getCommonSimpleModelElements()){
            String elemName = element.getClass().getSimpleName();
            updateSimpleModelElement(elemName, element);
            support.firePropertyChange(elemName,null,getElem(elemName));
        }

        getPlayerCache(stateInNetwork.getPlayerNumber()).updateState(stateInNetwork.getState(),stateInNetwork.getPlayerSimpleModelElements());
    }

    public static int getNumOfPlayers(){
        return numOfPlayers;
    }

}

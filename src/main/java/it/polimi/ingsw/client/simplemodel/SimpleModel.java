package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimpleMarketBoard;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class SimpleModel {

    Map<String , SimpleModelElement> commonSimpleModelElementsMap = new HashMap<>();

    List<PlayerCache> playersCacheList = new ArrayList<>();

    public SimpleModel(int numberOfPlayers){

       playersCacheList = new ArrayList<>(numberOfPlayers);

        IntStream.range(0, numberOfPlayers).forEach(i -> playersCacheList.add(new PlayerCache()));

        commonSimpleModelElementsMap.put(SimpleCardShop.class.getSimpleName(), new SimpleCardShop());
        commonSimpleModelElementsMap.put(SimpleMarketBoard.class.getSimpleName(), new SimpleMarketBoard());
    }

    public PlayerCache getPlayerCache(int playerNumber){
        return playersCacheList.get(playerNumber);
    }

    public void updateSimpleModelElement(String name, SimpleModelElement element){
        commonSimpleModelElementsMap.get(name).update(element);
    }


}

package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleCardShop {

    private Map<NetworkDevelopmentCardColor, Map<Integer, List<NetworkDevelopmentCard> >> simpleCardShop;

    public SimpleCardShop(){}

    public SimpleCardShop(Map<NetworkDevelopmentCardColor, Map<Integer, List<NetworkDevelopmentCard>>> simpleCardShop) {
        this.simpleCardShop = simpleCardShop;
    }

    public void updateSimpleCardShop(Map<NetworkDevelopmentCardColor, Map<Integer, List<NetworkDevelopmentCard> >> updatedSimpleCardShop){
        simpleCardShop = updatedSimpleCardShop;
    }

    public Optional<NetworkDevelopmentCard> getCardOnFront(NetworkDevelopmentCardColor color, int level){
      return Optional.of(simpleCardShop.get(color).get(level).get(0));
    }

    public Optional<NetworkDevelopmentCard> getCardOnBack(NetworkDevelopmentCardColor color, int level){
        return Optional.of(simpleCardShop.get(color).get(level).get(1));
    }

}

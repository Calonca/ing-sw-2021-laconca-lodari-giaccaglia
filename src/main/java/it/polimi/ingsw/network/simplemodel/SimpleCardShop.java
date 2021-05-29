package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleCardShop extends SimpleModelElement{

    private Map<NetworkDevelopmentCardColor, Map<Integer, List<NetworkDevelopmentCard> >> simpleCardShop;

    public SimpleCardShop(){}

    public SimpleCardShop(Map<NetworkDevelopmentCardColor, Map<Integer, List<NetworkDevelopmentCard>>> simpleCardShop) {
        this.simpleCardShop = simpleCardShop;
    }

    @Override
    public void update(SimpleModelElement element){
        SimpleCardShop serverCardShop = (SimpleCardShop) element;
        this.simpleCardShop = serverCardShop.simpleCardShop;
    }

    public Optional<NetworkDevelopmentCard> getCardOnFront(NetworkDevelopmentCardColor color, int level){
      return Optional.of(simpleCardShop.get(color).get(level).get(0));
    }

    public Optional<NetworkDevelopmentCard> getCardOnBack(NetworkDevelopmentCardColor color, int level){
        return Optional.of(simpleCardShop.get(color).get(level).get(1));
    }

}

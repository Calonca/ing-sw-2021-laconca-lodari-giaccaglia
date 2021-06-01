package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleCardShop extends SimpleModelElement{

    private Map<NetworkDevelopmentCardColor, Map<Integer, List<DevelopmentCardAsset> >> simpleCardShop;

    private DevelopmentCardAsset purchasedCard = null;

    public SimpleCardShop(){}

    public SimpleCardShop(Map<NetworkDevelopmentCardColor, Map<Integer, List<DevelopmentCardAsset>>> simpleCardShop, DevelopmentCardAsset purchasedCard) {
        this.simpleCardShop = simpleCardShop;
        this.purchasedCard = purchasedCard;
    }

    @Override
    public void update(SimpleModelElement element){
        SimpleCardShop serverCardShop = (SimpleCardShop) element;
        this.simpleCardShop = serverCardShop.simpleCardShop;
        this.purchasedCard = serverCardShop.purchasedCard;
    }


    public Optional<DevelopmentCardAsset> getCardFront(NetworkDevelopmentCardColor color, int level){
            return Optional.ofNullable(simpleCardShop.get(color).get(level).get(0));
    }

    public Optional<DevelopmentCardAsset> getCardBack(NetworkDevelopmentCardColor color, int level){
            return Optional.ofNullable(simpleCardShop.get(color).get(level).get(1));
    }

    public Optional<DevelopmentCardAsset> getPurchasedCard(){
            return Optional.of(purchasedCard);
    }

}

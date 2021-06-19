package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleCardShop extends SimpleModelElement{

    private Map<NetworkDevelopmentCardColor, Map<Integer, Pair<Integer, List<DevelopmentCardAsset> >>> simpleCardShop;

    private DevelopmentCardAsset purchasedCard = null;

    private boolean anyCardPurchasable;

    public SimpleCardShop(){}
                                                                            //size      //first 2 cards
    public SimpleCardShop(Map<NetworkDevelopmentCardColor, Map<Integer, Pair<Integer, List<DevelopmentCardAsset>>>> simpleCardShop, DevelopmentCardAsset purchasedCard) {
        this.simpleCardShop = simpleCardShop;
        this.purchasedCard = purchasedCard;
        anyCardPurchasable = isAnyCardPurchasable();
    }

    private boolean isAnyCardPurchasable() {

        return simpleCardShop.keySet().stream()
                .anyMatch(color ->

                        simpleCardShop.get(color).values().stream()

                                .anyMatch(cards ->

                                        cards.getValue().stream().anyMatch(asset -> asset.getDevelopmentCard().isSelectable())));
    }

    @Override
    public void update(SimpleModelElement element){
        SimpleCardShop serverCardShop = (SimpleCardShop) element;
        this.simpleCardShop = serverCardShop.simpleCardShop;
        this.purchasedCard = serverCardShop.purchasedCard;
        this.anyCardPurchasable = isAnyCardPurchasable();

    }


    public Optional<DevelopmentCardAsset> getCardFront(NetworkDevelopmentCardColor color, int level){
        return Optional.ofNullable(simpleCardShop.get(color).get(level).getValue().get(0));
    }

    public int getStackHeight(NetworkDevelopmentCardColor color, int level){
        return simpleCardShop.get(color).get(level).getKey();
    }

    public Optional<DevelopmentCardAsset> getSecondCard(NetworkDevelopmentCardColor color, int level){
        return Optional.ofNullable(simpleCardShop.get(color).get(level).getValue().get(1));
    }

    public Optional<DevelopmentCardAsset> getPurchasedCard(){
        return Optional.ofNullable(purchasedCard);
    }

    public boolean getIsAnyCardPurchasable(){
        return anyCardPurchasable;
    }


}

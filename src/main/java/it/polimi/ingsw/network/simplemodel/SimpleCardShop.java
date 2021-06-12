package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleCardShop extends SimpleModelElement{

    private Map<NetworkDevelopmentCardColor, Map<Integer, List<DevelopmentCardAsset> >> simpleCardShop;

    private DevelopmentCardAsset purchasedCard = null;

    private List<Pair<ResourceAsset,Integer>> costListWithDiscounts;

    private boolean anyCardPurchasable;

    public SimpleCardShop(){}

    public SimpleCardShop(Map<NetworkDevelopmentCardColor, Map<Integer, List<DevelopmentCardAsset>>> simpleCardShop, DevelopmentCardAsset purchasedCard, List<Pair<ResourceAsset,Integer>> costListWithDiscounts) {
        this.simpleCardShop = simpleCardShop;
        this.purchasedCard = purchasedCard;
        this.costListWithDiscounts = costListWithDiscounts;
        anyCardPurchasable = isAnyCardPurchasable();
    }

    private boolean isAnyCardPurchasable() {
        return simpleCardShop.keySet().stream()
                .anyMatch(color ->

                        simpleCardShop.get(color).values().stream()

                                .anyMatch(cards ->

                                        cards.stream().anyMatch(asset -> asset.getDevelopmentCard().isSelectable())));
    }

    @Override
    public void update(SimpleModelElement element){
        SimpleCardShop serverCardShop = (SimpleCardShop) element;
        this.simpleCardShop = serverCardShop.simpleCardShop;
        this.purchasedCard = serverCardShop.purchasedCard;
        this.costListWithDiscounts = serverCardShop.costListWithDiscounts;
        this.anyCardPurchasable = isAnyCardPurchasable();

    }


    public Optional<DevelopmentCardAsset> getCardFront(NetworkDevelopmentCardColor color, int level){
        return Optional.ofNullable(simpleCardShop.get(color).get(level).get(0));
    }

    public int getStackHeight(NetworkDevelopmentCardColor color, int level){
        return simpleCardShop.get(color).get(level).size();
    }

    public Optional<DevelopmentCardAsset> getSecondCard(NetworkDevelopmentCardColor color, int level){
        return Optional.ofNullable(simpleCardShop.get(color).get(level).get(1));
    }

    public Optional<DevelopmentCardAsset> getPurchasedCard(){
        return Optional.of(purchasedCard);
    }

    public boolean getIsAnyCardPurchasable(){
        return anyCardPurchasable;
    }

    public List<Pair<ResourceAsset,Integer>> getCostListWithDiscounts(){
        return costListWithDiscounts;
    }
}

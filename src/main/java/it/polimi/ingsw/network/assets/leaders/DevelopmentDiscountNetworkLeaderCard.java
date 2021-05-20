package it.polimi.ingsw.network.assets.leaders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

public class DevelopmentDiscountNetworkLeaderCard extends NetworkLeaderCard {

    Pair<ResourceAsset, Integer> resourcesDiscount;

    public DevelopmentDiscountNetworkLeaderCard() {
    }

    public void setResourcesDiscount(Pair<Integer, Integer> resourcesDiscount){
        this.resourcesDiscount = new Pair<>(ResourceAsset.fromInt(resourcesDiscount.getKey()), resourcesDiscount.getValue());
    }

    public Pair<ResourceAsset, Integer> getResourcesDiscount() {
        return resourcesDiscount;
    }

}

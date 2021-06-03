package it.polimi.ingsw.network.assets.leaders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

public class NetworkDevelopmentDiscountLeaderCard extends NetworkLeaderCard {

    Pair<ResourceAsset, Integer> resourcesDiscount;

    public NetworkDevelopmentDiscountLeaderCard() {
    }

    public void setResourcesDiscount(Pair<Integer, Integer> resourcesDiscount){
        this.resourcesDiscount = new Pair<>(ResourceAsset.fromInt(resourcesDiscount.getKey()), resourcesDiscount.getValue());
    }

    public Pair<ResourceAsset, Integer> getResourcesDiscount() {
        return resourcesDiscount;
    }

}

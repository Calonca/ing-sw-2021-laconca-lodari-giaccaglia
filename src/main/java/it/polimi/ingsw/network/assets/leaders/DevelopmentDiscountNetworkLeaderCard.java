package it.polimi.ingsw.network.assets.leaders;

import it.polimi.ingsw.network.assets.devcards.NetworkResource;
import javafx.util.Pair;

public class DevelopmentDiscountNetworkLeaderCard extends NetworkLeaderCard {

    Pair<NetworkResource, Integer> resourcesDiscount;

    public void setResourcesDiscount(Pair<Integer, Integer> resourcesDiscount){
        this.resourcesDiscount = new Pair<>(NetworkResource.fromInt(resourcesDiscount.getKey()), resourcesDiscount.getValue());
    }
}

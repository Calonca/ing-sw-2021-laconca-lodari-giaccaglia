package it.polimi.ingsw.network.assets.leaders;

import it.polimi.ingsw.network.assets.devcards.NetworkResource;

public class NetworkMarketLeaderCard extends NetworkLeaderCard {

    private NetworkResource marketBonusResource;

    public void setMarketBonusResource(int marketBonusResource){
        this.marketBonusResource = NetworkResource.fromInt(marketBonusResource);
    }
}

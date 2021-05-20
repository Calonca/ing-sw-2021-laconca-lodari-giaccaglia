package it.polimi.ingsw.network.assets.leaders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;

public class NetworkMarketLeaderCard extends NetworkLeaderCard {

    private ResourceAsset marketBonusResourceAsset;

    public NetworkMarketLeaderCard(){
    }

    public void setMarketBonusResource(int marketBonusResource){
        this.marketBonusResourceAsset = ResourceAsset.fromInt(marketBonusResource);
    }

    public ResourceAsset getMarketBonusNetworkResource(){
        return marketBonusResourceAsset;
    }

}

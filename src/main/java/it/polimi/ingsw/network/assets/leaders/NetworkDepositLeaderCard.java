package it.polimi.ingsw.network.assets.leaders;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;

public class NetworkDepositLeaderCard extends NetworkLeaderCard {

    private ResourceAsset resourcesTypeInDepot;

    public NetworkDepositLeaderCard(){}

    public void setResourcesTypeInDepot(int resourcesTypeInDepot) {
        this.resourcesTypeInDepot = ResourceAsset.fromInt(resourcesTypeInDepot);
    }
    public ResourceAsset getResourcesTypeInDepot(){
        return resourcesTypeInDepot;
    }

}

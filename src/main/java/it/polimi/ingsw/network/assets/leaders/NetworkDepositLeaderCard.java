package it.polimi.ingsw.network.assets.leaders;
import it.polimi.ingsw.network.assets.devcards.NetworkResource;

public class NetworkDepositLeaderCard extends NetworkLeaderCard {

    private NetworkResource resourcesTypeInDepot;

    public void setResourcesTypeInDepot(int resourcesTypeInDepot) {
        this.resourcesTypeInDepot = NetworkResource.fromInt(resourcesTypeInDepot);
    }
}

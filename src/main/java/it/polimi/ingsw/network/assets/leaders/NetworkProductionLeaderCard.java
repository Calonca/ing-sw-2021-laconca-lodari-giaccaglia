package it.polimi.ingsw.network.assets.leaders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.List;
import java.util.stream.Collectors;

public class NetworkProductionLeaderCard extends NetworkLeaderCard {

    private List<Integer> productionInputResources;
    private List<Integer> productionOutputResources;

    public NetworkProductionLeaderCard() {
    }

    public void setProductionInputResources(List<Integer> productionInputResources){
        this.productionInputResources = productionInputResources;
    }

    public void setProductionOutputResources(List<Integer> productionOutputResources){
        this.productionOutputResources = productionOutputResources;
    }

    public List<ResourceAsset> getProductionInputResources() {
        return productionInputResources.stream().map(ResourceAsset::fromInt).collect(Collectors.toList());
    }

    public List<ResourceAsset> getProductionOutputResources() {
        return productionOutputResources.stream().map(ResourceAsset::fromInt).collect(Collectors.toList());
    }

}

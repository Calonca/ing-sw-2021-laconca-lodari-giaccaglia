package it.polimi.ingsw.network.assets.leaders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.HashMap;
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

    public HashMap<ResourceAsset,Integer> getProductionInputResources() {
        return new HashMap<>();//return productionInputResources.stream().map(ResourceAsset::fromInt).collect(Collectors.toList());
    }

    public HashMap<ResourceAsset,Integer> getProductionOutputResources() {
        return new HashMap<>();//return productionOutputResources.stream().map(ResourceAsset::fromInt).collect(Collectors.toList());
    }

}

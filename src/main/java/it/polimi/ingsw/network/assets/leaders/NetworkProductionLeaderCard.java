package it.polimi.ingsw.network.assets.leaders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.server.model.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NetworkProductionLeaderCard extends NetworkLeaderCard {

    private Map<Integer, Integer> productionInputResources;
    private Map<Integer, Integer> productionOutputResources;

    public NetworkProductionLeaderCard() {
    }

    public void setProductionInputResources(Map<Integer, Integer> productionInputResources){
        this.productionInputResources = productionInputResources;
    }

    public void setProductionOutputResources(Map<Integer, Integer>productionOutputResources){
        this.productionOutputResources = productionOutputResources;
    }

    public Map<ResourceAsset, Integer> getProductionInputResources() {
        return productionInputResources.entrySet().stream().collect(Collectors.toMap(
                entry -> ResourceAsset.fromInt(entry.getKey()),
                Map.Entry::getValue
        ));
    }

    public Map<ResourceAsset, Integer> getProductionOutputResources() {
        return productionOutputResources.entrySet().stream().collect(Collectors.toMap(
                entry -> ResourceAsset.fromInt(entry.getKey()),
                Map.Entry::getValue
        ));
    }

}

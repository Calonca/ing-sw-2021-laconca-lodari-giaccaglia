package it.polimi.ingsw.network.assets.leaders;

import java.util.List;

public class NetworkProductionLeaderCard extends NetworkLeaderCard {

    private List<Integer> productionInputResources;
    private List<Integer> productionOutputResources;


    public void setProductionInputResources(List<Integer> productionInputResources){
        this.productionInputResources = productionInputResources;
    }

    public void setProductionOutputResources(List<Integer> productionOutputResources){
        this.productionOutputResources = productionOutputResources;
    }
}

package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ActiveLeaderBonusInfo extends SimpleModelElement{

    private List<ResourceAsset> resourcesInLeaderDepot = new ArrayList<>();
    private List<Pair<ResourceAsset, Integer>> discountedResources = new ArrayList<>();
    private List<ResourceAsset> marketBonusResources = new ArrayList<>();
    private List<SimpleProductionBonus> simpleProductionBonus;

    public ActiveLeaderBonusInfo(){}

    public ActiveLeaderBonusInfo(List<ResourceAsset> resourcesInLeaderDepot,
                                 List<Pair<ResourceAsset, Integer>> discountedResources,
                                 List<ResourceAsset> marketBonusResources,
                                 List<SimpleProductionBonus> simpleProductionBonus) {

        this.resourcesInLeaderDepot = resourcesInLeaderDepot;
        this.discountedResources = discountedResources;
        this.marketBonusResources = marketBonusResources;
        this.simpleProductionBonus = simpleProductionBonus;

    }

    @Override
    public void update(SimpleModelElement element) {

        ActiveLeaderBonusInfo serverElement = ((ActiveLeaderBonusInfo) element);
        this.resourcesInLeaderDepot = serverElement.resourcesInLeaderDepot;
        this.discountedResources = serverElement.discountedResources;
        this.marketBonusResources = serverElement.marketBonusResources;
        this.simpleProductionBonus = serverElement.simpleProductionBonus;

    }

    public List<ResourceAsset> getResourcesInLeaderDepot() {
        return resourcesInLeaderDepot;
    }

    public List<Pair<ResourceAsset, Integer>> getDiscountedResources() {
        return discountedResources;
    }

    public List<ResourceAsset> getMarketBonusResources() {
        return marketBonusResources;
    }

    public List<SimpleProductionBonus> getSimpleProductionBonus() {
        return simpleProductionBonus;
    }

    public static class SimpleProductionBonus
    {

        private final List<ResourceAsset> inputResources;
        private final List<ResourceAsset> outputResources;

        public SimpleProductionBonus(List<ResourceAsset> inputResources, List<ResourceAsset> outputResources){

            this.inputResources = inputResources;
            this.outputResources = outputResources;

        }

        public List<ResourceAsset> getInputResources() {
            return inputResources;
        }

        public List<ResourceAsset> getOutputResources() {
            return outputResources;
        }
    }

}

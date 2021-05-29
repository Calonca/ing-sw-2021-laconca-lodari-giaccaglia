package it.polimi.ingsw.network.assets.leaders;

import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.List;

public class NetworkLeaderCard {

    protected int victoryPoints;
    protected transient List<Pair<ResourceAsset, Integer>> requirementsResources;
    protected List<Pair<NetworkDevelopmentCardColor, Integer>> requirementsCards;
    protected int requirementsCardsLevel=1;
    protected boolean isActive;

    public NetworkLeaderCard(){}

    public List<Pair<ResourceAsset, Integer>> getRequirementsResources() {
        return requirementsResources;
    }

    public List<Pair<NetworkDevelopmentCardColor, Integer>> getRequirementsCards() {
        return requirementsCards;
    }

    public int getRequirementsCardsLevel() {
        return requirementsCardsLevel;
    }

    public boolean isLeaderActive(){
        return isActive;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setLeaderState(boolean state){
        this.isActive = state;
    }


}

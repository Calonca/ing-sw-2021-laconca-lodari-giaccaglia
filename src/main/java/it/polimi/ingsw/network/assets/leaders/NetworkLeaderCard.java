package it.polimi.ingsw.network.assets.leaders;

import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.List;

public class NetworkLeaderCard {

    protected int victoryPoints;
    protected List<Pair<ResourceAsset, Integer>> requirementsResources;
    protected List<Pair<NetworkDevelopmentCardColor, Integer>> requirementsCards;
    protected static final int REQUIREMENTS_CARDS_LEVEL =1;
    protected boolean isActive;
    protected boolean isPlayable;

    public List<Pair<ResourceAsset, Integer>> getRequirementsResources() {
        return requirementsResources;
    }

    public List<Pair<NetworkDevelopmentCardColor, Integer>> getRequirementsCards() {
        return requirementsCards;
    }

    public int getRequirementsCardsLevel() {
        return REQUIREMENTS_CARDS_LEVEL;
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

    public void setPlayableLeader(boolean isPlayable){
        this.isPlayable = isPlayable;
    }

    public boolean isPlayable(){
        return isPlayable;
    }

}

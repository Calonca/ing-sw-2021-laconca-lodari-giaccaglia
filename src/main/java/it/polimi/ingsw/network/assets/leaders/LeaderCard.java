package it.polimi.ingsw.network.assets.leaders;

import it.polimi.ingsw.network.assets.devcards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.leaders.LeaderState;
import javafx.util.Pair;

import java.util.List;

public class LeaderCard {

    protected LeaderState state;
    protected int victoryPoints;
    protected List<Pair<Resource, Integer>> requirementsResources;
    protected List<Pair<DevelopmentCardColor, Integer>> requirementsCards;
    protected int requirementsCardsLevel=1;

    public LeaderCard(){}

    public LeaderState getState(){
        return state;
    }

    public List<Pair<Resource, Integer>> getRequirementsResources() {
        return requirementsResources;
    }

    public List<Pair<DevelopmentCardColor, Integer>> getRequirementsCards() {
        return requirementsCards;
    }

    public int getRequirementsCardsLevel() {
        return requirementsCardsLevel;
    }

}

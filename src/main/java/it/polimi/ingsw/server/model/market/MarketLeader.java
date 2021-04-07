package it.polimi.ingsw.server.model.market;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Leader;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.LeaderState;
import javafx.util.Pair;

import java.util.List;

/**
 * This Leader class adds the resource choice to the player's resource market
 */

public class MarketLeader extends Leader
{
    private LeaderState state;
    public List<Pair<Resource, Integer>> requirementsResources;
    public List<Pair<DevelopmentCardColor, Integer>> requirementsCards;
    private Resource bonus;

    public MarketLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirementsResources, List<Pair<DevelopmentCardColor, Integer>> requirementsCards, Resource bonus)
    {
        this.state = state;
        this.victoryPoints = victoryPoints;
        this.requirementsCards = requirementsCards;
        this.requirementsResources = requirementsResources;
        this.bonus = bonus;
    }

    public void activate(GameModel gamemodel)
    {
        state = LeaderState.ACTIVE;
        //add market bonus
    }

}
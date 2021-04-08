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
    public int requirementsCardsLevel;
    private Resource bonus;


    public LeaderState getState()
    {
        return state;
    }

    public MarketLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirementsResources, List<Pair<DevelopmentCardColor, Integer>> requirementsCards, Resource bonus, int requirementsCardsLevel)
    {
        this.state = state;
        this.victoryPoints = victoryPoints;
        this.requirementsCards = requirementsCards;
        this.requirementsResources = requirementsResources;
        this.bonus = bonus;
        this.requirementsCardsLevel=requirementsCardsLevel;
    }

    public MarketLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirementsResources, List<Pair<DevelopmentCardColor, Integer>> requirementsCards, Resource bonus)
    {
        this(state, victoryPoints,requirementsResources,requirementsCards,bonus, 1);
    }

    public void activate(GameModel gamemodel)
    {
        state = LeaderState.ACTIVE;
        gamemodel.getCurrentPlayer().applyMarketBonus(bonus);
    }

    public void discard(GameModel gamemodel)
    {
        state = LeaderState.DISCARDED;
        gamemodel.getCurrentPlayer().moveOnePosition();
    }

    public boolean areRequirementsSatisfied(GameModel gamemodel)
    {
        int temp=0;
        for (Pair<Resource, Integer> requirementsResource : requirementsResources) {
            if (gamemodel.getCurrentPlayer().getPersonalBoard().getNumberOf(requirementsResource.getKey()) < requirementsResource.getValue())
                return false;
        }
        for (Pair<DevelopmentCardColor, Integer> requirementsCard : requirementsCards) {
            for (int j = 0; j < gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().size(); j++)
                temp +=gamemodel.getCurrentPlayer().getPersonalBoard().getCardCells().get(j).howManyOfColor(requirementsCard.getKey(),requirementsCardsLevel);
            if (temp < requirementsCard.getValue())
                return false;
            temp=0;
        }
        return true;
    }

}
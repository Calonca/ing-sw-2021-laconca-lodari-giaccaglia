package it.polimi.ingsw.server.model.player.leaders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import javafx.util.Pair;

import java.util.List;

/**
 * Concrete class for Market Leader. Upon activation, a Market bonus will be added to the player's board
 */

public class MarketLeader extends Leader
{
    private final Resource bonus;

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


    /**
     * Applies the leader's bonus to the Player
     * @param gamemodel != NULL
     */
    public void activate(GameModel gamemodel)
    {
        state = LeaderState.ACTIVE;
        gamemodel.getCurrentPlayer().applyMarketBonus(bonus);
    }

    public int getResourceBonusType(){
        return bonus.getResourceNumber();
    }

}
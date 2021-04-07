package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.LeaderState;
import javafx.util.Pair;

import java.util.List;

/**
 * This leader needs a different activation function, as it needs to add its production to the player's board
 */
public class ProductionLeader extends Leader
{

    private LeaderState state;
    public int victorypoints;
    public List<Pair<Resource, Integer>> requirementsResources;
    public List<Pair<DevelopmentCardColor, Integer>> requirementsCards;
    public int requirementsCardsLevel=1;
    public Production production;

    public LeaderState getState()
    {
        return state;
    }

    public ProductionLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirementsResources, List<Pair<DevelopmentCardColor, Integer>> requirementsCards, Production production, int requirementsCardsLevel )
    {
        this.state = state;
        this.victoryPoints = victoryPoints;
        this.requirementsCards = requirementsCards;
        this.requirementsResources = requirementsResources;
        this.production= production;
        this.requirementsCardsLevel=requirementsCardsLevel;
    }
    public ProductionLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirementsResources, List<Pair<DevelopmentCardColor, Integer>> requirementsCards, Production production)
    {
        this(state, victoryPoints,requirementsResources,requirementsCards,production, 1);
    }
    /**
     * Adds the leader's production to the others
     * @param gamemodel !=NULL
     */
    public void activate(GameModel gamemodel)
    {
        state = LeaderState.ACTIVE;
        gamemodel.getCurrentPlayer().getPersonalBoard().addProduction(production);
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

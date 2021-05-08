package it.polimi.ingsw.server.model.player.leaders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.network.assets.devcards.DevelopmentCardColor;
import javafx.util.Pair;

import java.util.List;

/**
 * Concrete class for Production Leader. Upon activation, a Production will be added to the player's board
 */
public class ProductionLeader extends Leader
{
    public Production production;


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
    @Override
    public void activate(GameModel gamemodel)
    {
        state = LeaderState.ACTIVE;
        gamemodel.getCurrentPlayer().getPersonalBoard().addProduction(production);
    }


}

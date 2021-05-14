package it.polimi.ingsw.server.model.player.leaders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.board.LeaderDepot;
import javafx.util.Pair;

import java.util.List;

/**
 * Concrete class for Deposit Leader. Upon activation, a Leader Depot will be added to the player's board
 */
public class DepositLeader extends Leader
{

    public LeaderDepot leaderdepot;

    public DepositLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirementsResources, List<Pair<DevelopmentCardColor, Integer>> requirementsCards, LeaderDepot leaderdepot, int requirementsCardsLevel)
    {
        this.state = state;
        this.victoryPoints = victoryPoints;
        this.requirementsCards = requirementsCards;
        this.requirementsResources = requirementsResources;
        this.leaderdepot = leaderdepot;
        this.requirementsCardsLevel=requirementsCardsLevel;
    }

    public DepositLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirementsResources, List<Pair<DevelopmentCardColor, Integer>> requirementsCards, LeaderDepot leaderdepot)
    {
        this(state, victoryPoints,requirementsResources,requirementsCards,leaderdepot, 1);
    }
    /**
     * Basic Leader activation function, but adds a deposit
     * @param gamemodel != NULL
     * Adds the Leader's deposit to the player's board.
     */
    public void activate(GameModel gamemodel)
    {
        gamemodel.getCurrentPlayer().getPersonalBoard().getWarehouseLeadersDepots().addDepot(this.leaderdepot);
        state = LeaderState.ACTIVE; //assumo che il leader attivato sia "in cima"
    }

    public int getDepotResourcesType(){
        return leaderdepot.geResourceType();
    }

}

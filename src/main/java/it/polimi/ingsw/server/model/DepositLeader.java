package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.LeaderState;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.board.Depot;
import it.polimi.ingsw.server.model.player.board.LeaderDepot;
import javafx.util.Pair;

import java.util.List;

/**
 * Concrete class for Deposit Leader DUMMY
 */
public class DepositLeader extends Leader
{
    private LeaderState state;

    public LeaderDepot leaderdepot;
    public List<Pair<Resource, Integer>> requirementsResources;
    public List<Pair<DevelopmentCardColor, Integer>> requirementsCards;

    public DepositLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirementsResources, List<Pair<DevelopmentCardColor, Integer>> requirementsCards, LeaderDepot leaderdepot)
    {
        this.state = state;
        this.victoryPoints = victoryPoints;
        this.requirementsCards = requirementsCards;
        this.requirementsResources = requirementsResources;
        this.leaderdepot = leaderdepot;
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
}

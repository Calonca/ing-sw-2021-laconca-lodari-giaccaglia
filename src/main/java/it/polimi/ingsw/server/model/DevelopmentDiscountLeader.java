package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.LeaderState;
import javafx.util.Pair;

import java.util.List;

/**
 * This Leader class doesn't need extra methods, discount is used by gamemodel.
 */

public class DevelopmentDiscountLeader extends Leader
{
    private LeaderState state;
    private int victoryPoints;
    public List<Pair<Resource, Integer>> requirements;
    public Pair<Resource,Integer> discount;

    public DevelopmentDiscountLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirements, Pair<Resource,Integer> discount)
    {
        this.state = state;
        this.victoryPoints = victoryPoints;
        this.requirements = requirements;
        this.discount = discount;
    }


    public Pair<Resource, Integer> getDiscount()
    {
        return discount;
    }

    /**
     * Basic Leader activation function, but adds a deposit
     * @param gamemodel != NULL
     * Adds the Leader's discount to the player's board.
     */
    public void activate(GameModel gamemodel)
    {
        gamemodel.getCurrentPlayer().applyDiscount(discount);
        state = LeaderState.ACTIVE; //assumo che il leader attivato sia "in cima"
    }
}

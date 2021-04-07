package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.LeaderState;
import javafx.util.Pair;

import java.util.List;

/**
 * This Leader class' activate method adds the discount to the player's card market
 */

public class DevelopmentDiscountLeader extends Leader
{
    private LeaderState state;
    private int victoryPoints;
    public List<Pair<Resource, Integer>> requirementsResources;
    public List<Pair<DevelopmentCardColor, Integer>> requirementsCards;
    public Pair<Resource,Integer> discount;

    public DevelopmentDiscountLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirementsResources, List<Pair<DevelopmentCardColor, Integer>> requirementsCards, Pair<Resource,Integer> discount)
    {
        this.state = state;
        this.victoryPoints = victoryPoints;
        this.requirementsCards = requirementsCards;
        this.requirementsResources = requirementsResources;
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

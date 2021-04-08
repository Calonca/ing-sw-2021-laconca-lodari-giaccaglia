package it.polimi.ingsw.server.model.player.leaders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import javafx.util.Pair;

import java.util.List;

/**
 * This Leader class' activate method adds the discount to the player's card market
 */

public class DevelopmentDiscountLeader extends Leader
{

    private Pair<Resource,Integer> discount;

    public DevelopmentDiscountLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirementsResources, List<Pair<DevelopmentCardColor, Integer>> requirementsCards, Pair<Resource,Integer> discount, int requirementsCardsLevel)
    {
        this.state = state;
        this.victoryPoints = victoryPoints;
        this.requirementsCards = requirementsCards;
        this.requirementsResources = requirementsResources;
        this.discount = discount;
        this.requirementsCardsLevel=requirementsCardsLevel;
    }

    public DevelopmentDiscountLeader(LeaderState state, int victoryPoints, List<Pair<Resource,Integer>> requirementsResources, List<Pair<DevelopmentCardColor, Integer>> requirementsCards, Pair<Resource,Integer> discount)
    {
        this(state, victoryPoints,requirementsResources,requirementsCards,discount, 1);
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

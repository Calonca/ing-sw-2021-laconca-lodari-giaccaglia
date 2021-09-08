package it.polimi.ingsw.server.model.player.leaders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import javafx.util.Pair;

import java.util.List;

/**
 * Concrete class for Discount Leader. Upon activation, a Discount will be added to the player's board
 */
public class DevelopmentDiscountLeader extends Leader
{

    private final Pair<Resource,Integer> discount;

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

    public Pair<Integer, Integer> getDiscountAsIntegerPair() {
        return new Pair<>(discount.getKey().getResourceNumber(), discount.getValue());
    }
    /**
     * Basic Leader activation function, but adds a deposit
     * @param gamemodel != NULL
     * Adds the Leader's discount to the player's board.
     */
    public void activate(GameModel gamemodel)
    {
        gamemodel.getCurrentPlayer().getPersonalBoard().applyDiscount(discount);
        state = LeaderState.ACTIVE; //assumo che il leader attivato sia "in cima"
    }

}

package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.LeaderState;
import it.polimi.ingsw.server.model.player.Player;
import javafx.util.Pair;

/**
 * This Leader class doesn't need extra methods, discount is used by gamemodel.
 */

public class DevelopmentDiscountLeader extends Leader
{
    private LeaderState state;
    public Pair<Resource,Integer> discount;
    private void activate(GameModel gamemodel)
    {
        gamemodel.getCurrentPlayer().applyDiscount(discount);
        state = LeaderState.ACTIVE; //assumo che il leader attivato sia "in cima"
    }
}

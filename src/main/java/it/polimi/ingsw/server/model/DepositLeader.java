package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.LeaderState;
import it.polimi.ingsw.server.model.player.Player;

/**
 * Concrete class for Deposit Leader
 */
public class DepositLeader extends Leader
{
    private LeaderState state;
    /**
     * The object will be transferred to the playerboard once ativated
     */
    public LeaderDepot leaderdepot;

    /**
     * Basic Leader activation function, but adds a deposit
     * @param gamemodel != NULL
     * Adds the Leader's deposit to the player's board.
     */
    public void activate(GameModel gamemodel)
    {
        //gamemodel.getCurrentPlayer().getPersonalBoard().addDeposit(leaderdepot);
        state = LeaderState.ACTIVE; //assumo che il leader attivato sia "in cima"
    }
}

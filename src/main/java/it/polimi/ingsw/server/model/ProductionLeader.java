package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.LeaderState;
import it.polimi.ingsw.server.model.player.Player;

/**
 * This leader needs a different activation function, as it needs to add its production to the player's board
 */
public class ProductionLeader extends Leader
{

    private LeaderState state;
   // public Production production;

    /**
     * Adds the leader's production to the others
     * @param gamemodel !=NULL
     */

    /**
     * Basic Leader activation method
     * @param gamemodel!= NULL;
     * Ensures that the leader's status is ACTIVE after the call
     */
    public void activate(GameModel gamemodel)
    {
        state = LeaderState.ACTIVE;
       // gamemodel.getCurrentPlayer().getPersonalBoard.addProduction(production);
    }

}

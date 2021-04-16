package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

/**
 * This method simply sets the state for the view. If the player has no available leaders, it will
 * simply proceed, as if nothing happened.
 */
public class ShowLeaders extends LeaderStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT CHOOSELEADEREVENT
        if (gamemodel.getCurrentPlayer().getLeaders().get(0).anyLeaderPlayable(gamemodel))
            return State.SHOWING_LEADERS_INITIAL;
        else return State.LEADER_END;
    }
}

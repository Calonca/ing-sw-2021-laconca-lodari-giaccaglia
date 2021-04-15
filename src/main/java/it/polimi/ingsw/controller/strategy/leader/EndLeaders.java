package it.polimi.ingsw.controller.strategy.leader;

import it.polimi.ingsw.controller.strategy.leader.LeaderStrategy;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

/**
 *  This method uses a flag variable Past, to decide wether or not MIDDLE PHASE has already been played.
 */
public class EndLeaders extends LeaderStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT ACTIVATELEADEREVENT OR DISCARDLEADEREVENT
        if(gamemodel.getCurrentPlayer().getLeaders().get(0).isPast())
            return State.IDLE;
        else
            return State.MIDDLE_PHASE;
    }
}
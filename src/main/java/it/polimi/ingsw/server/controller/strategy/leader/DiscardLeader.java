package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;
import it.polimi.ingsw.server.model.player.leaders.LeaderState;

/**
 *  If the chosen Leader is playable, it is discarded. If not, nothing happens.
 */
public class DiscardLeader extends GameStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT DISCARDLEADEREVENT
        //MESSAGE IS INT 2
        if(gamemodel.getCurrentPlayer().getLeaders().get(2).getState()== LeaderState.INACTIVE)
            gamemodel.getCurrentPlayer().getLeaders().get(2).discard(gamemodel);
        return State.SHOWING_LEADERS_INITIAL;
    }
}

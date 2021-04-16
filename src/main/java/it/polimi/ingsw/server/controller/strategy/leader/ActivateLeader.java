package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;
import it.polimi.ingsw.server.model.player.leaders.LeaderState;

/**
 *  If the chosen Leader is playable, its effect is activated. If not, nothing happens.
 */
public class ActivateLeader extends LeaderStrategy
{
    public State execute(GameModel gamemodel)
    {
        //ON EVENT PLAYLEADEREVENT
        //MESSAGE IS INT 2
        if(gamemodel.getCurrentPlayer().getLeaders().get(2).areRequirementsSatisfied(gamemodel)&&
            gamemodel.getCurrentPlayer().getLeaders().get(2).getState()== LeaderState.INACTIVE)
                gamemodel.getCurrentPlayer().getLeaders().get(2).activate(gamemodel);
        return State.SHOWING_LEADERS_INITIAL;

    }
}

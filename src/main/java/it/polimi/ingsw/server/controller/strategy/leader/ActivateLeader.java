package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;
import it.polimi.ingsw.server.model.player.leaders.LeaderState;

/**
 *  If the chosen Leader is playable, its effect is activated. If not, nothing happens.
 */
public class ActivateLeader implements GameStrategy {
    public State execute(GameModel gamemodel)
    {
        //ON EVENT PLAYLEADEREVENT
        //MESSAGE IS INT 2
    //    if(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(gamemodel.getCurrentPlayer().getLeaders().get(2))&&
    //        gamemodel.getCurrentPlayer().getLeaders().get(2).getState()== LeaderState.INACTIVE)
        gamemodel.getCurrentPlayer().getLeaders().get(2).activate(gamemodel);

        if (gamemodel.getCurrentPlayer().anyLeaderPlayable())
            return gamemodel.getCurrentPlayer().getCurrentState();
        else if (gamemodel.getCurrentPlayer().getCurrentState()==State.SHOWING_LEADERS_INITIAL)
            return State.MIDDLE_PHASE;
        else return State.IDLE;

    }
}

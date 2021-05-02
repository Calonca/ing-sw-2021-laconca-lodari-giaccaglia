package it.polimi.ingsw.server.controller.strategy.leader;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.State;

/**
 *  If the chosen Leader is playable, its effect is activated. If not, nothing happens.
 */
public class ActivateLeader implements GameStrategy {
    public State execute(GameModel gamemodel, Validable event)
    {
        //ON EVENT PLAYLEADEREVENT
        //MESSAGE IS INT 2
    //    if(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(gamemodel.getCurrentPlayer().getLeaders().get(2))&&
    //        gamemodel.getCurrentPlayer().getLeaders().get(2).getState()== LeaderState.INACTIVE)
        gamemodel.getCurrentPlayer().getLeaders().get(2).activate(gamemodel);

        return new EndLeaders().execute(gamemodel, );


    }
}
